package com.smahjoub.stockute.adapters.persistence.asset;

import com.smahjoub.stockute.application.port.asset.out.AssetPort;
import com.smahjoub.stockute.domain.model.Asset;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AssetAdapter implements AssetPort {

    private final AssetRepository assetRepository;

    @Override
    public Flux<Asset> findAllByPortfolio(final Long portfolioId) {
        return assetRepository.findAllByPortfolio(portfolioId);
    }

    @Override
    public Mono<Asset> getAssetForPortfolio(final Long portfolioId, final String ticker, final String exchange, final Long currencyRefId) {
        return assetRepository.findByPortfolioIdAndTickerAndExchange(portfolioId, ticker, exchange, currencyRefId);
    }

    @Override
    public Mono<Asset> createAssetForPortfolio(final String name, final Long portfolioId, final String ticker, final String exchange, final Long currencyRefId) {
        final Asset asset = new Asset();
        asset.setName(name);
        asset.setPortfolioRefId(portfolioId);
        asset.setTicker(ticker);
        asset.setExchange(exchange);
        asset.setCurrencyRefId(currencyRefId);
        asset.setQuantity(0.0d);
        asset.setPrice(BigDecimal.ZERO);
        return assetRepository.save(asset);
    }

    @Override
    public Mono<Asset> updateAsset(final Long assetId, final double quantity, final BigDecimal price) {
        return assetRepository.findById(assetId)
                .flatMap(asset -> {
                    final BigDecimal currentPrice = asset.getPrice() == null ? BigDecimal.ZERO : asset.getPrice();
                    final BigDecimal currentQtyBD = BigDecimal.valueOf(asset.getQuantity());
                    final BigDecimal addedQtyBD = BigDecimal.valueOf(quantity);
                    final BigDecimal newQtyBD = currentQtyBD.add(addedQtyBD);

                    if (newQtyBD.compareTo(BigDecimal.ZERO) <= 0) {
                        // If quantity becomes zero or negative, set price to zero and update quantity
                        asset.setQuantity(newQtyBD.doubleValue());
                        asset.setPrice(BigDecimal.ZERO);
                    } else {
                        final BigDecimal addedTotal = (price == null ? BigDecimal.ZERO : price).multiply(addedQtyBD);
                        final BigDecimal currentTotal = currentPrice.multiply(currentQtyBD);
                        final BigDecimal newAvgPrice = currentTotal.add(addedTotal)
                                .divide(newQtyBD, 8, java.math.RoundingMode.HALF_UP);
                        asset.setQuantity(newQtyBD.doubleValue());
                        asset.setPrice(newAvgPrice);
                    }

                    return assetRepository.save(asset);
        });
    }
}
