package com.r2dbc.demo.service;

import com.r2dbc.demo.dto.DBEvent;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class PGNotifyListener {
    private final DatabaseClient databaseClient;
    private Disposable disposable;
    private final R2dbcProperties r2dbcProperties;
    private final ReactiveEventBus eventBus;

    @PostConstruct
    public void listenPgEvents() {
        log.info("url: {}, username: {}, password: {}", r2dbcProperties.getUrl(), r2dbcProperties.getUsername(), r2dbcProperties.getPassword());
        ConnectionFactoryOptions factoryOptions = ConnectionFactoryOptions.parse(r2dbcProperties.getUrl())
                .mutate()
                .option(ConnectionFactoryOptions.USER, r2dbcProperties.getUsername())
                .option(ConnectionFactoryOptions.PASSWORD, r2dbcProperties.getPassword())
                .build();

        ConnectionFactory factory = ConnectionFactories.get(factoryOptions);

        disposable = Flux.usingWhen(
                        Mono.from(factory.create())
                                .ofType(PostgresqlConnection.class),
                        conn -> conn.createStatement("LISTEN app_event")
                                .execute()
                                .thenMany(conn.getNotifications()),
                        conn -> conn.close()
                                .doFinally(signalType -> log.info("Listen Connection closed"))
                )
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofSeconds(2))
                        .doBeforeRetry(rs -> log.info("Retrying Postgres LISTEN (attempt {})", rs.totalRetries() + 1)))
                .doOnNext(notification -> {
                    String payload = notification.getParameter();
                    log.info("Reactive received: {}", payload);
                    eventBus.publish(new DBEvent(payload));
                })
                .subscribe();

//        Mono.from(factory.create())
//                .flatMap(conn ->
//                        Mono.when(
//                                Mono.from(conn.createStatement(
//                                                "SELECT pg_notify('app_event', 'Hello from R2DBC!')")
//                                        .execute()
//                                ),
//                                Mono.from(conn.createStatement(
//                                                "NOTIFY app_event, 'Hello from Java!'")
//                                        .execute()
//                                )
//                        ).then(Mono.from(conn.close()))
//                ).subscribe();
    }

    @PreDestroy
    public void close() {
        Optional.ofNullable(disposable)
                .ifPresent(Disposable::dispose);
    }

    /*SELECT * FROM pg_listening_channels();

    SELECT pid, query
    FROM pg_stat_activity
    WHERE query LIKE '%LISTEN%';*/
}
