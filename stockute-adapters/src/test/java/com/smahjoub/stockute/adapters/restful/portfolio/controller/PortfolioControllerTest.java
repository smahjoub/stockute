package com.smahjoub.stockute.adapters.restful.portfolio.controller;

import com.smahjoub.stockute.adapters.MockDomainObjects;
import com.smahjoub.stockute.adapters.common.WithMockCustomUser;
import com.smahjoub.stockute.adapters.restful.portfolio.dto.CreatePortfolioDTO;
import com.smahjoub.stockute.adapters.restful.portfolio.dto.PortfolioDTO;
import com.smahjoub.stockute.adapters.restful.portfolio.dto.UpdatePortfolioDTO;
import com.smahjoub.stockute.adapters.restful.portfolio.mapper.CreatePortfolioRecordMapper;
import com.smahjoub.stockute.adapters.restful.portfolio.mapper.PortfolioMapper;
import com.smahjoub.stockute.adapters.restful.portfolio.mapper.UpdatePortfolioRecordMapper;
import com.smahjoub.stockute.application.port.portfolio.in.PortfolioUseCase;
import com.smahjoub.stockute.domain.model.Portfolio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.ReactorContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ReactorContextTestExecutionListener.class})
class PortfolioControllerTest {
    @Mock
    private PortfolioUseCase useCase;
    @Mock
    private CreatePortfolioRecordMapper createPortfolioRecordMapper;
    @Mock
    private UpdatePortfolioRecordMapper updatePortfolioRecordMapper;
    @Mock
    private PortfolioMapper portfolioMapper;


    @InjectMocks
    private PortfolioController controller;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @WithMockCustomUser
    void testCreatePortfolio() {
        CreatePortfolioDTO dto = MockDomainObjects.CREATE_PORTFOLIO_DTO_1_MOCK;
        when(createPortfolioRecordMapper.toPortfolio(dto)).thenReturn(MockDomainObjects.PORTFOLIO_1);
        when(useCase.createPortfolio(anyString(), eq(MockDomainObjects.PORTFOLIO_1))).thenReturn(Mono.just(MockDomainObjects.PORTFOLIO_1));
        when(portfolioMapper.toPortfolioDTO(MockDomainObjects.PORTFOLIO_1)).thenReturn(MockDomainObjects.PORTFOLIO_DTO_1_MOCK);
        webTestClient.post().uri("/portfolios")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockCustomUser
    void testGetPortfolio() {
        when(useCase.getUserPortfolio(anyString(), any())).thenReturn(Mono.just(MockDomainObjects.PORTFOLIO_1));
        when(portfolioMapper.toPortfolioDTO(MockDomainObjects.PORTFOLIO_1)).thenReturn(MockDomainObjects.PORTFOLIO_DTO_1_MOCK);
        webTestClient.get().uri("/portfolios/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockCustomUser
    void testGetAllPortfolios() {
        when(useCase.getAllUserPortfolios(anyString())).thenReturn(Flux.just(MockDomainObjects.PORTFOLIO_1, MockDomainObjects.PORTFOLIO_2));
        when(portfolioMapper.toPortfolioDTO(MockDomainObjects.PORTFOLIO_1)).thenReturn(MockDomainObjects.PORTFOLIO_DTO_1_MOCK);
        when(portfolioMapper.toPortfolioDTO(MockDomainObjects.PORTFOLIO_2)).thenReturn(MockDomainObjects.PORTFOLIO_DTO_2_MOCK);
        webTestClient.get().uri("/portfolios")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockCustomUser
    void testDeletePortfolio() {
        when(useCase.removePortfolio(anyString(), any())).thenReturn(Mono.empty());
        webTestClient.delete().uri("/portfolios/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockCustomUser
    void testUpdatePortfolio() {
        // Mock data
        UpdatePortfolioDTO dto = MockDomainObjects.UPDATE_PORTFOLIO_DTO_1_MOCK;
        Portfolio existingPortfolio = MockDomainObjects.PORTFOLIO_1; // Assume this has version 1L
        existingPortfolio.setVersion(1L); // Ensure version matches the header

        // Mock mapper for updating portfolio
        when(updatePortfolioRecordMapper.updatePortfolioFromDto(eq(dto), eq(existingPortfolio)))
                .thenReturn(existingPortfolio); // Or return an updated version if needed

        // Mock useCase.getUserPortfolio to return existing portfolio for version check
        when(useCase.getUserPortfolio(eq("admin"), eq(1L)))
                .thenReturn(Mono.just(existingPortfolio));

        // Mock useCase.updatePortfolio to return updated portfolio
        when(useCase.updatePortfolio(eq("admin"), eq(existingPortfolio)))
                .thenReturn(Mono.just(existingPortfolio));

        // Mock mapper to DTO
        when(portfolioMapper.toPortfolioDTO(eq(existingPortfolio)))
                .thenReturn(MockDomainObjects.PORTFOLIO_DTO_1_MOCK);

        // Perform the request with mocked authentication (username: "testUser")
        webTestClient
                .put()
                .uri("/portfolios/1")
                .header(HttpHeaders.IF_MATCH, "1")
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PortfolioDTO.class)
                .isEqualTo(MockDomainObjects.PORTFOLIO_DTO_1_MOCK); // Optional: Assert response body
    }

}
