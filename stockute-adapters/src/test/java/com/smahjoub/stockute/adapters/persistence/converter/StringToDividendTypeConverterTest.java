package com.smahjoub.stockute.adapters.persistence.converter;

import com.smahjoub.stockute.domain.model.enums.DividendType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StringToDividendTypeConverterTest {

    private final StringToDividendTypeConverter converter =
            new StringToDividendTypeConverter();

    @Test
    void convert_shouldReturnEnumValue_whenValidString() {
        assertThat(converter.convert("REGULAR"))
                .isEqualTo(DividendType.REGULAR);

        assertThat(converter.convert("special"))
                .isEqualTo(DividendType.SPECIAL);

        assertThat(converter.convert("Interim"))
                .isEqualTo(DividendType.INTERIM);

        assertThat(converter.convert("final"))
                .isEqualTo(DividendType.FINAL);
    }

    @Test
    void convert_shouldThrowException_whenInvalidValue() {
        assertThatThrownBy(() -> converter.convert("UNKNOWN"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void convert_shouldThrowException_whenNullValue() {
        // Your converter does NOT handle null → valueOf(null) throws NPE
        assertThatThrownBy(() -> converter.convert(null))
                .isInstanceOf(NullPointerException.class);
    }
}
