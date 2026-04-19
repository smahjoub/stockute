package com.smahjoub.stockute.application.port.security.out;

import com.smahjoub.stockute.domain.model.Security;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SecurityPort {
    Flux<Security> searchBySymbolOrName(String keyword);
    Mono<Security> findBySymbol(String symbol);
    Mono<Security> save(Security security);
    Flux<Security> saveAll(Flux<Security> securities);
}