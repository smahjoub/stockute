package com.smahjoub.stockute.application.service.dividend;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
@Service
public class DividendGrowthRateService {
    public Mono<BigDecimal> getGrowthRateForPortfolio(final Long portfolioRefId) {
        return Mono.just(new BigDecimal("0.02"));
    }
}