package com.smahjoub.stockute.adapters.persistence.currency;

import com.smahjoub.stockute.domain.model.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class CurrencyAdapterTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Currency currency = new Currency(1L, "US Dollar", "$", "USD");
        when(currencyRepository.findById(anyLong())).thenReturn(Mono.just(currency));

        StepVerifier.create(adapter.findById(1L))
                .expectNextMatches(result ->
                        result.getId().equals(1L) &&
                                result.getName().equals("US Dollar") &&
                                result.getSymbol().equals("$") &&
                                result.getCode().equals("USD"))
                .verifyComplete();
    }

    @Test
    void testFindAll() {
        Currency currency1 = new Currency(1L, "US Dollar", "$", "USD");
        Currency currency2 = new Currency(2L, "Euro", "€", "EUR");

        when(currencyRepository.findAll()).thenReturn(Flux.just(currency1, currency2));

        StepVerifier.create(adapter.findAll())
                .expectNext(currency1)
                .expectNext(currency2)
                .verifyComplete();
    }

    @Test
    void testFindByCode() {
        Currency currency = new Currency(1L, "US Dollar", "$", "USD");
        when(currencyRepository.findByCode(anyString())).thenReturn(Mono.just(currency));

        StepVerifier.create(adapter.findByCode("USD"))
                .expectNextMatches(result ->
                        result.getId().equals(1L) &&
                                result.getName().equals("US Dollar") &&
                                result.getSymbol().equals("$") &&
                                result.getCode().equals("USD"))
                .verifyComplete();
    }
}
