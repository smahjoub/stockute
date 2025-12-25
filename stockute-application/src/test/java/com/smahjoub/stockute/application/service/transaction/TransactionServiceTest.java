package com.smahjoub.stockute.application.service.transaction;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.smahjoub.stockute.application.exception.PortfolioNotFoundException;
import com.smahjoub.stockute.application.port.asset.out.AssetPort;
import com.smahjoub.stockute.application.port.portfolio.out.PortfolioPort;
import com.smahjoub.stockute.application.port.transaction.out.TransactionPort;
import com.smahjoub.stockute.domain.model.Asset;
import com.smahjoub.stockute.domain.model.Portfolio;
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
class TransactionServiceTest {

    @Mock
    private PortfolioPort portfolioPort;

    @Mock
    private TransactionPort transactionPort;

    @Mock
    private AssetPort assetPort;

    @InjectMocks
    private TransactionService transactionService;

    private Portfolio portfolio;
    private Asset asset;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio();
        portfolio.setId(1L);
        portfolio.setCurrencyRefId(1L);

        asset = new Asset();
        asset.setId(100L);

        transaction = new Transaction();
        transaction.setQuantity(10.0);
        transaction.setPrice(BigDecimal.valueOf(100.0));
        transaction.setFees(BigDecimal.valueOf(1.0));
        transaction.setType("BUY");
        transaction.setTransactionDate(LocalDateTime.now());
    }

    @Test
    void createTransaction_AssetExists_UpdatesAndSavesTransaction() {
        // Given
        when(portfolioPort.findById(1L)).thenReturn(Mono.just(portfolio));
        when(assetPort.getAssetForPortfolio(eq(1L), eq("BTC"), eq("BINANCE"), eq(1L)))
                .thenReturn(Mono.just(asset)); // Called 2x - service logic requires it

        when(assetPort.createAssetForPortfolio("Bitcoin", 1L, "BTC", "BINANCE", 1L))
                .thenReturn(Mono.just(asset));
        when(assetPort.updateAsset(eq(100L), any(Transaction.class)))
                .thenReturn(Mono.just(asset));
        when(transactionPort.save(any(Transaction.class))).thenReturn(Mono.just(transaction));

        // When & Then
        StepVerifier.create(
                        transactionService.createTransaction("Bitcoin", "BTC", "BINANCE", transaction, 1L)
                )
                .assertNext(result -> {
                    assert result.getAssetRefId() == 100L;
                    assert result.getPortfolioRefId() == 1L;
                })
                .verifyComplete();

        verify(portfolioPort).findById(1L);
        verify(assetPort, times(2)).getAssetForPortfolio(1L, "BTC", "BINANCE", 1L); // Expect 2 calls
        verify(assetPort).updateAsset(eq(100L), any(Transaction.class));
        verify(transactionPort).save(any(Transaction.class));
        verifyNoMoreInteractions(assetPort, transactionPort, portfolioPort);
    }


    @Test
    void createTransaction_AssetNotExists_CreatesThenUpdatesAndSaves() {
        // Given
        when(portfolioPort.findById(1L)).thenReturn(Mono.just(portfolio));

        // After creation, re-query must return the created asset
        when(assetPort.getAssetForPortfolio(eq(1L), eq("BTC"), eq("BINANCE"), eq(1L)))
                .thenReturn(Mono.empty())      // First call
                .thenReturn(Mono.just(asset)); // Second call after creation

        when(assetPort.createAssetForPortfolio("Bitcoin", 1L, "BTC", "BINANCE", 1L))
                .thenReturn(Mono.just(asset));

        when(assetPort.updateAsset(eq(100L), any(Transaction.class)))
                .thenReturn(Mono.just(asset));

        when(transactionPort.save(any(Transaction.class)))
                .thenReturn(Mono.just(transaction));

        // When & Then
        StepVerifier.create(
                        transactionService.createTransaction("Bitcoin", "BTC", "BINANCE", transaction, 1L)
                )
                .assertNext(result -> {
                    assert result.getAssetRefId() == 100L;
                    assert result.getPortfolioRefId() == 1L;
                })
                .verifyComplete();

        verify(portfolioPort).findById(1L);
        verify(assetPort, times(2)).getAssetForPortfolio(1L, "BTC", "BINANCE", 1L);
        verify(assetPort).createAssetForPortfolio("Bitcoin", 1L, "BTC", "BINANCE", 1L);
        verify(assetPort).updateAsset(eq(100L), any(Transaction.class));
        verify(transactionPort).save(any(Transaction.class));
        verifyNoMoreInteractions(assetPort, transactionPort, portfolioPort);
    }

    @Test
    void createTransaction_PortfolioNotFound_ErrorsWithPortfolioNotFoundException() {
        // Given
        when(portfolioPort.findById(999L)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(
                        transactionService.createTransaction("Test", "TEST", "TESTEX", transaction, 999L)
                )
                .expectErrorMatches(throwable ->
                        throwable instanceof PortfolioNotFoundException
                                || throwable.getCause() instanceof PortfolioNotFoundException
                )
                .verify();

        verify(portfolioPort).findById(999L);
        verifyNoInteractions(assetPort, transactionPort);
    }

    @Test
    void getAllTransactionsForAssetInPortfolio_ReturnsTransactions() {
        // Given
        Transaction tx1 = new Transaction();
        Transaction tx2 = new Transaction();
        when(transactionPort.findAllByPortfolioIdAndAssetId(1L, 100L))
                .thenReturn(Flux.just(tx1, tx2));

        // When & Then
        StepVerifier.create(
                        transactionService.getAllTransactionsForAssetInPortfolio(1L, 100L)
                )
                .expectNext(tx1, tx2)
                .verifyComplete();

        verify(transactionPort).findAllByPortfolioIdAndAssetId(1L, 100L);
        verifyNoMoreInteractions(transactionPort);
    }

    @Test
    void getAllTransactionsForAssetInPortfolio_NoTransactions_ReturnsEmpty() {
        // Given
        when(transactionPort.findAllByPortfolioIdAndAssetId(1L, 100L)).thenReturn(Flux.empty());

        // When & Then
        StepVerifier.create(
                        transactionService.getAllTransactionsForAssetInPortfolio(1L, 100L)
                )
                .verifyComplete();

        verify(transactionPort).findAllByPortfolioIdAndAssetId(1L, 100L);
        verifyNoMoreInteractions(transactionPort);
    }
}
