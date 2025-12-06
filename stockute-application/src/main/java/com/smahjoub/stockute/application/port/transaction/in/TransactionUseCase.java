package com.smahjoub.stockute.application.port.transaction.in;

import com.smahjoub.stockute.domain.model.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionUseCase {
    Mono<Transaction> createTransaction(String assetName, String assetTicker, String exchange, Transaction transaction, Long portfolioId);

    Flux<Transaction> getAllTransactionsForAssetInPortfolio(final Long portfolioId, final Long asserId);
}
