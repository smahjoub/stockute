package com.smahjoub.stockute.application.port.asset.in;

import com.smahjoub.stockute.domain.model.Asset;
import reactor.core.publisher.Flux;

public interface AssetUseCase {

    Flux<Asset> getAllAssetsForPortfolio(final Long portfolioId);
}
