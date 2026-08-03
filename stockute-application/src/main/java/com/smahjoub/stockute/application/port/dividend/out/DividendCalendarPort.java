package com.smahjoub.stockute.application.port.dividend.out;


import reactor.core.publisher.Flux;

public interface DividendCalendarPort {
    Flux<SecurityDividendCalendarItem> getUpcomingDividends();
}