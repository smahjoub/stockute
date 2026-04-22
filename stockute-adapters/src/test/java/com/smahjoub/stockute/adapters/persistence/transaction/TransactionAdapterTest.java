package com.smahjoub.stockute.adapters.persistence.transaction;

import com.smahjoub.stockute.domain.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionAdapterTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionAdapter transactionAdapter;

    @Test
    void save_ReturnsSavedTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setType("BUY");
        transaction.setQuantity(BigDecimal.valueOf(10));

        when(transactionRepository.save(transaction)).thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionAdapter.save(transaction))
                .expectNext(transaction)
                .verifyComplete();

        verify(transactionRepository).save(transaction);
    }

    @Test
    void findAllByPortfolioIdAndAssetId_ReturnsTransactions() {
        Transaction tx1 = new Transaction();
        tx1.setId(1L);

        Transaction tx2 = new Transaction();
        tx2.setId(2L);

        when(transactionRepository.findAllByPortfolioIdAndAssetId(1L, 100L))
                .thenReturn(Flux.just(tx1, tx2));

        StepVerifier.create(transactionAdapter.findAllByPortfolioIdAndAssetId(1L, 100L))
                .expectNext(tx1, tx2)
                .verifyComplete();

        verify(transactionRepository).findAllByPortfolioIdAndAssetId(1L, 100L);
    }

    @Test
    void findAllByAssetRefIdAndTransactionDateLessThanEqual_ReturnsTransactions() {
        LocalDateTime date = LocalDateTime.of(2026, 4, 1, 0, 0);

        Transaction tx1 = new Transaction();
        tx1.setId(1L);
        tx1.setTransactionDate(LocalDateTime.of(2026, 3, 1, 0, 0));

        Transaction tx2 = new Transaction();
        tx2.setId(2L);
        tx2.setTransactionDate(LocalDateTime.of(2026, 3, 15, 0, 0));

        when(transactionRepository.findAllByAssetRefIdAndTransactionDateLessThanEqual(100L, date))
                .thenReturn(Flux.just(tx1, tx2));

        StepVerifier.create(transactionAdapter.findAllByAssetRefIdAndTransactionDateLessThanEqual(100L, date))
                .expectNext(tx1, tx2)
                .verifyComplete();

        verify(transactionRepository).findAllByAssetRefIdAndTransactionDateLessThanEqual(100L, date);
    }
}