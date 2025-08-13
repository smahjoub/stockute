package com.smahjoub.stockute.adapters.restful.portfolio.controller;

import com.smahjoub.stockute.application.port.portfolio.in.PortfolioUseCase;
import com.smahjoub.stockute.domain.model.Portfolio;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/portfolios")
@AllArgsConstructor
public class PortfolioController {
    private final PortfolioUseCase useCase;

    @PostMapping
    public Mono<Portfolio> create(@RequestBody final Portfolio portfolio) {
        return useCase.createPortfolio(portfolio);
    }

    @GetMapping("/{id}")
    public Mono<Portfolio> get(@PathVariable final Long id) {
        return useCase.getPortfolio(id);
    }

    @GetMapping
    public Flux<Portfolio> getAll() {
        return useCase.getAllPortfolios();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable final Long id) {
        return useCase.removePortfolio(id);
    }
}