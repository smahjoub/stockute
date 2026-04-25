package com.smahjoub.stockute.adapters.restful.dividend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DividendEntitlementDto(
        Long id,
        BigDecimal eligibleShares,
        BigDecimal dividendPerShare,
        BigDecimal grossAmount,
        Long currencyRefId,
        LocalDateTime exDate,
        LocalDateTime paymentDate,
        String status
) {}
