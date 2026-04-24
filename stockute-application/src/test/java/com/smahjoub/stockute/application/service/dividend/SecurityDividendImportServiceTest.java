package com.smahjoub.stockute.application.service.dividend;

import com.smahjoub.stockute.application.port.dividend.out.DividendHistoryItem;
import com.smahjoub.stockute.application.port.dividend.out.DividendMarketDataPort;
import com.smahjoub.stockute.application.port.security.out.SecurityPort;
import com.smahjoub.stockute.domain.model.Security;
import com.smahjoub.stockute.domain.model.SecurityDividend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class SecurityDividendImportServiceTest {

    private SecurityPort securityPort;
    private DividendMarketDataPort dividendMarketDataPort;
    private SecurityDividendUpsertService upsertService;
    private PortfolioDividendEntitlementService entitlementService;

    private SecurityDividendImportService service;

    @BeforeEach
    void setup() {
        securityPort = mock(SecurityPort.class);
        dividendMarketDataPort = mock(DividendMarketDataPort.class);
        upsertService = mock(SecurityDividendUpsertService.class);
        entitlementService = mock(PortfolioDividendEntitlementService.class);

        service = new SecurityDividendImportService(
                securityPort,
                dividendMarketDataPort,
                upsertService,
                entitlementService
        );
    }

    @Test
    void importDividendHistoryForAllSecurities_shouldProcessEligibleSecurities() {
        Security s1 = new Security();
        s1.setId(1L);
        s1.setSymbol("AAPL");

        Security s2 = new Security();
        s2.setId(2L);
        s2.setSymbol("   ");

        Security s3 = new Security();
        s3.setId(3L);
        s3.setSymbol("MSFT");

        DividendHistoryItem item1 = new DividendHistoryItem(
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                new BigDecimal("1.23")
        );

        DividendHistoryItem item2 = new DividendHistoryItem(
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                new BigDecimal("0.88")
        );

        when(securityPort.findAll())
                .thenReturn(Flux.just(s1, s2, s3));

        when(dividendMarketDataPort.getDividendHistory("AAPL"))
                .thenReturn(Flux.just(item1));

        when(dividendMarketDataPort.getDividendHistory("MSFT"))
                .thenReturn(Flux.just(item2));

        SecurityDividend saved1 = new SecurityDividend();
        saved1.setId(10L);

        SecurityDividend saved2 = new SecurityDividend();
        saved2.setId(20L);

        when(upsertService.upsert(s1, item1))
                .thenReturn(Mono.just(saved1));

        when(upsertService.upsert(s3, item2))
                .thenReturn(Mono.just(saved2));

        when(entitlementService.rebuildEntitlementsForSecurityDividend(10L))
                .thenReturn(Mono.empty());

        when(entitlementService.rebuildEntitlementsForSecurityDividend(20L))
                .thenReturn(Mono.empty());

        StepVerifier.create(service.importDividendHistoryForAllSecurities())
                .verifyComplete();

        verify(securityPort).findAll();
        verify(dividendMarketDataPort).getDividendHistory("AAPL");
        verify(dividendMarketDataPort).getDividendHistory("MSFT");
        verify(upsertService).upsert(s1, item1);
        verify(upsertService).upsert(s3, item2);
        verify(entitlementService).rebuildEntitlementsForSecurityDividend(10L);
        verify(entitlementService).rebuildEntitlementsForSecurityDividend(20L);
        verifyNoInteractionsForIneligible();
    }

    @Test
    void importDividendHistoryForAllSecurities_shouldHandleErrorsAndContinue() {
        Security s1 = new Security();
        s1.setId(1L);
        s1.setSymbol("AAPL");

        when(securityPort.findAll())
                .thenReturn(Flux.just(s1));

        when(dividendMarketDataPort.getDividendHistory("AAPL"))
                .thenReturn(Flux.error(new RuntimeException("boom")));

        StepVerifier.create(service.importDividendHistoryForAllSecurities())
                .verifyComplete();

        verify(securityPort).findAll();
        verify(dividendMarketDataPort).getDividendHistory("AAPL");
    }

    private void verifyNoInteractionsForIneligible() {
        verify(upsertService, never()).upsert(
                ArgumentMatchers.argThat(sec -> sec.getId() == 2L),
                any()
        );
    }
}
