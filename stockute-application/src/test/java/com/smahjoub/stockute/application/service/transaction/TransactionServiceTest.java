package com.smahjoub.stockute.application.service.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.smahjoub.stockute.application.exception.PortfolioNotFoundException;
import com.smahjoub.stockute.application.exception.SecurityNotFoundException;
import com.smahjoub.stockute.application.port.asset.out.AssetPort;
import com.smahjoub.stockute.application.port.portfolio.out.PortfolioPort;
import com.smahjoub.stockute.application.port.security.out.SecurityPort;
import com.smahjoub.stockute.application.port.transaction.out.TransactionPort;
import com.smahjoub.stockute.domain.model.Asset;
import com.smahjoub.stockute.domain.model.Portfolio;
import com.smahjoub.stockute.domain.model.Security;
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

    @Mock
    private SecurityPort securityPort;

    @InjectMocks
    private TransactionService transactionService;

    private Portfolio portfolio;
    private Asset asset;
    private Transaction transaction;
    private Security security;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio();
        portfolio.setId(1L);
        portfolio.setCurrencyRefId(1L);

        asset = new Asset();
        asset.setId(100L);

        security = new Security();
        security.setId(200L);

        transaction = new Transaction();
        transaction.setQuantity(10.0);
        transaction.setPrice(BigDecimal.valueOf(100.0));
        transaction.setFees(BigDecimal.valueOf(1.0));
        transaction.setType("BUY");
        transaction.setTransactionDate(LocalDateTime.now());
    }

    @Test
    void createTransaction_AssetExists_UpdatesAndSavesTransaction() {
        when(portfolioPort.findById(1L)).thenReturn(Mono.just(portfolio));
        when(securityPort.findById(200L)).thenReturn(Mono.just(security));
        when(assetPort.getAssetForPortfolioBySecurityRefId(1L, 200L, 1L))
                .thenReturn(Mono.just(asset));

        when(assetPort.updateAsset(eq(100L), any(Transaction.class)))
                .thenReturn(Mono.just(asset));
        when(transactionPort.save(any(Transaction.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(
                        transactionService.createTransaction("Bitcoin", 200L, transaction, 1L)
                )
                .assertNext(result -> {
                    assertEquals(100L, result.getAssetRefId());
                    assertEquals(1L, result.getPortfolioRefId());
                })
                .verifyComplete();

        verify(portfolioPort).findById(1L);
        verify(securityPort).findById(200L);
        verify(assetPort).getAssetForPortfolioBySecurityRefId(1L, 200L, 1L);
        verify(assetPort).updateAsset(100L, transaction);
        verify(transactionPort).save(transaction);
    }

    @Test
    void createTransaction_AssetNotExists_CreatesThenUpdatesAndSaves() {
        when(portfolioPort.findById(1L)).thenReturn(Mono.just(portfolio));
        when(securityPort.findById(200L)).thenReturn(Mono.just(security));

        when(assetPort.getAssetForPortfolioBySecurityRefId(1L, 200L, 1L))
                .thenReturn(Mono.empty())
                .thenReturn(Mono.just(asset));

        when(assetPort.createAssetForPortfolio("Bitcoin", 1L, 200L, 1L))
                .thenReturn(Mono.just(asset));

        when(assetPort.updateAsset(eq(100L), any(Transaction.class)))
                .thenReturn(Mono.just(asset));

        when(transactionPort.save(any(Transaction.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(
                        transactionService.createTransaction("Bitcoin", 200L, transaction, 1L)
                )
                .assertNext(result -> {
                    assertEquals(100L, result.getAssetRefId());
                    assertEquals(1L, result.getPortfolioRefId());
                })
                .verifyComplete();

        verify(portfolioPort).findById(1L);
        verify(securityPort).findById(200L);
        verify(assetPort, times(2)).getAssetForPortfolioBySecurityRefId(1L, 200L, 1L);
        verify(assetPort).createAssetForPortfolio("Bitcoin", 1L, 200L, 1L);
        verify(assetPort).updateAsset(100L, transaction);
        verify(transactionPort).save(transaction);
        verifyNoMoreInteractions(portfolioPort, securityPort, assetPort, transactionPort);
    }

    @Test
    void createTransaction_PortfolioNotFound_ErrorsWithPortfolioNotFoundException() {
        when(portfolioPort.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(
                        transactionService.createTransaction("Test", 200L, transaction, 999L)
                )
                .expectError(PortfolioNotFoundException.class)
                .verify();

        verify(portfolioPort).findById(999L);
        verifyNoInteractions(securityPort, assetPort, transactionPort);
    }

    @Test
    void createTransaction_SecurityNotFound_ErrorsWithSecurityNotFoundException() {
        when(portfolioPort.findById(1L)).thenReturn(Mono.just(portfolio));
        when(securityPort.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(
                        transactionService.createTransaction("Test", 999L, transaction, 1L)
                )
                .expectError(SecurityNotFoundException.class)
                .verify();

        verify(portfolioPort).findById(1L);
        verify(securityPort).findById(999L);
        verifyNoInteractions(assetPort, transactionPort);
    }

    @Test
    void getAllTransactionsForAssetInPortfolio_ReturnsTransactions() {
        Transaction tx1 = new Transaction();
        Transaction tx2 = new Transaction();

        when(transactionPort.findAllByPortfolioIdAndAssetId(1L, 100L))
                .thenReturn(Flux.just(tx1, tx2));

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
        when(transactionPort.findAllByPortfolioIdAndAssetId(1L, 100L))
                .thenReturn(Flux.empty());

        StepVerifier.create(
                        transactionService.getAllTransactionsForAssetInPortfolio(1L, 100L)
                )
                .verifyComplete();

        verify(transactionPort).findAllByPortfolioIdAndAssetId(1L, 100L);
        verifyNoMoreInteractions(transactionPort);
    }
}