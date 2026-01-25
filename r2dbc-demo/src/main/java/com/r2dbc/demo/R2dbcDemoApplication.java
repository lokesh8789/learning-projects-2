package com.r2dbc.demo;

import com.r2dbc.demo.dto.PDto;
import com.r2dbc.demo.service.ReactiveEventBus;
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.processors.ReplayProcessor;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

@SpringBootApplication
public class R2dbcDemoApplication {
	Logger logger = LoggerFactory.getLogger(R2dbcDemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(R2dbcDemoApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(ReactiveEventBus eventBus) {
		return args -> {
			RxJava3Adapter.fluxToFlowable(Flux.just(12,23,34,45,46))
					.doOnNext(integer -> logger.info("{}", integer))
					.subscribe();
			Observable.fromSupplier(() -> List.of(11,22,33))
					.subscribeOn(Schedulers.computation())
					.doOnNext(list -> logger.info("{}", list))
					.subscribe();
			Single.fromSupplier(() -> List.of(1,2,3))
					.subscribeOn(Schedulers.from(Executors.newVirtualThreadPerTaskExecutor()))
					.doOnSuccess(list -> logger.info("{}", list))
					.subscribe();
			Maybe.just(23)
					.subscribeOn(Schedulers.io())
					.subscribe(integer -> logger.info("{}", integer));
			Completable.complete()
					.subscribe(() -> logger.info("Completable.complete()"));

			ReplaySubject<String> replaySubject = ReplaySubject.<String>create();
			replaySubject.onNext("Hello");
			Observable<String> hide = replaySubject.hide();
			hide.subscribe(s -> logger.info("{}", s));

			ReplayProcessor<String> replayProcessor = ReplayProcessor.<String>create();
			replayProcessor.onNext("Ok");
			Flowable<String> hide1 = replayProcessor.hide();
			hide1.subscribeOn(Schedulers.single(), true)
					.subscribe(s -> logger.info("{}", s));

			eventBus.consume(PDto.class)
					.subscribe(System.out::println);

			eventBus.publish(new PDto(Map.of("HI", "Hello"), "Lokesh"));

			eventBus.consume(Test.class)
					.delayElements(Duration.ofSeconds(2))
					.subscribe(System.out::println);

			eventBus.consume(User.class)
					.delayElements(Duration.ofSeconds(2))
					.subscribe(System.out::println);

			eventBus.consume(User.class)
					.delayElements(Duration.ofSeconds(2))
                    .<User>handle((user, sink) -> {
                        if (user.name().equals("User5")) {
                            sink.error(new RuntimeException("User5 not allowed"));
                            return;
                        }
                        sink.next(user);
                    })
					.onErrorContinue((throwable, o) -> System.out.println("error: " + throwable.getMessage()))
					.subscribe(System.out::println);

			eventBus.consume(Owner.class)
					.delayElements(Duration.ofSeconds(2))
					.subscribe(System.out::println);

			Flux<Long> test = Flux.interval(Duration.ofMillis(1))
					.doOnNext(aLong -> eventBus.publish(new Test("Test" + aLong)))
					.take(4);

			Flux<Long> user = Flux.interval(Duration.ofMillis(1))
					.doOnNext(aLong -> eventBus.publish(new User("User" + aLong)))
					.take(4);

			Flux<Long> owner = Flux.interval(Duration.ofMillis(1))
					.doOnNext(aLong -> eventBus.publish(new Owner("Owner" + aLong)))
					.take(4);

			Flux.zip(test, user, owner)
					.subscribe();
		};
	}

	record Test(String name) {}
	record User(String name) {}
	record Owner(String name) {}

}
