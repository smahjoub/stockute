package com.smahjoub.stockute.application.port.currency.in;

import com.smahjoub.stockute.domain.model.Currency;
import reactor.core.publisher.Mono;

public interface CurrencyUseCase {

    Mono<Currency> getCurrencyBYId(Long id);
}
