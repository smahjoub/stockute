package com.smahjoub.stockute.adapters.persistence.dividend;

import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendPort;
import com.smahjoub.stockute.domain.model.SecurityDividend;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SecurityDividendAdapter implements SecurityDividendPort {

    private final SecurityDividendRepository repository;

    @Override
    public Mono<SecurityDividend> findById(final Long id) {
        return repository.findById(id);
    }
}