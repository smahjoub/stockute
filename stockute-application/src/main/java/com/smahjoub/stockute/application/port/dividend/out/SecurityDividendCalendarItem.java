package com.smahjoub.stockute.application.port.dividend.out;


import java.math.BigDecimal;
import java.time.LocalDate;

public record SecurityDividendCalendarItem(
        String name,
        String symbol,
        LocalDate dividendExDate,
        LocalDate paymentDate,
        BigDecimal dividendRate
) {

}