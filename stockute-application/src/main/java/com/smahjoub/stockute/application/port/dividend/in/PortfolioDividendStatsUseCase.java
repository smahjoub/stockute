package com.smahjoub.stockute.application.port.dividend.in;

import com.smahjoub.stockute.application.port.dividend.in.response.PortfolioDividendStats;
import reactor.core.publisher.Mono;

public interface PortfolioDividendStatsUseCase {
    Mono<PortfolioDividendStats> getDividendStatsForPortfolio(final Long portfolioRefId);
}