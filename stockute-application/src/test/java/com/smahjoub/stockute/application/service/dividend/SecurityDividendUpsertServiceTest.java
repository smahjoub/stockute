package com.smahjoub.stockute.application.service.dividend;

import com.smahjoub.stockute.application.port.dividend.out.DividendHistoryItem;
import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendPort;
import com.smahjoub.stockute.domain.model.Security;
import com.smahjoub.stockute.domain.model.SecurityDividend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SecurityDividendUpsertServiceTest {

    private SecurityDividendPort port;
    private SecurityDividendUpsertService service;

    @BeforeEach
    void setup() {
        port = mock(SecurityDividendPort.class);
        service = new SecurityDividendUpsertService(port);
    }

    @Test
    void upsert_whenExistingDividend_updatesAndSaves() {
        Security security = new Security();
        security.setId(1L);
        security.setCurrencyRefId(50L);

        DividendHistoryItem item = new DividendHistoryItem(
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                new BigDecimal("1.23")
        );

        SecurityDividend existing = new SecurityDividend();
        existing.setId(10L);

        when(port.findByBusinessKey(anyLong(), any(), any(), any()))
                .thenReturn(Mono.just(existing));

        when(port.save(existing)).thenReturn(Mono.just(existing));

        StepVerifier.create(service.upsert(security, item))
                .expectNext(existing)
                .verifyComplete();

        verify(port).findByBusinessKey(
                eq(1L),
                eq(item.exDate()),
                eq(item.paymentDate()),
                eq(item.amount())
        );

        verify(port).save(existing);
    }

    @Test
    void upsert_whenNoExistingDividend_createsAndSavesNew() {
        Security security = new Security();
        security.setId(1L);
        security.setCurrencyRefId(50L);

        DividendHistoryItem item = new DividendHistoryItem(
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                new BigDecimal("2.50")
        );

        when(port.findByBusinessKey(anyLong(), any(), any(), any()))
                .thenReturn(Mono.empty());

        SecurityDividend saved = new SecurityDividend();
        saved.setId(99L);

        when(port.save(any(SecurityDividend.class)))
                .thenReturn(Mono.just(saved));

        StepVerifier.create(service.upsert(security, item))
                .expectNext(saved)
                .verifyComplete();

        verify(port).findByBusinessKey(
                eq(1L),
                eq(item.exDate()),
                eq(item.paymentDate()),
                eq(item.amount())
        );

        verify(port).save(any(SecurityDividend.class));
    }
}
