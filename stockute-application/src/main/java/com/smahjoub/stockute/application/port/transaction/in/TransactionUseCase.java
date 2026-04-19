package com.smahjoub.stockute.application.port.transaction.in;

import com.smahjoub.stockute.domain.model.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionUseCase {

    Mono<Transaction> createTransaction(String assetName,
                                        Long securityRefId,
                                        Transaction transaction,
                                        Long portfolioId);

    Flux<Transaction> getAllTransactionsForAssetInPortfolio(Long portfolioId, Long assetId);
}