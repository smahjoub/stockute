package com.smahjoub.stockute.adapters.scheduler;

import com.smahjoub.stockute.application.service.dividend.SecurityDividendImportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

class SecurityDividendImportSchedulerTest {

    private SecurityDividendImportService service;
    private SecurityDividendImportScheduler scheduler;

    @BeforeEach
    void setup() {
        service = mock(SecurityDividendImportService.class);
        when(service.importDividendHistoryForAllSecurities())
                .thenReturn(Mono.empty());

        scheduler = new SecurityDividendImportScheduler(service);
    }

    @Test
    void importDividendHistory_shouldRunWhenNotAlreadyRunning() {
        scheduler.importDividendHistory();

        verify(service, times(1)).importDividendHistoryForAllSecurities();
    }

    @Test
    void importDividendHistory_shouldSkipWhenAlreadyRunning() {
        when(service.importDividendHistoryForAllSecurities())
                .thenReturn(Mono.never());

        scheduler.importDividendHistory(); // running = true
        scheduler.importDividendHistory(); // should be skipped

        verify(service, times(1)).importDividendHistoryForAllSecurities();
    }


    @Test
    void importDividendHistory_shouldResetRunningFlagAfterCompletion() {
        scheduler.importDividendHistory();
        scheduler.importDividendHistory();
        verify(service, times(2)).importDividendHistoryForAllSecurities();
    }
}
