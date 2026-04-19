package com.smahjoub.stockute.application.service.security;

import com.smahjoub.stockute.application.port.currency.out.CurrencyPort;
import com.smahjoub.stockute.application.port.security.out.SecurityPort;
import com.smahjoub.stockute.application.port.security.out.SecuritySearchPort;
import com.smahjoub.stockute.domain.model.Currency;
import com.smahjoub.stockute.domain.model.Security;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SecurityServiceLocalTest {

    private SecurityPort securityPort;
    private SecuritySearchPort securitySearchPort;
    private CurrencyPort currencyPort;

    private SecurityService service;

    @BeforeEach
    void setup() {
        securityPort = mock(SecurityPort.class);
        securitySearchPort = mock(SecuritySearchPort.class);
        currencyPort = mock(CurrencyPort.class);

        service = new SecurityService(securityPort, securitySearchPort, currencyPort);
    }


    @Test
    void search_whenLocalEmpty_shouldSearchExternalEnrichAndSave() {
        // GIVEN
        String keyword = "AAPL";

        // Local DB returns empty
        when(securityPort.searchBySymbolOrName(keyword))
                .thenReturn(Flux.empty());

        // External search returns one security
        Security external = new Security();
        external.setSymbol("AAPL");
        external.setName("Apple Inc");
        external.setCurrency("USD");

        when(securitySearchPort.search(keyword))
                .thenReturn(Flux.just(external));

        // Currency lookup
        Currency usd = new Currency();
        usd.setId(10L);
        usd.setCode("USD");

        when(currencyPort.findByCode("USD"))
                .thenReturn(Mono.just(usd));

        // saveAll returns the same securities
        when(securityPort.saveAll(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        StepVerifier.create(service.search(keyword))
                .expectNextMatches(sec ->
                        sec.getSymbol().equals("AAPL") &&
                                sec.getCurrencyRefId().equals(10L) &&
                                sec.getCreatedDate() != null &&
                                sec.getLastModifiedDate() != null &&
                                sec.getVersion() == 0L
                )
                .verifyComplete();

        // THEN
        verify(securityPort).searchBySymbolOrName(keyword);
        verify(securitySearchPort).search(keyword);
        verify(currencyPort).findByCode("USD");

        ArgumentCaptor<Flux<Security>> captor = ArgumentCaptor.forClass(Flux.class);
        verify(securityPort).saveAll(captor.capture());

        List<Security> saved = captor.getValue().collectList().block();
        assertThat(saved).hasSize(1);
        assertThat(saved.getFirst().getCurrencyRefId()).isEqualTo(10L);
    }

    @Test
    void search_whenLocalResultsExist_shouldEnrichCurrencyAndReturnLocal() {
        // GIVEN
        String keyword = "AAPL";

        Security local = new Security();
        local.setSymbol("AAPL");
        local.setName("Apple Inc");
        local.setCurrencyRefId(10L); // local DB has currencyRefId
        local.setActive(true);

        when(securityPort.searchBySymbolOrName(keyword))
                .thenReturn(Flux.just(local));

        Currency usd = new Currency();
        usd.setId(10L);
        usd.setCode("USD");

        when(currencyPort.findById(10L))
                .thenReturn(Mono.just(usd));

        // WHEN
        StepVerifier.create(service.search(keyword))
                .assertNext(sec -> {
                    assertThat(sec.getSymbol()).isEqualTo("AAPL");
                    assertThat(sec.getCurrency()).isEqualTo("USD"); // enriched
                    assertThat(sec.getCurrencyRefId()).isEqualTo(10L);
                })
                .verifyComplete();

        // THEN
        verify(securityPort).searchBySymbolOrName(keyword);
        verify(currencyPort).findById(10L);

        verify(securitySearchPort, never()).search(anyString());
        verify(securityPort, never()).saveAll(any());
    }
}
