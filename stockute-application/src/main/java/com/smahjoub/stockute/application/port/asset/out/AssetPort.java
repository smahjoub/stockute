package com.smahjoub.stockute.application.port.asset.out;

import com.smahjoub.stockute.domain.model.Asset;
import com.smahjoub.stockute.domain.model.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface AssetPort {

    Flux<Asset> findAllByPortfolio(final Long portfolioId);

    Mono<Asset> getAssetForPortfolioBySecurityRefId(final Long portfolioId, final Long securityRefId, final Long currencyRefId);

    Mono<Asset> createAssetForPortfolio(String name, final Long portfolioId, final Long securityRefId, Long currencyRefId);

    Mono<Asset> updateAsset(Long assetId, Transaction transaction);
}
