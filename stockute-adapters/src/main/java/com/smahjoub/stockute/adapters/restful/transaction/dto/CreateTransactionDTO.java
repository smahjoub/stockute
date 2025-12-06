package com.smahjoub.stockute.adapters.restful.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateTransactionDTO(
        String assetName,
        String ticker,
        String exchange,
        double quantity,
        Long currencyRefId,
        BigDecimal price,
        BigDecimal fees,
        String notes,
        String type,
        LocalDateTime transactionDate
) {
}