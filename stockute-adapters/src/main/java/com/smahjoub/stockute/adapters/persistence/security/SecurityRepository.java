package com.smahjoub.stockute.adapters.persistence.security;

import com.smahjoub.stockute.domain.model.Security;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SecurityRepository extends R2dbcRepository<Security, Long> {

    Flux<Security> findBySymbolContainingIgnoreCase(String symbol);

    Flux<Security> findByNameContainingIgnoreCase(String name);

    Mono<Security> findBySymbol(String symbol);
}