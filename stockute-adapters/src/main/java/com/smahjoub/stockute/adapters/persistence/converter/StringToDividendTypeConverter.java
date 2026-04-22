package com.smahjoub.stockute.adapters.persistence.converter;

import com.smahjoub.stockute.domain.model.enums.DividendType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class StringToDividendTypeConverter implements Converter<String, DividendType> {

    @Override
    public DividendType convert(String source) {
        return DividendType.valueOf(source.toUpperCase());
    }
}