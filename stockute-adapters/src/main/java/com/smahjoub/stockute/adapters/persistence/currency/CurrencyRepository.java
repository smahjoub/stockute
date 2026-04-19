package com.smahjoub.stockute.adapters.persistence.currency;

import com.smahjoub.stockute.domain.model.Currency;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CurrencyRepository extends R2dbcRepository<Currency, Long> {
    Mono<Currency> findByCode(String code);
}
