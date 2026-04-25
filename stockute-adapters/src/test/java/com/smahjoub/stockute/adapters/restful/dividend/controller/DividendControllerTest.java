package com.smahjoub.stockute.adapters.restful.dividend.controller;

import com.smahjoub.stockute.adapters.restful.dividend.dto.DividendEntitlementDto;
import com.smahjoub.stockute.adapters.restful.dividend.mapper.DividendEntitlementMapper;
import com.smahjoub.stockute.application.port.dividend.in.PortfolioDividendEntitlementUseCase;
import com.smahjoub.stockute.domain.model.PortfolioDividendEntitlement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DividendControllerTest {

    @Mock
    private DividendEntitlementMapper dividendEntitlementMapper;

    @Mock
    private PortfolioDividendEntitlementUseCase portfolioDividendEntitlementUseCase;

    @InjectMocks
    private DividendController dividendController;

    private PortfolioDividendEntitlement entitlement1;
    private PortfolioDividendEntitlement entitlement2;
    private DividendEntitlementDto dto1;
    private DividendEntitlementDto dto2;

    @BeforeEach
    void setUp() {
        entitlement1 = new PortfolioDividendEntitlement();
        entitlement1.setId(1L);
        entitlement1.setEligibleShares(BigDecimal.valueOf(10));
        entitlement1.setDividendPerShare(BigDecimal.valueOf(2));
        entitlement1.setGrossAmount(BigDecimal.valueOf(20));
        entitlement1.setCurrencyRefId(1L);
        entitlement1.setExDate(LocalDateTime.now());
        entitlement1.setPaymentDate(LocalDateTime.now());
        entitlement1.setStatus("PAID");

        entitlement2 = new PortfolioDividendEntitlement();
        entitlement2.setId(2L);
        entitlement2.setEligibleShares(BigDecimal.valueOf(5));
        entitlement2.setDividendPerShare(BigDecimal.valueOf(3));
        entitlement2.setGrossAmount(BigDecimal.valueOf(15));
        entitlement2.setCurrencyRefId(1L);
        entitlement2.setExDate(LocalDateTime.now());
        entitlement2.setPaymentDate(LocalDateTime.now());
        entitlement2.setStatus("PENDING");

        dto1 = new DividendEntitlementDto(
                1L,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(2),
                BigDecimal.valueOf(20),
                1L,
                entitlement1.getExDate(),
                entitlement1.getPaymentDate(),
                "PAID"
        );

        dto2 = new DividendEntitlementDto(
                2L,
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(3),
                BigDecimal.valueOf(15),
                1L,
                entitlement2.getExDate(),
                entitlement2.getPaymentDate(),
                "PENDING"
        );
    }

    @Test
    void getDividendEntitlementsForPortfolio_Success_ReturnsDtos() {
        when(portfolioDividendEntitlementUseCase.getDividendEntitlementsForPortfolio(1L, 100L))
                .thenReturn(Flux.just(entitlement1, entitlement2));

        when(dividendEntitlementMapper.toDto(entitlement1)).thenReturn(dto1);
        when(dividendEntitlementMapper.toDto(entitlement2)).thenReturn(dto2);

        Flux<DividendEntitlementDto> result =
                dividendController.getDividendEntitlementsForPortfolio(1L, 100L);

        StepVerifier.create(result)
                .expectNext(dto1, dto2)
                .verifyComplete();

        verify(portfolioDividendEntitlementUseCase)
                .getDividendEntitlementsForPortfolio(1L, 100L);
    }

    @Test
    void getDividendEntitlementsForPortfolio_Empty_ReturnsEmpty() {
        when(portfolioDividendEntitlementUseCase.getDividendEntitlementsForPortfolio(anyLong(), anyLong()))
                .thenReturn(Flux.empty());

        StepVerifier.create(
                dividendController.getDividendEntitlementsForPortfolio(1L, 100L)
        ).verifyComplete();
    }

    @Test
    void getDividendEntitlementsForPortfolio_ServiceError_ReturnsError() {
        when(portfolioDividendEntitlementUseCase.getDividendEntitlementsForPortfolio(anyLong(), anyLong()))
                .thenReturn(Flux.error(new RuntimeException("Service error")));

        StepVerifier.create(
                        dividendController.getDividendEntitlementsForPortfolio(1L, 100L)
                )
                .expectError(RuntimeException.class)
                .verify();
    }
}
