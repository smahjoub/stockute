package com.smahjoub.stockute.adapters.persistence.converter;

import com.smahjoub.stockute.domain.model.enums.DividendStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class StringToDividendStatusConverter implements Converter<String, DividendStatus> {

    @Override
    public DividendStatus convert(String source) {
        return source == null ? null : DividendStatus.valueOf(source.toUpperCase());
    }
}