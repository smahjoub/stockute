package com.smahjoub.stockute.adapters.restful.portfolio.controller;

import com.smahjoub.stockute.adapters.restful.portfolio.dto.CreatePortfolioDTO;
import com.smahjoub.stockute.adapters.restful.portfolio.dto.UpdatePortfolioDTO;
import com.smahjoub.stockute.adapters.restful.portfolio.mapper.CreatePortfolioRecordMapper;
import com.smahjoub.stockute.adapters.restful.portfolio.mapper.UpdatePortfolioRecordMapper;
import com.smahjoub.stockute.application.port.portfolio.in.PortfolioUseCase;
import com.smahjoub.stockute.domain.model.Portfolio;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/portfolios")
@AllArgsConstructor
public class PortfolioController {
    private final PortfolioUseCase useCase;
    private final CreatePortfolioRecordMapper createPortfolioRecordMapper;
    private final UpdatePortfolioRecordMapper updatePortfolioRecordMapper;

    @PostMapping
    public Mono<Portfolio> create(final Authentication authentication,
                                  @RequestBody final CreatePortfolioDTO createPortfolioDTO) {
        final String userName = authentication.getName();
        return useCase.createPortfolio(userName, createPortfolioRecordMapper.toPortfolio(createPortfolioDTO));
    }

    @GetMapping("/{id}")
    public Mono<Portfolio> get(final Authentication authentication, @PathVariable final Long id) {
        final String userName = authentication.getName();
        return useCase.getUserPortfolio(userName, id);
    }

    @GetMapping
    public Flux<Portfolio> getAll(final Authentication authentication) {
        final String userName = authentication.getName();
        return useCase.getAllUserPortfolios(userName);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(final Authentication authentication, @PathVariable final Long id) {

        final String userName = authentication.getName();
        return useCase.removePortfolio(userName, id);
    }


    @PutMapping("/{id}")
    public Mono<Portfolio> update(final Authentication authentication,
                                  @RequestBody final UpdatePortfolioDTO updatePortfolioDTO,
                                  @PathVariable final Long id,
                                  @RequestHeader(value = HttpHeaders.IF_MATCH) final Long version
    ) {
        final String userName = authentication.getName();
        return useCase.getUserPortfolio(userName, id)
                .flatMap(portfolio -> {
                    if (!portfolio.getVersion().equals(version)) {
                        return Mono.error(new IllegalArgumentException("Portfolio version mismatch"));
                    }
                    return useCase.updatePortfolio(userName, updatePortfolioRecordMapper.updatePortfolioFromDto(updatePortfolioDTO, portfolio));
                });
    }
}