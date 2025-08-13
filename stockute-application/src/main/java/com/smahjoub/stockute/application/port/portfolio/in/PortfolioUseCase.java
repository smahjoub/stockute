package com.smahjoub.stockute.application.port.portfolio.in;

import com.smahjoub.stockute.domain.model.Portfolio;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PortfolioUseCase {
    Mono<Portfolio> createPortfolio(Portfolio portfolio);
    Mono<Portfolio> getPortfolio(Long id);
    Flux<Portfolio> getAllPortfolios();
    Mono<Void> removePortfolio(Long id);
}
