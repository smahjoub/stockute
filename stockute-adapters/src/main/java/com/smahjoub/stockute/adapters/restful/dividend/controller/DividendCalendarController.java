package com.smahjoub.stockute.adapters.restful.dividend.controller;

import com.smahjoub.stockute.adapters.restful.dividend.dto.DividendCalendarItemDto;
import com.smahjoub.stockute.adapters.restful.dividend.mapper.DividendCalendarMapper;
import com.smahjoub.stockute.application.port.dividend.in.DividendCalendarUseCase;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/dividends")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
@AllArgsConstructor
public class DividendCalendarController {

    private final DividendCalendarUseCase dividendCalendarUseCase;
    private final DividendCalendarMapper dividendCalendarMapper;

    @GetMapping("/calendar")
    public Flux<DividendCalendarItemDto> getDividendCalendar() {
        return dividendCalendarUseCase.getUpcomingDividends()
                .map(dividendCalendarMapper::toDto);
    }
}
