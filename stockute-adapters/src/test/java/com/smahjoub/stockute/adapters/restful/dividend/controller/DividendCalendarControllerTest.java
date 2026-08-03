package com.smahjoub.stockute.adapters.restful.dividend.controller;

import com.smahjoub.stockute.adapters.restful.dividend.dto.DividendCalendarItemDto;
import com.smahjoub.stockute.adapters.restful.dividend.mapper.DividendCalendarMapper;
import com.smahjoub.stockute.application.port.dividend.in.DividendCalendarUseCase;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DividendCalendarControllerTest {

    @Mock
    private DividendCalendarUseCase dividendCalendarUseCase;

    @Mock
    private DividendCalendarMapper dividendCalendarMapper;

    @InjectMocks
    private DividendCalendarController controller;

    @Test
    void getDividendCalendar_returnsDividendCalendarItemDtoFlux() {
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

        DividendCalendarItemDto dto1 = new DividendCalendarItemDto(
                "Apple Inc.",
                "AAPL",
                LocalDate.of(2026, 8, 10),
                LocalDate.of(2026, 8, 20),
                BigDecimal.valueOf(0.24)
        );

        DividendCalendarItemDto dto2 = new DividendCalendarItemDto(
                "Microsoft Corp.",
                "MSFT",
                LocalDate.of(2026, 8, 12),
                LocalDate.of(2026, 8, 22),
                BigDecimal.valueOf(0.68)
        );

        when(dividendCalendarUseCase.getUpcomingDividends()).thenReturn(Flux.just(item1, item2));
        when(dividendCalendarMapper.toDto(item1)).thenReturn(dto1);
        when(dividendCalendarMapper.toDto(item2)).thenReturn(dto2);

        StepVerifier.create(controller.getDividendCalendar())
                .expectNext(dto1)
                .expectNext(dto2)
                .verifyComplete();

        verify(dividendCalendarUseCase).getUpcomingDividends();
        verify(dividendCalendarMapper).toDto(item1);
        verify(dividendCalendarMapper).toDto(item2);
    }

    @Test
    void getDividendCalendar_emptyFlux_returnsEmptyFlux() {
        when(dividendCalendarUseCase.getUpcomingDividends()).thenReturn(Flux.empty());

        StepVerifier.create(controller.getDividendCalendar())
                .verifyComplete();

        verify(dividendCalendarUseCase).getUpcomingDividends();
        verifyNoInteractions(dividendCalendarMapper);
    }

    @Test
    void getDividendCalendar_serviceError_propagatesError() {
        RuntimeException exception = new RuntimeException("Service error");

        when(dividendCalendarUseCase.getUpcomingDividends()).thenReturn(Flux.error(exception));

        StepVerifier.create(controller.getDividendCalendar())
                .expectErrorMatches(throwable -> throwable == exception)
                .verify();

        verify(dividendCalendarUseCase).getUpcomingDividends();
        verifyNoInteractions(dividendCalendarMapper);
    }
}