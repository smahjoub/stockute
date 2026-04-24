package com.smahjoub.stockute.adapters.persistence.converter;

import com.smahjoub.stockute.domain.model.enums.DividendStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DividendStatusToStringConverterTest {

    private final DividendStatusToStringConverter converter =
            new DividendStatusToStringConverter();

    @Test
    void convert_shouldReturnEnumName() {
        assertThat(converter.convert(DividendStatus.ANNOUNCED))
                .isEqualTo("ANNOUNCED");

        assertThat(converter.convert(DividendStatus.CONFIRMED))
                .isEqualTo("CONFIRMED");

        assertThat(converter.convert(DividendStatus.PAID))
                .isEqualTo("PAID");

        assertThat(converter.convert(DividendStatus.CANCELLED))
                .isEqualTo("CANCELLED");
    }

    @Test
    void convert_shouldReturnNullWhenSourceIsNull() {
        assertThat(converter.convert(null)).isNull();
    }
}
