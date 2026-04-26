package com.smahjoub.stockute.adapters.restful.dividend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PortfolioDividendStatsDto {
    private BigDecimal pastMonthAccumulated;
    private BigDecimal pastYearAccumulated;
    private BigDecimal incomingDividend;
    private BigDecimal nextYearForecast;
}