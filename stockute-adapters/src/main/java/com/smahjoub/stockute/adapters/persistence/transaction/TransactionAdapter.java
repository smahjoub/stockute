package com.smahjoub.stockute.adapters.persistence.transaction;

import com.smahjoub.stockute.application.port.transaction.out.TransactionPort;
import com.smahjoub.stockute.domain.model.Transaction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TransactionAdapter implements TransactionPort {
    private final TransactionRepository transactionRepository;
    @Override
    public Mono<Transaction> save(final Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Flux<Transaction> findAllByPortfolioIdAndAssetId(Long portfolioId, Long assetId) {
        return transactionRepository.findAllByPortfolioIdAndAssetId(portfolioId, assetId);
    }
}
