package com.smahjoub.stockute.adapters.restful.portfolio.controller;

import com.smahjoub.stockute.adapters.restful.portfolio.dto.CreatePortfolioDTO;
import com.smahjoub.stockute.adapters.restful.portfolio.dto.PortfolioDTO;
import com.smahjoub.stockute.adapters.restful.portfolio.dto.UpdatePortfolioDTO;
import com.smahjoub.stockute.adapters.restful.portfolio.mapper.CreatePortfolioRecordMapper;
import com.smahjoub.stockute.adapters.restful.portfolio.mapper.PortfolioMapper;
import com.smahjoub.stockute.adapters.restful.portfolio.mapper.UpdatePortfolioRecordMapper;
import com.smahjoub.stockute.application.port.portfolio.in.PortfolioUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.smahjoub.stockute.adapters.helper.AuthenticationHelper.getAuthenticatedUserName;

@RestController
@RequestMapping("/portfolios")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
@AllArgsConstructor
public class PortfolioController {
    private final PortfolioUseCase useCase;
    private final CreatePortfolioRecordMapper createPortfolioRecordMapper;
    private final UpdatePortfolioRecordMapper updatePortfolioRecordMapper;
    private final PortfolioMapper portfolioMapper;

    @PostMapping
    public Mono<PortfolioDTO> create(
            @RequestBody final CreatePortfolioDTO createPortfolioDTO) {
        return getAuthenticatedUserName().flatMap(userName -> useCase.createPortfolio(userName, createPortfolioRecordMapper.toPortfolio(createPortfolioDTO))
                .map(portfolioMapper::toPortfolioDTO));
    }

    @GetMapping("/{id}")
    public Mono<PortfolioDTO> get(@PathVariable final Long id) {
        return getAuthenticatedUserName().flatMap(
                userName ->
                        useCase.getUserPortfolio(userName, id).map(portfolioMapper::toPortfolioDTO)
        );
    }

    @GetMapping
    public Flux<PortfolioDTO> getAll() {
        return getAuthenticatedUserName().flatMapMany(userName -> useCase.getAllUserPortfolios(userName).map(portfolioMapper::toPortfolioDTO));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable final Long id) {

        return getAuthenticatedUserName().flatMap(userName -> useCase.removePortfolio(userName, id));
    }


    @PutMapping("/{id}")
    public Mono<PortfolioDTO> update(@RequestBody final UpdatePortfolioDTO updatePortfolioDTO,
                                     @PathVariable final Long id,
                                     @RequestHeader(value = HttpHeaders.IF_MATCH) final Long version
    ) {
        return getAuthenticatedUserName().flatMap(userName -> useCase.getUserPortfolio(userName, id)
                .flatMap(portfolio -> {
                    if (!portfolio.getVersion().equals(version)) {
                        return Mono.error(new IllegalArgumentException("Portfolio version mismatch"));
                    }
                    return useCase.updatePortfolio(userName, updatePortfolioRecordMapper.updatePortfolioFromDto(updatePortfolioDTO, portfolio));
                }).map(portfolioMapper::toPortfolioDTO));
    }
}