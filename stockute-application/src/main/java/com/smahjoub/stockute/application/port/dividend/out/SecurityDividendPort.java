package com.smahjoub.stockute.application.port.dividend.out;


import com.smahjoub.stockute.domain.model.SecurityDividend;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface SecurityDividendPort {

    Mono<SecurityDividend> findById(Long id);

    Mono<SecurityDividend> save(SecurityDividend securityDividend);

    Mono<SecurityDividend> findByBusinessKey(
            Long securityRefId,
            LocalDateTime exDate,
            LocalDateTime paymentDate,
            BigDecimal dividendPerShare
    );
}