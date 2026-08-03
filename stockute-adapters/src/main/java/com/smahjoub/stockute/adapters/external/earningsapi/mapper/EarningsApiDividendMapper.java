package com.smahjoub.stockute.adapters.external.earningsapi.mapper;

import com.smahjoub.stockute.adapters.external.earningsapi.dto.EarningsApiDividendResponse;
import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendCalendarItem;
import org.springframework.stereotype.Component;

import io.vavr.control.Try;

import java.time.LocalDate;

@Component
public class EarningsApiDividendMapper {

    public SecurityDividendCalendarItem toDomain(EarningsApiDividendResponse response) {
        return new SecurityDividendCalendarItem(
                response.getName(),
                response.getSymbol(),
                parseDate(response.getDividendExDate()),
                parseDate(response.getPaymentDate()),
                response.getDividendRate()
        );
    }

    private LocalDate parseDate(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        return Try.of(() -> LocalDate.parse(date)).getOrNull();
    }
}