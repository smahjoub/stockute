package com.smahjoub.stockute.adapters.persistence.transaction;

import com.smahjoub.stockute.domain.model.Transaction;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends R2dbcRepository<Transaction, Long> {
}
