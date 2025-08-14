package com.smahjoub.stockute.application.port.currency.out;

import com.smahjoub.stockute.domain.model.Currency;
import reactor.core.publisher.Mono;

public interface CurrencyPort {

    Mono<Currency> findById(Long id);
}
