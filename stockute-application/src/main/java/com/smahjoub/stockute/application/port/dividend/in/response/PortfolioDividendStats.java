package com.smahjoub.stockute.application.port.dividend.in.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PortfolioDividendStats {
    private BigDecimal pastMonthAccumulated;
    private BigDecimal pastYearAccumulated;
    private BigDecimal incomingDividend;
    private BigDecimal nextYearForecast;
}