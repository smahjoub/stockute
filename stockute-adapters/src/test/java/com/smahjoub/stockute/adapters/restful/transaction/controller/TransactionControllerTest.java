package com.smahjoub.stockute.adapters.restful.transaction.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.smahjoub.stockute.adapters.restful.transaction.dto.CreateTransactionDTO;
import com.smahjoub.stockute.adapters.restful.transaction.dto.TransactionDTO;
import com.smahjoub.stockute.adapters.restful.transaction.mapper.CreateTransactionMapper;
import com.smahjoub.stockute.adapters.restful.transaction.mapper.TransactionMapper;
import com.smahjoub.stockute.application.service.transaction.TransactionService;
import com.smahjoub.stockute.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private CreateTransactionMapper createTransactionMapper;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private CreateTransactionDTO createTransactionDTO;
    private Transaction domainTransaction;
    private Transaction transaction;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        // Test data setup
        createTransactionDTO = new CreateTransactionDTO(
                "Apple Inc.", "AAPL", "NASDAQ", 10.0, 1L,
                BigDecimal.valueOf(150), BigDecimal.ZERO, "Test", "BUY", LocalDateTime.now()
        );

        domainTransaction = new Transaction();
        domainTransaction.setId(1L);
        domainTransaction.setAssetRefId(100L);
        domainTransaction.setPortfolioRefId(1L);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setPortfolioRefId(1L);
        transaction.setAssetRefId(100L);

        transactionDTO = new TransactionDTO(
                1L, 1L, 100L, "BUY", 10.0, BigDecimal.valueOf(150),
                BigDecimal.ZERO, LocalDateTime.now(), "Test"
        );
    }

    @Test
    void create_Success_ReturnsTransactionDTO() {
        // Given
        when(createTransactionMapper.toDomain(createTransactionDTO)).thenReturn(domainTransaction);
        when(transactionService.createTransaction(
                eq("Apple Inc."), eq("AAPL"), eq("NASDAQ"), eq(domainTransaction), eq(1L)))
                .thenReturn(Mono.just(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(transactionDTO);

        // When
        Mono<TransactionDTO> result = transactionController.create(1L, createTransactionDTO);

        // Then
        StepVerifier.create(result)
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(transactionService).createTransaction(
                eq("Apple Inc."), eq("AAPL"), eq("NASDAQ"), eq(domainTransaction), eq(1L));
        verify(transactionMapper).toDto(transaction);
    }

    @Test
    void create_ServiceError_ReturnsError() {
        // Given
        when(createTransactionMapper.toDomain(createTransactionDTO)).thenReturn(domainTransaction);
        when(transactionService.createTransaction(anyString(), anyString(), anyString(), any(Transaction.class), anyLong()))
                .thenReturn(Mono.error(new RuntimeException("Service error")));

        // When & Then
        StepVerifier.create(transactionController.create(1L, createTransactionDTO))
                .expectError(RuntimeException.class)
                .verify();

        verify(transactionService).createTransaction(anyString(), anyString(), anyString(), any(Transaction.class), anyLong());
    }

    @Test
    void getAllTransactionsForAssetInPortfolio_Success_ReturnsTransactions() {
        // Given
        Transaction tx2 = new Transaction();
        tx2.setId(2L);
        TransactionDTO tx2DTO = new TransactionDTO(
                2L, 1L, 100L, "SELL", 5.0, BigDecimal.valueOf(160),
                BigDecimal.ZERO, LocalDateTime.now(), "Second"
        );

        when(transactionService.getAllTransactionsForAssetInPortfolio(1L, 100L))
                .thenReturn(Flux.just(transaction, tx2));
        when(transactionMapper.toDto(transaction)).thenReturn(transactionDTO);
        when(transactionMapper.toDto(tx2)).thenReturn(tx2DTO);

        // When
        Flux<TransactionDTO> result = transactionController.getAllTransactionsForAssetInPortfolio(1L, 100L);

        // Then
        StepVerifier.create(result)
                .expectNext(transactionDTO, tx2DTO)
                .verifyComplete();
    }

    @Test
    void getAllTransactionsForAssetInPortfolio_Empty_ReturnsEmpty() {
        // Given
        when(transactionService.getAllTransactionsForAssetInPortfolio(1L, 100L))
                .thenReturn(Flux.empty());

        // When & Then
        StepVerifier.create(transactionController.getAllTransactionsForAssetInPortfolio(1L, 100L))
                .verifyComplete();
    }

    @Test
    void getAllTransactionsForAssetInPortfolio_ServiceError_ReturnsError() {
        // Given
        when(transactionService.getAllTransactionsForAssetInPortfolio(1L, 100L))
                .thenReturn(Flux.error(new RuntimeException("Service error")));

        // When & Then
        StepVerifier.create(transactionController.getAllTransactionsForAssetInPortfolio(1L, 100L))
                .expectError(RuntimeException.class)
                .verify();
    }
}