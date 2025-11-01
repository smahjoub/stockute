package com.smahjoub.stockute.application.port.transaction.out;

import com.smahjoub.stockute.domain.model.Transaction;
import reactor.core.publisher.Mono;

public interface TransactionPort {
    Mono<Transaction> save(Transaction transaction);
}
