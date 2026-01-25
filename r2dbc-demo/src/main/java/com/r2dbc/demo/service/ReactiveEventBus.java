package com.r2dbc.demo.service;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ReactiveEventBus {
    private final Sinks.Many<Object> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final ExecutorService eventExecutor = Executors.newSingleThreadExecutor();

    public <T> Flux<T> consume(Class<T> type) {
        return sink.asFlux()
                .ofType(type)
                .share();
    }

    public <T> void publish(T event) {
//        eventExecutor.submit(() -> sink.tryEmitNext(event));
//        Schedulers.newSingle("event-sink").schedule(() -> sink.tryEmitNext(event));
//        sink.emitNext(event, Sinks.EmitFailureHandler.busyLooping(Duration.ofMillis(100)));
        sink.emitNext(event, (signalType, emitResult) -> emitResult.equals(Sinks.EmitResult.FAIL_NON_SERIALIZED));
    }
}
