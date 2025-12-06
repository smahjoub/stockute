package com.smahjoub.stockute.application.port.transaction.out;

import com.smahjoub.stockute.domain.model.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionPort {
    Mono<Transaction> save(Transaction transaction);

    Flux<Transaction> findAllByPortfolioIdAndAssetId(Long portfolioId, Long assetId);
}
