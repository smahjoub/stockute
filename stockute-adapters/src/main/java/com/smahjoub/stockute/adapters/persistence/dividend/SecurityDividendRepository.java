package com.smahjoub.stockute.adapters.persistence.dividend;

import com.smahjoub.stockute.domain.model.SecurityDividend;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface SecurityDividendRepository
        extends ReactiveCrudRepository<SecurityDividend, Long> {

    @Query("""
        SELECT * FROM security_dividends
        WHERE security_ref_id = :securityRefId
          AND ex_date = :exDate
          AND (payment_date = :paymentDate OR (payment_date IS NULL AND :paymentDate IS NULL))
          AND (dividend_per_share = :dividendPerShare OR (dividend_per_share IS NULL AND :dividendPerShare IS NULL))
        LIMIT 1
    """)
    Mono<SecurityDividend> findByBusinessKey(
            Long securityRefId,
            LocalDateTime exDate,
            LocalDateTime paymentDate,
            BigDecimal dividendPerShare
    );
}
