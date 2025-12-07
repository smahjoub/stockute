package com.smahjoub.stockute.application.service.asset;

import com.smahjoub.stockute.application.port.asset.in.AssetUseCase;
import com.smahjoub.stockute.application.port.asset.out.AssetPort;
import com.smahjoub.stockute.application.service.currency.CurrencyService;
import com.smahjoub.stockute.domain.model.Asset;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
@Service
@AllArgsConstructor
public class AssetService implements AssetUseCase {
    private final AssetPort assetPort;
    private final CurrencyService currencyService;

    @Override
    public Flux<Asset> getAllAssetsForPortfolio(Long portfolioId) {
        return assetPort.findAllByPortfolio(portfolioId)
                .flatMap(asset ->
                        currencyService.getCurrencyBYId(asset.getCurrencyRefId())
                                .doOnNext(asset::setCurrency)
                                .thenReturn(asset)
                );
    }
}
