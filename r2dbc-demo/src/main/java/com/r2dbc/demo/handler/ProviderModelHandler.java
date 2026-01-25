package com.r2dbc.demo.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2dbc.demo.repo.ProviderModelRepository;
import com.r2dbc.demo.service.ReactiveEventBus;
import com.r2dbc.demo.enums.Status;
import com.r2dbc.demo.dto.DBEvent;
import com.r2dbc.demo.dto.ProviderDTO;
import com.r2dbc.demo.entity.ProviderModel;
import io.r2dbc.postgresql.codec.Json;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderModelHandler {
    private final ProviderModelRepository providerModelRepository;
    private final ObjectMapper objectMapper;
    private final DatabaseClient databaseClient;
    private final ReactiveEventBus reactiveEventBus;

    @PostConstruct
    public void init() {
        reactiveEventBus.consume(DBEvent.class)
                .flatMap(dbEvent -> Flux.merge(handleDbEvent(dbEvent), handleDbEvent2(dbEvent)))
                .subscribe();
    }

    private Mono<Void> handleDbEvent(DBEvent dbEvent) {
        log.info("DBEvent: {}", dbEvent);
        return Mono.empty();
    }

    private Mono<Void> handleDbEvent2(DBEvent dbEvent) {
        log.info("DBEvent2: {}", dbEvent);
        return Mono.empty();
    }

    public Mono<ServerResponse> getModelsGroupedByProvider(
            ServerRequest request) {
        String modelType = request.queryParam("modelType")
                .orElseThrow(() -> new IllegalArgumentException("modelType is required"));
        log.info("Fetching models grouped by provider for type: {}", modelType);

        R2dbcJsonbMapCleaner cleaner = new R2dbcJsonbMapCleaner(objectMapper, Map.of(
                "details", ProviderDTO.class
                // add more JSONB fields as needed
        ));

        Function<Integer, Flux<Integer>> expandFunc = n -> {
            if (n > 3) return Flux.empty();
            return Flux.just(n * 2, n * 2 + 1);
        };

        Flux<Integer> bfs = Flux.just(1)
                .expand(expandFunc);

        bfs.subscribe(System.out::println);
        Flux<Integer> dfs = Flux.just(1)
                .expandDeep(expandFunc);

        dfs.subscribe(System.out::println);

        ProviderModel build = ProviderModel.builder()
                .provider("OPENAI")
                .modelId("gpt-4.1")
                .modelType("LLM")
                .status(Status.ACTIVE)
                .details(Map.of(
                        "id", "65"
                ))
                .build();

        Flux<Object> flux = providerModelRepository.save(build)
                .flatMapMany(providerModel -> databaseClient.sql("select details, id from provider_models where model_type = :modelType and status = 'ACTIVE'")
                        .bind("modelType", modelType)
                        .map((row, rowMetadata) -> {
                            byte[] details = Optional.ofNullable(row.get("details", byte[].class))
                                    .orElse(null);
                            String id = Optional.ofNullable(row.get("id", String.class))
                                    .orElseThrow(() -> new IllegalArgumentException("id is required"));
                            Map<String, Object> result = new HashMap<>();
                            result.put("id", id);
                            result.put("details", details == null ? null : executeInException(() -> objectMapper.readValue(new String(details), ProviderDTO.class)));
                            return result;
                        })
                        .all()
                        .cast(Object.class));
//                .fetch()
//                .all()
//                .map(row -> objectMapper.convertValue(cleaner.clean(row), PDto.class));
//        return providerModelRepository.findByModelTypeAndStatus(modelType, Status.ACTIVE)
//                .map(pIn -> Map.of(
//                        "id", pIn.getId(),
//                        "details", objectMapper.convertValue(pIn.getDetails(), ProviderDTO.class)
//                ))
//                .cast(Object.class)
//                .onErrorMap(throwable -> new RuntimeException(throwable.getMessage()));

        return ServerResponse.ok()
                .body(flux, Object.class);
    }

    public static class R2dbcJsonbMapCleaner {
        private final ObjectMapper objectMapper;
        private final Map<String, Class<?>> jsonbFieldTypes;

        public R2dbcJsonbMapCleaner(ObjectMapper objectMapper, Map<String, Class<?>> jsonbFieldTypes) {
            this.objectMapper = objectMapper;
            this.jsonbFieldTypes = jsonbFieldTypes;
        }

        public Map<String, Object> clean(Map<String, Object> row) {
            Map<String, Object> fixed = new HashMap<>(row);
            fixed.replaceAll((k, v) -> {
                if (v instanceof Json json) {
                    Class<?> targetType = jsonbFieldTypes.getOrDefault(k, Map.class);
                    return executeInException(() -> objectMapper.readValue(json.asString(), targetType));
                }
                return v;
            });
            return fixed;
        }
    }

    public static <T> T executeInException(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
