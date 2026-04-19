package com.smahjoub.stockute.application.service.asset;

import com.smahjoub.stockute.application.port.asset.in.AssetUseCase;
import com.smahjoub.stockute.application.port.asset.out.AssetPort;
import com.smahjoub.stockute.application.port.security.out.SecurityPort;
import com.smahjoub.stockute.application.service.currency.CurrencyService;
import com.smahjoub.stockute.domain.model.Asset;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AssetService implements AssetUseCase {
    private final AssetPort assetPort;
    private final CurrencyService currencyService;
    private final SecurityPort securityPort;
    @Override
    public Flux<Asset> getAllAssetsForPortfolio(Long portfolioId) {
        return assetPort.findAllByPortfolio(portfolioId)
                .flatMap(this::enrichAsset);
    }

    private Mono<Asset> enrichAsset(final Asset asset) {
        Mono<?> currencyMono = currencyService.getCurrencyBYId(asset.getCurrencyRefId())
                .doOnNext(asset::setCurrency);

        Mono<?> securityMono = securityPort.findById(asset.getSecurityRefId())
                .doOnNext(asset::setSecurity);

        return Mono.zip(currencyMono, securityMono)
                .thenReturn(asset);
    }
}
