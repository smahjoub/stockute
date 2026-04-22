package com.smahjoub.stockute.adapters.persistence.converter;

import com.smahjoub.stockute.domain.model.enums.DividendStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class DividendStatusToStringConverter implements Converter<DividendStatus, String> {

    @Override
    public String convert(DividendStatus source) {
        return source == null ? null : source.name();
    }
}