package com.smahjoub.stockute.adapters.persistence.portfolio;


import com.smahjoub.stockute.application.port.portfolio.out.PortfolioPort;
import com.smahjoub.stockute.domain.model.Portfolio;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PortfolioAdapter implements PortfolioPort {

    private final PortfolioRepository portfolioRepository;

    @Override
    public Mono<Portfolio> save(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    @Override
    public Mono<Portfolio> findById(Long id) {
        return portfolioRepository.findById(id);
    }

    @Override
    public Flux<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return portfolioRepository.deleteById(id);
    }
}
