package com.smahjoub.stockute.adapters.restful.asset.dto;

import java.math.BigDecimal;

public record AssetDTO(

    Long id,
    String ticker,
    String exchange,
    String name,
    double quantity,
    BigDecimal averagePrice,
    Long currencyRefId,
    String currency

) {
}