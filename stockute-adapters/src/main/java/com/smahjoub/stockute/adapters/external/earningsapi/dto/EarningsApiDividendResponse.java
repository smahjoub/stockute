package com.smahjoub.stockute.adapters.external.earningsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EarningsApiDividendResponse {

    private String name;
    private String symbol;

    @JsonProperty("dividend_ex_date")
    private String dividendExDate;

    @JsonProperty("payment_date")
    private String paymentDate;

    @JsonProperty("dividend_rate")
    private BigDecimal dividendRate;
}