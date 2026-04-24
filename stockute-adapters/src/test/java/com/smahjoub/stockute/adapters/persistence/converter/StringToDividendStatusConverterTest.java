package com.smahjoub.stockute.adapters.persistence.converter;

import com.smahjoub.stockute.domain.model.enums.DividendStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StringToDividendStatusConverterTest {

    private final StringToDividendStatusConverter converter =
            new StringToDividendStatusConverter();

    @Test
    void convert_shouldReturnEnumValue_whenValidString() {
        assertThat(converter.convert("ANNOUNCED"))
                .isEqualTo(DividendStatus.ANNOUNCED);

        assertThat(converter.convert("confirmed"))
                .isEqualTo(DividendStatus.CONFIRMED);

        assertThat(converter.convert("PaId"))
                .isEqualTo(DividendStatus.PAID);

        assertThat(converter.convert("cancelled"))
                .isEqualTo(DividendStatus.CANCELLED);
    }

    @Test
    void convert_shouldReturnNull_whenSourceIsNull() {
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    void convert_shouldThrowException_whenInvalidValue() {
        assertThatThrownBy(() -> converter.convert("INVALID_STATUS"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
