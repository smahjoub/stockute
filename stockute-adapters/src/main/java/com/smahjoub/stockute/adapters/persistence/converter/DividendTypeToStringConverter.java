package com.smahjoub.stockute.adapters.persistence.converter;

import com.smahjoub.stockute.domain.model.enums.DividendType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class DividendTypeToStringConverter implements Converter<DividendType, String> {

    @Override
    public String convert(DividendType source) {
        return source == null ? null : source.name();
    }
}