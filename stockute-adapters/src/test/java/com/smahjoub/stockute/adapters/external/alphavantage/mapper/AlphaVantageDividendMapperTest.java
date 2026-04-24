package com.smahjoub.stockute.adapters.external.alphavantage.mapper;

import com.smahjoub.stockute.adapters.external.alphavantage.dto.AlphaVantageDividendItem;
import com.smahjoub.stockute.application.port.dividend.out.DividendHistoryItem;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AlphaVantageDividendMapperTest {

    private final AlphaVantageDividendMapper mapper =
            Mappers.getMapper(AlphaVantageDividendMapper.class);

    @Test
    void toDividendHistoryItem_shouldMapAllFieldsCorrectly() {
        AlphaVantageDividendItem item = new AlphaVantageDividendItem();
        item.setExDividendDate("2026-05-08");
        item.setDeclarationDate("2026-04-22");
        item.setRecordDate("2026-05-08");
        item.setPaymentDate("2026-06-10");
        item.setAmount("1.69");

        DividendHistoryItem result = mapper.toDividendHistoryItem(item);

        assertThat(result).isNotNull();
        assertThat(result.exDate()).isEqualTo(LocalDateTime.of(2026, 5, 8, 0, 0));
        assertThat(result.declarationDate()).isEqualTo(LocalDateTime.of(2026, 4, 22, 0, 0));
        assertThat(result.recordDate()).isEqualTo(LocalDateTime.of(2026, 5, 8, 0, 0));
        assertThat(result.paymentDate()).isEqualTo(LocalDateTime.of(2026, 6, 10, 0, 0));
        assertThat(result.amount()).isEqualByComparingTo(new BigDecimal("1.69"));
    }

    @Test
    void toDividendHistoryItem_shouldHandleNullDates() {
        AlphaVantageDividendItem item = new AlphaVantageDividendItem();
        item.setExDividendDate(null);
        item.setDeclarationDate("");
        item.setRecordDate(" ");
        item.setPaymentDate(null);
        item.setAmount("2.50");

        DividendHistoryItem result = mapper.toDividendHistoryItem(item);

        assertThat(result.exDate()).isNull();
        assertThat(result.declarationDate()).isNull();
        assertThat(result.recordDate()).isNull();
        assertThat(result.paymentDate()).isNull();
        assertThat(result.amount()).isEqualByComparingTo(new BigDecimal("2.50"));
    }
}
