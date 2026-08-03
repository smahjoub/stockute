package com.smahjoub.stockute.application.service.dividend;

import com.smahjoub.stockute.application.port.dividend.in.DividendCalendarUseCase;
import com.smahjoub.stockute.application.port.dividend.out.DividendCalendarPort;
import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendCalendarItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class DividendCalendarService implements DividendCalendarUseCase {

    private final DividendCalendarPort dividendCalendarPort;

    @Override
    public Flux<SecurityDividendCalendarItem> getUpcomingDividends() {
        return dividendCalendarPort.getUpcomingDividends();
    }
}
