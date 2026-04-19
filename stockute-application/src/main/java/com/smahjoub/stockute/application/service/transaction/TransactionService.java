package com.smahjoub.stockute.application.service.transaction;

import com.smahjoub.stockute.application.exception.PortfolioNotFoundException;
import com.smahjoub.stockute.application.exception.SecurityNotFoundException;
import com.smahjoub.stockute.application.port.asset.out.AssetPort;
import com.smahjoub.stockute.application.port.portfolio.out.PortfolioPort;
import com.smahjoub.stockute.application.port.security.out.SecurityPort;
import com.smahjoub.stockute.application.port.transaction.in.TransactionUseCase;
import com.smahjoub.stockute.application.port.transaction.out.TransactionPort;
import com.smahjoub.stockute.domain.model.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class TransactionService implements TransactionUseCase {
    private final PortfolioPort portfolioPort;
    private final TransactionPort transactionPort;
    private final AssetPort assetPort;
    private final SecurityPort securityPort;

    @Override
    public Mono<Transaction> createTransaction(final String assetName,
                                               final Long securityRefId,
                                               final Transaction transaction,
                                               final Long portfolioId) {
        return portfolioPort.findById(portfolioId)
                .switchIfEmpty(Mono.error(new PortfolioNotFoundException("Portfolio not found")))
                .flatMap(portfolio ->
                        securityPort.findById(securityRefId)
                                .switchIfEmpty(Mono.error(new SecurityNotFoundException("Security not found")))
                                .flatMap(security ->
                                        assetPort.getAssetForPortfolioBySecurityRefId(
                                                        portfolioId,
                                                        securityRefId,
                                                        portfolio.getCurrencyRefId()
                                                )
                                                .switchIfEmpty(Mono.defer(() ->
                                                        assetPort.createAssetForPortfolio(
                                                                        assetName,
                                                                        portfolioId,
                                                                        securityRefId,
                                                                        portfolio.getCurrencyRefId()
                                                                )
                                                                .then(
                                                                        assetPort.getAssetForPortfolioBySecurityRefId(
                                                                                portfolioId,
                                                                                securityRefId,
                                                                                portfolio.getCurrencyRefId()
                                                                        )
                                                                )
                                                ))
                                                .flatMap(asset -> assetPort.updateAsset(asset.getId(), transaction))
                                                .flatMap(asset -> {
                                                    transaction.setAssetRefId(asset.getId());
                                                    transaction.setPortfolioRefId(portfolioId);
                                                    return transactionPort.save(transaction);
                                                })
                                )
                );
    }

    public Flux<Transaction> getAllTransactionsForAssetInPortfolio(final Long portfolioId, final Long asserId) {
        return transactionPort.findAllByPortfolioIdAndAssetId(portfolioId, asserId);
    }
}
