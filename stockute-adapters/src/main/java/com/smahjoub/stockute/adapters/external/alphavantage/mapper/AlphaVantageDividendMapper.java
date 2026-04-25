package com.smahjoub.stockute.adapters.external.alphavantage.mapper;

import com.smahjoub.stockute.adapters.external.alphavantage.dto.AlphaVantageDividendItem;
import com.smahjoub.stockute.application.port.dividend.out.DividendHistoryItem;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface AlphaVantageDividendMapper {

    @Mapping(target = "exDate", expression = "java(toStartOfDay(item.getExDividendDate()))")
    @Mapping(target = "declarationDate", expression = "java(toStartOfDay(item.getDeclarationDate()))")
    @Mapping(target = "recordDate", expression = "java(toStartOfDay(item.getRecordDate()))")
    @Mapping(target = "paymentDate", expression = "java(toStartOfDay(item.getPaymentDate()))")
    @Mapping(target = "amount", expression = "java(new java.math.BigDecimal(item.getAmount()))")
    DividendHistoryItem toDividendHistoryItem(AlphaVantageDividendItem item);

    // Helper method MapStruct can call
    default LocalDateTime toStartOfDay(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        return Try.of(() -> LocalDate.parse(date).atStartOfDay())
                .getOrNull();
    }
}