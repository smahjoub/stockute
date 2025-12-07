package com.smahjoub.stockute.adapters.persistence.asset;

import com.smahjoub.stockute.domain.model.Asset;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AssetRepository extends R2dbcRepository<Asset, Long>  {
    @Query("SELECT * FROM assets WHERE portfolio_ref_id = $1 AND ticker = $2 AND exchange = $3 AND currency_ref_id = $4 LIMIT 1")
    Mono<Asset> findByPortfolioIdAndTickerAndExchange(Long portfolioId, String ticker, String exchange, Long currencyRefId);

    @Query("SELECT * FROM assets WHERE portfolio_ref_id = :portfolioId")
    Flux<Asset> findAllByPortfolio(Long portfolioId);
}
