package com.smahjoub.stockute.adapters.external.alphavantage.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AlphaVantageDividendResponse {
    private String symbol;
    private List<AlphaVantageDividendItem> data;
}