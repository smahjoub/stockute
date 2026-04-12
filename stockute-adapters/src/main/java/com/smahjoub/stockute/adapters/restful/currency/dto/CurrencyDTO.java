package com.smahjoub.stockute.adapters.restful.currency.dto;

public record CurrencyDTO(
        Long id,
        String name,
        String symbol,
        String code
) {}
