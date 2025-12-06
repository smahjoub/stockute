package com.smahjoub.stockute.adapters.restful.transaction.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDTO(
        Long id,
        Long portfolioRefId,
        Long assetRefId,
        String type,
        double quantity,
        BigDecimal price,
        BigDecimal fees,
        LocalDateTime transactionDate,
        String notes
) {
}
