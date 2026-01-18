package com.sse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sse.service.SseService;
import com.sse.util.ApiResponse;
import com.sse.util.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
@Slf4j
public class SseController {
    private final SseService sseService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final Scheduler customScheduler = Schedulers.fromExecutorService(executorService);

    @GetMapping("/emitter")
    public SseEmitter getSseEmitter() {
        log.info("/emitter");
        SseEmitter sseEmitter = new SseEmitter();
        log.info("sseEmitter");
        sseService.getDataFlux()
                .publishOn(Schedulers.fromExecutorService(executorService))
                .doOnNext(data -> log.info("Processing data on thread: {}", Thread.currentThread().getName()))
                .subscribe(data -> {
                    log.info("subscription done");
                    try {
                        sseEmitter.send(new ApiResponse<>(data));
                    } catch (Exception e) {
                        log.info("error get");
                        sseEmitter.completeWithError(e);
                    }
//                    Try.withException(() -> sseEmitter.send(new ApiResponse<>(data)), e -> {
//                        log.info("error get");
//                        sseEmitter.completeWithError(e);
//                        throw new RuntimeException("error get");
//                    });
                }, sseEmitter::completeWithError, sseEmitter::complete);
        log.info("endEmitter");
        return sseEmitter;
    }

    //curl -N http://localhost:8080/sse/stream
    @GetMapping(value = "/stream")
    public ResponseEntity<StreamingResponseBody> getStreamingData() {
        log.info("getStreamingData");
        StreamingResponseBody errorDuringDataStreaming =  outputStream -> {
            Try.withResource(() -> outputStream, os -> {
                Try.withException(() -> {
                    for (int i = 0; i <= 10; i++) {
                        String data = i + "::";
                        outputStream.write(data.getBytes());
                        outputStream.flush();
                        Thread.sleep(1000);
                    }
                }, e -> {
                    throw new RuntimeException("Error during data streaming", e);
                });
            });
        };
        return ResponseEntity.ok().body(errorDuringDataStreaming);
    }

    // curl -N http://localhost:8080/sse/h
    @GetMapping(value = "/h", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public StreamingResponseBody handleRequest() {
        return out -> {
            for (int i = 0; i < 10; i++) {
                ObjectMapper objectMapper = new ObjectMapper();
                byte[] bytes = objectMapper.writeValueAsBytes(new ApiResponse<>(i));
                out.write(bytes);
                out.flush();
                Try.withException(() -> Thread.sleep(1000));
            }
        };
    }

    //curl -N http://localhost:8080/sse/h2
    @GetMapping("/h2")
    public ResponseEntity<StreamingResponseBody> handleRequest2() {
        StreamingResponseBody stream = out -> {
            for (int i = 0; i < 10; i++) {
                ObjectMapper objectMapper = new ObjectMapper();
                byte[] bytes = objectMapper.writeValueAsBytes(new ApiResponse<>(i));
                out.write(bytes);
                out.write("\n".getBytes());
                out.flush();
                Try.withException(() -> Thread.sleep(1000));
            }
        };
        return ResponseEntity.ok(stream);
    }

    @GetMapping("/future")
    public CompletableFuture<ApiResponse<String>> getCompletableFuture() {
        log.info("future start on Thread: {}", Thread.currentThread().getName());
        return CompletableFuture.supplyAsync(() -> {
            Try.withException(() -> Thread.sleep(2000));
            return new ApiResponse<>("Lokesh");
        }).whenComplete((stringApiResponse, throwable) -> log.info("future done in thread: {}", Thread.currentThread().getName()));
    }

    @GetMapping("/deferred")
    public DeferredResult<ApiResponse<String>> getDeferred() {
        DeferredResult<ApiResponse<String>> result = new DeferredResult<>();
        log.info("deferred start on Thread: {}", Thread.currentThread().getName());
        CompletableFuture.supplyAsync(() -> {
            log.info("deferred thread {}", Thread.currentThread().getName());
            Try.withException(() -> Thread.sleep(2000));
            log.info("deferred response");
            return new ApiResponse<>("Lokesh");
        }).whenComplete((stringApiResponse, throwable) -> result.setResult(stringApiResponse));
        log.info("deferred out");
        return result;
    }

    @GetMapping("/callable")
    public Callable<ApiResponse<String>> getCallable() {
        log.info("callable in thread: {}", Thread.currentThread().getName());
        return () -> {
            log.info("callable thread {}", Thread.currentThread().getName());
            Try.withException(() -> Thread.sleep(2000));
            log.info("callable response");
            return new ApiResponse<>("Lokesh");
        };
    }
}
