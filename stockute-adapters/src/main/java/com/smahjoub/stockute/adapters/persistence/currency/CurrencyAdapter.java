package com.smahjoub.stockute.adapters.persistence.currency;

import com.smahjoub.stockute.application.port.currency.out.CurrencyPort;
import com.smahjoub.stockute.domain.model.Currency;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class CurrencyAdapter implements CurrencyPort {
    private final CurrencyRepository currencyRepository;
    @Override
    public Mono<Currency> findById(Long id) {
        return currencyRepository.findById(id);
    }
}
