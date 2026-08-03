package com.smahjoub.stockute.application.port.dividend.in;

import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendCalendarItem;
import reactor.core.publisher.Flux;

public interface DividendCalendarUseCase {
    Flux<SecurityDividendCalendarItem> getUpcomingDividends();
}
