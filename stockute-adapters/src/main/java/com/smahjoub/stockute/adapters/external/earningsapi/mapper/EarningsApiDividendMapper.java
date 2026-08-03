package com.smahjoub.stockute.adapters.external.earningsapi.mapper;

import com.smahjoub.stockute.adapters.external.earningsapi.dto.EarningsApiDividendResponse;
import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendCalendarItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EarningsApiDividendMapper {

    public SecurityDividendCalendarItem toDomain(EarningsApiDividendResponse response) {
        return new SecurityDividendCalendarItem(
                response.getName(),
                response.getSymbol(),
                response.getDividendExDate() != null ? LocalDate.parse(response.getDividendExDate()) : null,
                response.getPaymentDate() != null ? LocalDate.parse(response.getPaymentDate()) : null,
                response.getDividendRate()
        );
    }
}