package com.smahjoub.stockute.adapters.persistence.asset;

import com.smahjoub.stockute.application.port.asset.out.AssetPort;
import com.smahjoub.stockute.domain.model.Asset;
import com.smahjoub.stockute.domain.model.Transaction;
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
        asset.setAveragePrice(BigDecimal.ZERO);
        return assetRepository.save(asset);
    }

    @Override
    public Mono<Asset> updateAsset(final Long assetId, final Transaction transaction) {
        return assetRepository.findById(assetId)
                .flatMap(asset -> {
                    final BigDecimal currentPrice = asset.getAveragePrice() == null ? BigDecimal.ZERO : asset.getAveragePrice();
                    final BigDecimal currentQtyBD = BigDecimal.valueOf(asset.getQuantity());
                    final BigDecimal addedQtyBD = BigDecimal.valueOf(transaction.getQuantity());
                    final BigDecimal newQtyBD = "BUY".equals(transaction.getType()) ? currentQtyBD.add(addedQtyBD) : currentQtyBD.subtract(addedQtyBD);

                    if (newQtyBD.compareTo(BigDecimal.ZERO) <= 0) {
                        // If quantity becomes zero or negative, set price to zero and update quantity
                        asset.setQuantity(newQtyBD.doubleValue());
                        asset.setAveragePrice(BigDecimal.ZERO);
                    } else {
                        final BigDecimal addedTotal = (transaction.getPrice() == null ? BigDecimal.ZERO : transaction.getPrice()).multiply(addedQtyBD);
                        final BigDecimal currentTotal = currentPrice.multiply(currentQtyBD);
                        final BigDecimal newAvgPrice = currentTotal.add(addedTotal)
                                .divide(newQtyBD, 8, java.math.RoundingMode.HALF_UP);
                        asset.setQuantity(newQtyBD.doubleValue());
                        asset.setAveragePrice(newAvgPrice);
                    }
                    final BigDecimal fees = transaction.getFees() != null ? transaction.getFees() : BigDecimal.ZERO;
                    asset.setAccumulatedFees(fees.add(transaction.getFees()));
                    if("BUY".equals(transaction.getType())){
                        final BigDecimal totalInvested = (asset.getTotalAmountInvested() != null) ? asset.getTotalAmountInvested() : BigDecimal.ZERO;
                        asset.setTotalAmountInvested(totalInvested.add(transaction.getPrice().multiply(addedQtyBD)));
                    }

                    if("SELL".equals(transaction.getType())){
                        final BigDecimal totalGainLost = (asset.getTotalGainLoss() != null) ? asset.getTotalGainLoss() : BigDecimal.ZERO;
                        asset.setTotalGainLoss(totalGainLost.add(transaction.getPrice().multiply(addedQtyBD)));
                    }

                    return assetRepository.save(asset);
        });
    }
}
