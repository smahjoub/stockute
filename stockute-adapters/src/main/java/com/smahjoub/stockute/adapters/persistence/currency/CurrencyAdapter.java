package com.smahjoub.stockute.adapters.persistence.currency;

import com.smahjoub.stockute.application.port.currency.out.CurrencyPort;
import com.smahjoub.stockute.domain.model.Currency;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class CurrencyAdapter implements CurrencyPort {
    private final CurrencyRepository currencyRepository;
    @Override
    public Mono<Currency> findById(final Long id) {
        return currencyRepository.findById(id);
    }


    @Override
    public Flux<Currency> findAll() {
        return currencyRepository.findAll();
    }

    @Override
    public Mono<Currency> findByCode(final String code) {
        return currencyRepository.findByCode(code);
    }
}
