package com.smahjoub.stockute.adapters.restful.dividend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DividendCalendarItemDto(
        String name,
        String symbol,
        LocalDate dividendExDate,
        LocalDate paymentDate,
        BigDecimal dividendRate
) {}
