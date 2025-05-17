package com.smahjoub.stockute.adapters.restful;

import com.smahjoub.stockute.application.port.in.PortfolioUseCase;
import com.smahjoub.stockute.domain.model.Portfolio;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/portfolios")
public class PortfolioController {
    private final PortfolioUseCase useCase;

    public PortfolioController(PortfolioUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public Mono<Portfolio> create(@RequestBody Portfolio portfolio) {
        return useCase.createPortfolio(portfolio);
    }

    @GetMapping("/{id}")
    public Mono<Portfolio> get(@PathVariable Long id) {
        return useCase.getPortfolio(id);
    }

    @GetMapping
    public Flux<Portfolio> getAll() {
        return useCase.getAllPortfolios();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return useCase.removePortfolio(id);
    }
}