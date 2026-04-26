package com.smahjoub.stockute.application.service.dividend;

import com.smahjoub.stockute.application.port.dividend.out.PortfolioDividendEntitlementPort;
import com.smahjoub.stockute.domain.model.PortfolioDividendEntitlement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PortfolioDividendStatsServiceTest {

    @Mock
    private PortfolioDividendEntitlementPort portfolioDividendEntitlementPort;

    @Mock
    private DividendGrowthRateService dividendGrowthRateService;

    @InjectMocks
    private PortfolioDividendStatsService portfolioDividendStatsService;

    @Test
    void getDividendStatsForPortfolio_ReturnsCalculatedStats() {
        PortfolioDividendEntitlement entitlement1 = new PortfolioDividendEntitlement();
        entitlement1.setGrossAmount(BigDecimal.valueOf(10));

        PortfolioDividendEntitlement entitlement2 = new PortfolioDividendEntitlement();
        entitlement2.setGrossAmount(BigDecimal.valueOf(15));

        PortfolioDividendEntitlement entitlement3 = new PortfolioDividendEntitlement();
        entitlement3.setGrossAmount(BigDecimal.valueOf(100));

        PortfolioDividendEntitlement entitlement4 = new PortfolioDividendEntitlement();
        entitlement4.setGrossAmount(BigDecimal.valueOf(140));

        when(portfolioDividendEntitlementPort.findAllByPortfolioRefIdAndPaymentDateBetween(
                any(Long.class),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        ))
                .thenReturn(Flux.just(entitlement1, entitlement2))
                .thenReturn(Flux.just(entitlement3, entitlement4));

        when(dividendGrowthRateService.getGrowthRateForPortfolio(1L))
                .thenReturn(Mono.just(new BigDecimal("0.02")));

        StepVerifier.create(portfolioDividendStatsService.getDividendStatsForPortfolio(1L))
                .assertNext(stats -> {
                    org.junit.jupiter.api.Assertions.assertEquals(
                            0, stats.getPastMonthAccumulated().compareTo(new BigDecimal("25"))
                    );
                    org.junit.jupiter.api.Assertions.assertEquals(
                            0, stats.getPastYearAccumulated().compareTo(new BigDecimal("240"))
                    );
                    org.junit.jupiter.api.Assertions.assertEquals(
                            0, stats.getIncomingDividend().compareTo(BigDecimal.ZERO)
                    );
                    org.junit.jupiter.api.Assertions.assertEquals(
                            0, stats.getNextYearForecast().compareTo(new BigDecimal("244.80"))
                    );
                })
                .verifyComplete();

        verify(portfolioDividendEntitlementPort, times(2))
                .findAllByPortfolioRefIdAndPaymentDateBetween(
                        any(Long.class),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                );

        verify(dividendGrowthRateService).getGrowthRateForPortfolio(1L);
    }

    @Test
    void getDividendStatsForPortfolio_WhenNoEntitlements_ReturnsZerosExceptForecastBasedOnZero() {
        when(portfolioDividendEntitlementPort.findAllByPortfolioRefIdAndPaymentDateBetween(
                any(Long.class),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        ))
                .thenReturn(Flux.empty())
                .thenReturn(Flux.empty());

        when(dividendGrowthRateService.getGrowthRateForPortfolio(1L))
                .thenReturn(Mono.just(new BigDecimal("0.02")));

        StepVerifier.create(portfolioDividendStatsService.getDividendStatsForPortfolio(1L))
                .assertNext(stats -> {
                    org.junit.jupiter.api.Assertions.assertEquals(
                            0, stats.getPastMonthAccumulated().compareTo(BigDecimal.ZERO)
                    );
                    org.junit.jupiter.api.Assertions.assertEquals(
                            0, stats.getPastYearAccumulated().compareTo(BigDecimal.ZERO)
                    );
                    org.junit.jupiter.api.Assertions.assertEquals(
                            0, stats.getIncomingDividend().compareTo(BigDecimal.ZERO)
                    );
                    org.junit.jupiter.api.Assertions.assertEquals(
                            0, stats.getNextYearForecast().compareTo(BigDecimal.ZERO)
                    );
                })
                .verifyComplete();
    }

    @Test
    void getDividendStatsForPortfolio_WhenGrossAmountIsNull_TreatsItAsZero() {
        PortfolioDividendEntitlement entitlement1 = new PortfolioDividendEntitlement();
        entitlement1.setGrossAmount(null);

        PortfolioDividendEntitlement entitlement2 = new PortfolioDividendEntitlement();
        entitlement2.setGrossAmount(BigDecimal.valueOf(20));

        when(portfolioDividendEntitlementPort.findAllByPortfolioRefIdAndPaymentDateBetween(
                any(Long.class),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        ))
                .thenReturn(Flux.just(entitlement1, entitlement2))
                .thenReturn(Flux.just(entitlement1, entitlement2));

        when(dividendGrowthRateService.getGrowthRateForPortfolio(1L))
                .thenReturn(Mono.just(new BigDecimal("0.02")));

        StepVerifier.create(portfolioDividendStatsService.getDividendStatsForPortfolio(1L))
                .assertNext(stats -> {
                    org.junit.jupiter.api.Assertions.assertEquals(
                            0, stats.getPastMonthAccumulated().compareTo(new BigDecimal("20"))
                    );
                    org.junit.jupiter.api.Assertions.assertEquals(
                            0, stats.getPastYearAccumulated().compareTo(new BigDecimal("20"))
                    );
                    org.junit.jupiter.api.Assertions.assertEquals(
                            0, stats.getIncomingDividend().compareTo(BigDecimal.ZERO)
                    );
                    org.junit.jupiter.api.Assertions.assertEquals(
                            0, stats.getNextYearForecast().compareTo(new BigDecimal("20.40"))
                    );
                })
                .verifyComplete();
    }

    @Test
    void getDividendStatsForPortfolio_WhenGrowthRateServiceFails_ReturnsError() {
        when(portfolioDividendEntitlementPort.findAllByPortfolioRefIdAndPaymentDateBetween(
                any(Long.class),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        ))
                .thenReturn(Flux.empty())
                .thenReturn(Flux.empty());

        when(dividendGrowthRateService.getGrowthRateForPortfolio(1L))
                .thenReturn(Mono.error(new RuntimeException("Growth rate error")));

        StepVerifier.create(portfolioDividendStatsService.getDividendStatsForPortfolio(1L))
                .expectError(RuntimeException.class)
                .verify();
    }
}