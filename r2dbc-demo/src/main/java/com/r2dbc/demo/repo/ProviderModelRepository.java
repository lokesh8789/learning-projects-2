package com.r2dbc.demo.repo;

import com.r2dbc.demo.enums.Status;
import com.r2dbc.demo.dto.PIn;
import com.r2dbc.demo.entity.ProviderModel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface ProviderModelRepository extends R2dbcRepository<ProviderModel, Long> {

    Flux<ProviderModel> findByProviderAndModelTypeAndStatus(String provider, String modelType, Status status);

    @Query(value = """
            SELECT p.details, p.id
            FROM provider_models p
            WHERE p.model_type = :modelType AND p.status = :status
           """
    )
    Flux<PIn> findByModelTypeAndStatus(String modelType, Status status);

    @Query(value = """
            SELECT DISTINCT p.provider
            FROM provider_models p
            WHERE p.model_type = :modelType AND p.status = 'ACTIVE'
           """
    )
    Flux<String> findDistinctProvidersByModelType(String modelType);
}
