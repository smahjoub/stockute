package com.smahjoub.stockute.application.service.currency;

import com.smahjoub.stockute.application.port.currency.in.CurrencyUseCase;
import com.smahjoub.stockute.application.port.currency.out.CurrencyPort;
import com.smahjoub.stockute.domain.model.Currency;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CurrencyService implements CurrencyUseCase {
    private final CurrencyPort currencyPort;
    @Override
    public Mono<Currency> getCurrencyBYId(final Long id) {
        return currencyPort.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Currency not found with id: " + id)));
    }
}
