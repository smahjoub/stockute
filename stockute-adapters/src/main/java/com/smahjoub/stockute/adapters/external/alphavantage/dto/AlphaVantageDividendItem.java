package com.smahjoub.stockute.adapters.external.alphavantage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlphaVantageDividendItem {

    @JsonProperty("ex_dividend_date")
    private String exDividendDate;

    @JsonProperty("declaration_date")
    private String declarationDate;

    @JsonProperty("record_date")
    private String recordDate;

    @JsonProperty("payment_date")
    private String paymentDate;

    @JsonProperty("amount")
    private String amount;
}