package com.smahjoub.stockute.adapters.external.earningsapi.mapper;

import com.smahjoub.stockute.adapters.external.earningsapi.dto.EarningsApiDividendResponse;
import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendCalendarItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class EarningsApiDividendMapperTest {

    private final EarningsApiDividendMapper mapper = new EarningsApiDividendMapper();

    @Test
    void toDomain_shouldMapAllFieldsCorrectly() {
        EarningsApiDividendResponse response = new EarningsApiDividendResponse();
        response.setName("Apple Inc.");
        response.setSymbol("AAPL");
        response.setDividendExDate("2026-08-10");
        response.setPaymentDate("2026-08-20");
        response.setDividendRate(BigDecimal.valueOf(0.24));

        SecurityDividendCalendarItem result = mapper.toDomain(response);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Apple Inc.");
        assertThat(result.symbol()).isEqualTo("AAPL");
        assertThat(result.dividendExDate()).isEqualTo(LocalDate.of(2026, 8, 10));
        assertThat(result.paymentDate()).isEqualTo(LocalDate.of(2026, 8, 20));
        assertThat(result.dividendRate()).isEqualByComparingTo(BigDecimal.valueOf(0.24));
    }

    @Test
    void toDomain_shouldReturnNullDates_whenDatesAreNullOrBlank() {
        EarningsApiDividendResponse response = new EarningsApiDividendResponse();
        response.setName("Microsoft Corp.");
        response.setSymbol("MSFT");
        response.setDividendExDate(null);
        response.setPaymentDate("   ");
        response.setDividendRate(BigDecimal.valueOf(0.68));

        SecurityDividendCalendarItem result = mapper.toDomain(response);

        assertThat(result.dividendExDate()).isNull();
        assertThat(result.paymentDate()).isNull();
    }

    @Test
    void toDomain_shouldReturnNullDate_whenDateIsInvalidFormat() {
        EarningsApiDividendResponse response = new EarningsApiDividendResponse();
        response.setName("Tesla Inc.");
        response.setSymbol("TSLA");
        response.setDividendExDate("not-a-date");
        response.setPaymentDate("2026-08-20");
        response.setDividendRate(BigDecimal.valueOf(0.10));

        SecurityDividendCalendarItem result = mapper.toDomain(response);

        assertThat(result.dividendExDate()).isNull();
        assertThat(result.paymentDate()).isEqualTo(LocalDate.of(2026, 8, 20));
    }
}
