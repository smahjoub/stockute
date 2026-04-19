package com.smahjoub.stockute.adapters.restful.asset.dto;


import java.math.BigDecimal;

public record AssetDTO(
        Long id,
        String ticker,
        String region,
        String name,
        double quantity,
        BigDecimal averagePrice,
        Long currencyRefId,
        String currency,
        BigDecimal accumulatedFees,
        BigDecimal accumulatedDividends,
        BigDecimal totalGainLoss,
        BigDecimal totalAmountInvested
) {
}