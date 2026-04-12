package com.smahjoub.stockute.adapters.restful.currency.controller;

import com.smahjoub.stockute.adapters.common.WithMockCustomUser;
import com.smahjoub.stockute.adapters.restful.currency.dto.CurrencyDTO;
import com.smahjoub.stockute.adapters.restful.currency.mapper.CurrencyMapper;
import com.smahjoub.stockute.application.port.currency.in.CurrencyUseCase;
import com.smahjoub.stockute.domain.model.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.test.context.support.ReactorContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ReactorContextTestExecutionListener.class})
class CurrencyControllerTest {

    @Mock
    private CurrencyUseCase currencyUseCase;

    @Mock
    private CurrencyMapper currencyMapper;

    @InjectMocks
    private CurrencyController controller;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @WithMockCustomUser
    void testGetAllCurrencies() {
        Currency currency1 = new Currency(1L, "US Dollar", "$", "USD");
        Currency currency2 = new Currency(2L, "Euro", "€", "EUR");

        CurrencyDTO currencyDTO1 = new CurrencyDTO(1L, "US Dollar", "$", "USD");
        CurrencyDTO currencyDTO2 = new CurrencyDTO(2L, "Euro", "€", "EUR");

        when(currencyUseCase.getAllCurrencies()).thenReturn(Flux.just(currency1, currency2));
        when(currencyMapper.toDTO(currency1)).thenReturn(currencyDTO1);
        when(currencyMapper.toDTO(currency2)).thenReturn(currencyDTO2);

        webTestClient.get()
                .uri("/v1/currencies")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CurrencyDTO.class)
                .isEqualTo(List.of(currencyDTO1, currencyDTO2));
    }
}