package com.smahjoub.stockute.application.service.dividend;

import com.smahjoub.stockute.application.port.dividend.out.DividendCalendarPort;
import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendCalendarItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DividendCalendarServiceTest {

    @Mock
    private DividendCalendarPort dividendCalendarPort;

    @InjectMocks
    private DividendCalendarService dividendCalendarService;

    @Test
    void getUpcomingDividends_returnsPortFlux() {
        SecurityDividendCalendarItem item1 = new SecurityDividendCalendarItem(
                "Apple Inc.",
                "AAPL",
                LocalDate.of(2026, 8, 10),
                LocalDate.of(2026, 8, 20),
                BigDecimal.valueOf(0.24)
        );

        SecurityDividendCalendarItem item2 = new SecurityDividendCalendarItem(
                "Microsoft Corp.",
                "MSFT",
                LocalDate.of(2026, 8, 12),
                LocalDate.of(2026, 8, 22),
                BigDecimal.valueOf(0.68)
        );

        when(dividendCalendarPort.getUpcomingDividends()).thenReturn(Flux.just(item1, item2));

        StepVerifier.create(dividendCalendarService.getUpcomingDividends())
                .expectNext(item1)
                .expectNext(item2)
                .verifyComplete();

        verify(dividendCalendarPort).getUpcomingDividends();
    }

    @Test
    void getUpcomingDividends_emptyFlux_returnsEmptyFlux() {
        when(dividendCalendarPort.getUpcomingDividends()).thenReturn(Flux.empty());

        StepVerifier.create(dividendCalendarService.getUpcomingDividends())
                .verifyComplete();

        verify(dividendCalendarPort).getUpcomingDividends();
    }

    @Test
    void getUpcomingDividends_portError_propagatesError() {
        RuntimeException exception = new RuntimeException("Port error");

        when(dividendCalendarPort.getUpcomingDividends()).thenReturn(Flux.error(exception));

        StepVerifier.create(dividendCalendarService.getUpcomingDividends())
                .expectErrorMatches(throwable -> throwable == exception)
                .verify();

        verify(dividendCalendarPort).getUpcomingDividends();
    }
}
