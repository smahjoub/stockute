package com.smahjoub.stockute.application.service.portfolio;


import com.smahjoub.stockute.application.port.portfolio.in.PortfolioUseCase;
import com.smahjoub.stockute.application.port.portfolio.out.PortfolioPort;
import com.smahjoub.stockute.domain.model.Portfolio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class PortfolioService implements PortfolioUseCase {
    private final PortfolioPort repository;

    @Override
    public Mono<Portfolio> createPortfolio(Portfolio portfolio) {
        return repository.save(portfolio);
    }

    @Override
    public Mono<Portfolio> getPortfolio(Long id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Portfolio> getAllPortfolios() {
        return repository.findAll();
    }

    @Override
    public Mono<Void> removePortfolio(Long id) {
        return repository.deleteById(id);
    }
}