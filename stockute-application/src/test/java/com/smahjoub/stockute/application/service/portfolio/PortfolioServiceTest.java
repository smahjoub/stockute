package com.smahjoub.stockute.application.service.portfolio;

import com.smahjoub.stockute.application.port.currency.out.CurrencyPort;
import com.smahjoub.stockute.application.port.membership.in.UserUseCase;
import com.smahjoub.stockute.application.port.portfolio.out.PortfolioPort;
import com.smahjoub.stockute.domain.model.Currency;
import com.smahjoub.stockute.domain.model.Portfolio;
import com.smahjoub.stockute.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class PortfolioServiceTest {
    @Mock
    private PortfolioPort repository;
    @Mock
    private UserUseCase userUseCase;
    @Mock
    private CurrencyPort currencyPort;
    @InjectMocks
    private PortfolioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePortfolio() {
        User user = new User();
        user.setId(1L);
        Portfolio portfolio = new Portfolio();
        portfolio.setId(1L);
        portfolio.setCurrencyRefId(1L);
        when(userUseCase.getUserByUsername(anyString())).thenReturn(Mono.just(user));

        when(repository.save(any(Portfolio.class))).thenReturn(Mono.just(portfolio));
        when(repository.findById(1L)).thenReturn(Mono.just(portfolio));
        when(currencyPort.findById(1L)).thenReturn(Mono.just(new Currency(1L, "USD", "$", "USD")));


        StepVerifier.create(service.createPortfolio("user", portfolio))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testUpdatePortfolio() {
        User user = new User();
        user.setId(1L);
        Portfolio portfolio = new Portfolio();

        when(userUseCase.getUserByUsername(anyString())).thenReturn(Mono.just(user));
        when(repository.save(any(Portfolio.class))).thenReturn(Mono.just(portfolio));

        StepVerifier.create(service.updatePortfolio("user", portfolio))
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();
    }

    @Test
    void testGetUserPortfolio_PermissionDenied() {
        User user = new User();
        user.setId(1L);
        Portfolio portfolio = new Portfolio();
        portfolio.setUserRefId(2L); // Different user ID to trigger permission issue

        when(userUseCase.getUserByUsername(anyString())).thenReturn(Mono.just(user));
        when(repository.findById(anyLong())).thenReturn(Mono.just(portfolio));

        StepVerifier.create(service.getUserPortfolio("user", 1L))
                .expectError() // Assert that the stream terminates with an error
                .verify();
    }
}


