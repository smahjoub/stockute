package com.smahjoub.stockute.adapters.restful.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateTransactionDTO(
        String assetName,
        double quantity,
        Long currencyRefId,
        Long securityRefId,
        BigDecimal price,
        BigDecimal fees,
        String notes,
        String type,
        LocalDateTime transactionDate
) {
}