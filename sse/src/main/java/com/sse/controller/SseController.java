package com.sse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sse.service.SseService;
import com.sse.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

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
                .doOnNext(data -> log.info("Processing data on thread: " + Thread.currentThread().getName()))
                .subscribe(data -> {
                    log.info("subscrtion done");
                    try {
                        sseEmitter.send(new ApiResponse<>(data));
                    } catch (Exception e) {
                        log.info("error gget");
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);
        log.info("endEmitter");
        return sseEmitter;
    }

    @GetMapping(value = "/stream")
    public ResponseEntity<StreamingResponseBody> getStreamingData() {
        log.info("getStreamingData");
        StreamingResponseBody errorDuringDataStreaming =  outputStream -> {
            try {
                for (int i = 3; i <= 700; i++) {
                    String data = String.valueOf(i) + "::";
                    outputStream.write(data.getBytes());
                    outputStream.flush();
                    Thread.sleep(500); // Simulate delay for demonstration
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Error during data streaming", e);
            } finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        return ResponseEntity.ok().body(errorDuringDataStreaming);
    }
    @GetMapping("/")
    public StreamingResponseBody handleRequest () {

        return out -> {
            for (int i = 0; i < 1000; i++) {
                ObjectMapper objectMapper = new ObjectMapper();
                byte[] bytes = objectMapper.writeValueAsBytes(new ApiResponse<>(i));
                out.write(bytes);
                out.flush();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @GetMapping("/h")
    public ResponseEntity<StreamingResponseBody> handleRequest2() {
        StreamingResponseBody stream = out -> {
            for (int i = 0; i < 1000; i++) {
                ObjectMapper objectMapper = new ObjectMapper();
                byte[] bytes = objectMapper.writeValueAsBytes(new ApiResponse<>(i));
                out.write(bytes);
                out.write("\n".getBytes());
                out.flush();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return ResponseEntity.ok(stream);
    }

    @GetMapping("/future")
    public CompletableFuture<ApiResponse<String>> getCompletableFuture() {
        log.info("future start");
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return new ApiResponse<>("Lokesh");
        }).whenComplete((stringApiResponse, throwable) -> log.info("future done"));
    }

    @GetMapping("/deferred")
    public DeferredResult<ApiResponse<String>> getDeferred() {
        DeferredResult<ApiResponse<String>> result = new DeferredResult<>();
        log.info("deferred");
        CompletableFuture.supplyAsync(() -> {
            try {
                log.info("deferred thread in");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("deferred response");
            return new ApiResponse<>("Lokesh");
        }).whenComplete((stringApiResponse, throwable) -> result.setResult(stringApiResponse));
        log.info("deferred out");
        return result;
    }

    @GetMapping("/callable")
    public Callable<ApiResponse<String>> getCallable() {
        log.info("callable");
        return () -> {
            try {
                log.info("callable thread in");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("callable response");
            return new ApiResponse<>("Lokesh");
        };
    }
}
