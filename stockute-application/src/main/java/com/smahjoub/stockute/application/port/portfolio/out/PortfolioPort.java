package com.smahjoub.stockute.application.port.portfolio.out;


import com.smahjoub.stockute.domain.model.Portfolio;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PortfolioPort {
    Mono<Portfolio> save(Portfolio portfolio);
    Mono<Portfolio> findById(Long id);
    Flux<Portfolio> findAll();
    Mono<Void> deleteById(Long id);
}
