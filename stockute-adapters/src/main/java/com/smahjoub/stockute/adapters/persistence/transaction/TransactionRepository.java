package com.smahjoub.stockute.adapters.persistence.transaction;

import com.smahjoub.stockute.domain.model.Transaction;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TransactionRepository extends R2dbcRepository<Transaction, Long> {

    /**
     * Finds all transactions for a specific asset within a given portfolio using a native SQL query.
     *
     * @param portfolioId The ID of the portfolio.
     * @param assetId     The ID of the asset.
     * @return A Flux of transactions matching the criteria.
     */
    @Query("SELECT * FROM transactions WHERE portfolio_ref_id = :portfolioId AND asset_ref_id = :assetId")
    Flux<Transaction> findAllByPortfolioIdAndAssetId(Long portfolioId, Long assetId);
}
