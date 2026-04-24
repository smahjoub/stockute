package com.smahjoub.stockute.adapters.persistence.converter;


import com.smahjoub.stockute.domain.model.enums.DividendType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DividendTypeToStringConverterTest {

    private final DividendTypeToStringConverter converter =
            new DividendTypeToStringConverter();

    @Test
    void convert_shouldReturnEnumName() {
        assertThat(converter.convert(DividendType.REGULAR))
                .isEqualTo("REGULAR");

        assertThat(converter.convert(DividendType.SPECIAL))
                .isEqualTo("SPECIAL");

        assertThat(converter.convert(DividendType.INTERIM))
                .isEqualTo("INTERIM");

        assertThat(converter.convert(DividendType.FINAL))
                .isEqualTo("FINAL");
    }

    @Test
    void convert_shouldReturnNullWhenSourceIsNull() {
        assertThat(converter.convert(null)).isNull();
    }
}
