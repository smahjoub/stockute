package com.smahjoub.stockute.adapters.persistence.dividend;

import com.smahjoub.stockute.domain.model.SecurityDividend;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface SecurityDividendRepository
        extends ReactiveCrudRepository<SecurityDividend, Long> {

    Mono<SecurityDividend> findBySecurityRefIdAndExDateAndPaymentDateAndDividendPerShare(
            Long securityRefId,
            LocalDateTime exDate,
            LocalDateTime paymentDate,
            BigDecimal dividendPerShare
    );
}
