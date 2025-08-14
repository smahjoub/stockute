package com.smahjoub.stockute.application.port.portfolio.in;

import com.smahjoub.stockute.domain.model.Portfolio;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PortfolioUseCase {
    Mono<Portfolio> createPortfolio(String userName, Portfolio portfolio);
    Mono<Portfolio> updatePortfolio(String userName, Portfolio portfolio);

    Mono<Portfolio> getUserPortfolio(String userName, Long id);
    Flux<Portfolio> getAllUserPortfolios(String userName);
    Mono<Void> removePortfolio(String userName, Long id);
}
