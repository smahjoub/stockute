package com.smahjoub.stockute.application.service.dividend;
import com.smahjoub.stockute.application.port.dividend.in.response.PortfolioDividendStats;
import com.smahjoub.stockute.application.port.dividend.in.PortfolioDividendStatsUseCase;
import com.smahjoub.stockute.application.port.dividend.out.PortfolioDividendEntitlementPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PortfolioDividendStatsService implements PortfolioDividendStatsUseCase {

    private final PortfolioDividendEntitlementPort portfolioDividendEntitlementPort;
    private final DividendGrowthRateService dividendGrowthRateService;

    @Override
    public Mono<PortfolioDividendStats> getDividendStatsForPortfolio(final Long portfolioRefId) {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime oneMonthAgo = now.minusMonths(1);
        final LocalDateTime oneYearAgo = now.minusYears(1);

        final Mono<BigDecimal> pastMonthAccumulatedMono = sumGrossAmountsForPeriod(portfolioRefId, oneMonthAgo, now);
        final Mono<BigDecimal> pastYearAccumulatedMono = sumGrossAmountsForPeriod(portfolioRefId, oneYearAgo, now);
        final Mono<BigDecimal> incomingDividendMono = Mono.just(BigDecimal.ZERO);
        final Mono<BigDecimal> growthRateMono = dividendGrowthRateService.getGrowthRateForPortfolio(portfolioRefId);

        return Mono.zip(pastMonthAccumulatedMono, pastYearAccumulatedMono, incomingDividendMono, growthRateMono)
                .map(tuple -> {
                    final BigDecimal pastMonthAccumulated = tuple.getT1();
                    final BigDecimal pastYearAccumulated = tuple.getT2();
                    final BigDecimal incomingDividend = tuple.getT3();
                    final BigDecimal growthRate = tuple.getT4();
                    final BigDecimal nextYearForecast = pastYearAccumulated.multiply(BigDecimal.ONE.add(growthRate));

                    return new PortfolioDividendStats(
                            pastMonthAccumulated,
                            pastYearAccumulated,
                            incomingDividend,
                            nextYearForecast
                    );
                });
    }

    private Mono<BigDecimal> sumGrossAmountsForPeriod(
            final Long portfolioRefId,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        return portfolioDividendEntitlementPort
                .findAllByPortfolioRefIdAndPaymentDateBetween(portfolioRefId, startDate, endDate)
                .map(entitlement -> entitlement.getGrossAmount() == null ? BigDecimal.ZERO : entitlement.getGrossAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}