package com.smahjoub.stockute.application.service.currency;

import com.smahjoub.stockute.application.port.currency.out.CurrencyPort;
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
import static org.mockito.Mockito.when;

class CurrencyServiceTest {

    @Mock
    private CurrencyPort currencyPort;

    @InjectMocks
    private CurrencyService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrencyById() {
        Currency currency = new Currency(1L, "US Dollar", "$", "USD");
        when(currencyPort.findById(anyLong())).thenReturn(Mono.just(currency));

        StepVerifier.create(service.getCurrencyBYId(1L))
                .expectNextMatches(result ->
                        result.getId().equals(1L) &&
                                result.getName().equals("US Dollar") &&
                                result.getSymbol().equals("$") &&
                                result.getCode().equals("USD"))
                .verifyComplete();
    }

    @Test
    void testGetCurrencyById_NotFound() {
        when(currencyPort.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(service.getCurrencyBYId(1L))
                .expectErrorMatches(error ->
                        error instanceof IllegalArgumentException &&
                                error.getMessage().equals("Currency not found with id: 1"))
                .verify();
    }

    @Test
    void testGetAllCurrencies() {
        Currency currency1 = new Currency(1L, "US Dollar", "$", "USD");
        Currency currency2 = new Currency(2L, "Euro", "€", "EUR");

        when(currencyPort.findAll()).thenReturn(Flux.just(currency1, currency2));

        StepVerifier.create(service.getAllCurrencies())
                .expectNext(currency1)
                .expectNext(currency2)
                .verifyComplete();
    }
}