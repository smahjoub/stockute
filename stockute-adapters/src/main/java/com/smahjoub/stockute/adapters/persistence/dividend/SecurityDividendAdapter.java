package com.smahjoub.stockute.adapters.persistence.dividend;

import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendPort;
import com.smahjoub.stockute.domain.model.SecurityDividend;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SecurityDividendAdapter implements SecurityDividendPort {

    private final SecurityDividendRepository repository;

    @Override
    public Mono<SecurityDividend> findById(final Long id) {
        return repository.findById(id);
    }

    @Override
    public Mono<SecurityDividend> save(final SecurityDividend securityDividend) {
        return repository.save(securityDividend);
    }

    @Override
    public Mono<SecurityDividend> findByBusinessKey(
            final Long securityRefId,
            final LocalDateTime exDate,
            final LocalDateTime paymentDate,
            final BigDecimal dividendPerShare
    ) {
        return repository.findByBusinessKey(
                securityRefId,
                exDate,
                paymentDate,
                dividendPerShare
        );
    }
}