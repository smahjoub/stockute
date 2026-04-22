package com.smahjoub.stockute.application.service.dividend;

import com.smahjoub.stockute.application.port.asset.out.AssetPort;
import com.smahjoub.stockute.application.port.dividend.out.PortfolioDividendEntitlementPort;
import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendPort;
import com.smahjoub.stockute.application.port.transaction.out.TransactionPort;
import com.smahjoub.stockute.domain.model.Asset;
import com.smahjoub.stockute.domain.model.PortfolioDividendEntitlement;
import com.smahjoub.stockute.domain.model.SecurityDividend;
import com.smahjoub.stockute.domain.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioDividendEntitlementServiceTest {

    @Mock
    private SecurityDividendPort securityDividendPort;

    @Mock
    private PortfolioDividendEntitlementPort portfolioDividendEntitlementPort;

    @Mock
    private AssetPort assetPort;

    @Mock
    private TransactionPort transactionPort;

    @InjectMocks
    private PortfolioDividendEntitlementService service;

    @Test
    void rebuildEntitlementsForSecurityDividend_generatesEntitlementUsingHistoricalQuantity() {
        SecurityDividend dividend = new SecurityDividend();
        dividend.setId(10L);
        dividend.setSecurityRefId(200L);
        dividend.setDividendPerShare(BigDecimal.valueOf(1.50));
        dividend.setCurrencyRefId(1L);
        dividend.setExDate(LocalDateTime.of(2026, 4, 1, 0, 0));
        dividend.setPaymentDate(LocalDateTime.of(2026, 4, 15, 0, 0));

        Asset asset = new Asset();
        asset.setId(100L);
        asset.setPortfolioRefId(1L);
        asset.setSecurityRefId(200L);

        Transaction buy = new Transaction();
        buy.setType("BUY");
        buy.setQuantity(BigDecimal.valueOf(10));
        buy.setTransactionDate(LocalDateTime.of(2026, 3, 1, 0, 0));

        Transaction sell = new Transaction();
        sell.setType("SELL");
        sell.setQuantity(BigDecimal.valueOf(3));
        sell.setTransactionDate(LocalDateTime.of(2026, 3, 15, 0, 0));

        when(securityDividendPort.findById(10L)).thenReturn(Mono.just(dividend));
        when(portfolioDividendEntitlementPort.deleteBySecurityDividendRefId(10L)).thenReturn(Mono.empty());
        when(assetPort.findAllBySecurityRefId(200L)).thenReturn(Flux.just(asset));
        when(transactionPort.findAllByAssetRefIdAndTransactionDateLessThanEqual(100L, dividend.getExDate()))
                .thenReturn(Flux.just(buy, sell));
        when(portfolioDividendEntitlementPort.saveAll(any(Publisher.class)))
                .thenAnswer(invocation -> Flux.from(invocation.getArgument(0)));

        StepVerifier.create(service.rebuildEntitlementsForSecurityDividend(10L))
                .verifyComplete();

        verify(securityDividendPort).findById(10L);
        verify(portfolioDividendEntitlementPort).deleteBySecurityDividendRefId(10L);
        verify(assetPort).findAllBySecurityRefId(200L);
        verify(transactionPort).findAllByAssetRefIdAndTransactionDateLessThanEqual(100L, dividend.getExDate());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Publisher<PortfolioDividendEntitlement>> captor =
                ArgumentCaptor.forClass((Class) Publisher.class);

        verify(portfolioDividendEntitlementPort).saveAll(captor.capture());

        List<PortfolioDividendEntitlement> entitlements =
                Flux.from(captor.getValue()).collectList().block();

        assertNotNull(entitlements);
        assertEquals(1, entitlements.size());

        PortfolioDividendEntitlement entitlement = entitlements.get(0);
        assertEquals(1L, entitlement.getPortfolioRefId());
        assertEquals(100L, entitlement.getAssetRefId());
        assertEquals(10L, entitlement.getSecurityDividendRefId());
        assertEquals(200L, entitlement.getSecurityRefId());
        assertEquals(0, entitlement.getEligibleShares().compareTo(BigDecimal.valueOf(7)));
        assertEquals(0, entitlement.getDividendPerShare().compareTo(BigDecimal.valueOf(1.50)));
        assertEquals(0, entitlement.getGrossAmount().compareTo(BigDecimal.valueOf(10.50)));
        assertEquals("CALCULATED", entitlement.getStatus());
    }

    @Test
    void rebuildEntitlementsForSecurityDividend_whenDividendNotFound_completesWithoutDoingAnything() {
        when(securityDividendPort.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(service.rebuildEntitlementsForSecurityDividend(999L))
                .verifyComplete();

        verify(securityDividendPort).findById(999L);
        verifyNoInteractions(portfolioDividendEntitlementPort, assetPort, transactionPort);
    }
}