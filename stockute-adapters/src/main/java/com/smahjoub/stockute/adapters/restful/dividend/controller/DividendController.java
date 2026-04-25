package com.smahjoub.stockute.adapters.restful.dividend.controller;

import com.smahjoub.stockute.adapters.restful.dividend.dto.DividendEntitlementDto;
import com.smahjoub.stockute.adapters.restful.dividend.mapper.DividendEntitlementMapper;
import com.smahjoub.stockute.application.port.dividend.in.PortfolioDividendEntitlementUseCase;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/portfolios")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
@AllArgsConstructor
public class DividendController {

    private final DividendEntitlementMapper dividendEntitlementMapper;
    private final PortfolioDividendEntitlementUseCase portfolioDividendEntitlementUseCase;

    @GetMapping("/{portfolioId}/assets/{assetId}/dividends")
    public Flux<DividendEntitlementDto> getDividendEntitlementsForPortfolio(@PathVariable("portfolioId") final Long portfolioId,
                                                                            @PathVariable("assetId") final Long asserId) {
        return portfolioDividendEntitlementUseCase.getDividendEntitlementsForPortfolio(portfolioId, asserId)
                .map(dividendEntitlementMapper::toDto);
    }
}
