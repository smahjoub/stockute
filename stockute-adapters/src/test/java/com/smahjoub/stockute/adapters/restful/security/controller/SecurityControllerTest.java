package com.smahjoub.stockute.adapters.restful.security.controller;

import com.smahjoub.stockute.adapters.common.WithMockCustomUser;
import com.smahjoub.stockute.adapters.restful.security.dto.SecurityDTO;
import com.smahjoub.stockute.adapters.restful.security.mapper.SecurityMapper;
import com.smahjoub.stockute.application.port.security.in.SearchSecurityUseCase;
import com.smahjoub.stockute.domain.model.Security;
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


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ReactorContextTestExecutionListener.class})
class SecurityControllerTest {

    @Mock
    private SearchSecurityUseCase searchSecurityUseCase;

    @Mock
    private SecurityMapper securityMapper;

    @InjectMocks
    private SecurityController controller;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @WithMockCustomUser
    void testSearchSecurities() {
        String ticker = "AAPL";

        // Domain model
        Security s1 = new Security();
        s1.setId(1L);
        s1.setSymbol("AAPL");
        s1.setName("Apple Inc");
        s1.setType("Equity");
        s1.setRegion("United States");
        s1.setCurrency("USD");
        s1.setActive(true);

        Security s2 = new Security();
        s2.setId(2L);
        s2.setSymbol("AAPL34");
        s2.setName("Apple Inc ADR");
        s2.setType("Equity");
        s2.setRegion("Brazil");
        s2.setCurrency("BRL");
        s2.setActive(true);

        // DTOs
        SecurityDTO dto1 = new SecurityDTO(
                1L, "AAPL", "Apple Inc", "Equity", "United States", "USD", true
        );

        SecurityDTO dto2 = new SecurityDTO(
                2L, "AAPL34", "Apple Inc ADR", "Equity", "Brazil", "BRL", true
        );

        when(searchSecurityUseCase.search(ticker))
                .thenReturn(Flux.just(s1, s2));

        when(securityMapper.toDto(s1)).thenReturn(dto1);
        when(securityMapper.toDto(s2)).thenReturn(dto2);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/securities/search")
                        .queryParam("tickerSymbol", ticker)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SecurityDTO.class)
                .consumeWith(result -> {
                    var list = result.getResponseBody();
                    assertThat(list).hasSize(2);

                    assertThat(list.get(0).getSymbol()).isEqualTo("AAPL");
                    assertThat(list.get(1).getSymbol()).isEqualTo("AAPL34");
                });

    }
}
