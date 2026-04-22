package com.smahjoub.stockute.application.service.dividend;

import com.smahjoub.stockute.application.port.asset.out.AssetPort;
import com.smahjoub.stockute.application.port.dividend.out.PortfolioDividendEntitlementPort;
import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendPort;
import com.smahjoub.stockute.application.port.transaction.out.TransactionPort;
import com.smahjoub.stockute.domain.model.Asset;
import com.smahjoub.stockute.domain.model.PortfolioDividendEntitlement;
import com.smahjoub.stockute.domain.model.SecurityDividend;
import com.smahjoub.stockute.domain.model.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PortfolioDividendEntitlementService {

    private final SecurityDividendPort securityDividendPort;
    private final PortfolioDividendEntitlementPort portfolioDividendEntitlementPort;
    private final AssetPort assetPort;
    private final TransactionPort transactionPort;

    public Mono<Void> rebuildEntitlementsForSecurityDividend(final Long securityDividendId) {
        return securityDividendPort.findById(securityDividendId)
                .flatMap(this::rebuildEntitlementsForDividend)
                .then();
    }

    private Mono<Void> rebuildEntitlementsForDividend(final SecurityDividend dividend) {
        return portfolioDividendEntitlementPort.deleteBySecurityDividendRefId(dividend.getId())
                .thenMany(assetPort.findAllBySecurityRefId(dividend.getSecurityRefId()))
                .flatMap(asset ->
                        getQuantityHeldOnDate(asset.getId(), dividend.getExDate())
                                .filter(quantity -> quantity.compareTo(BigDecimal.ZERO) > 0)
                                .map(quantity -> toEntitlement(asset, dividend, quantity))
                )
                .as(portfolioDividendEntitlementPort::saveAll)
                .then();
    }

    private Mono<BigDecimal> getQuantityHeldOnDate(final Long assetId, final LocalDateTime date) {
        return transactionPort.findAllByAssetRefIdAndTransactionDateLessThanEqual(assetId, date)
                .map(this::toSignedQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal toSignedQuantity(final Transaction transaction) {
        if (transaction.getQuantity() == null) {
            return BigDecimal.ZERO;
        }

        return switch (transaction.getType()) {
            case "BUY" -> transaction.getQuantity();
            case "SELL" -> transaction.getQuantity().negate();
            default -> BigDecimal.ZERO;
        };
    }

    private PortfolioDividendEntitlement toEntitlement(
            final Asset asset,
            final SecurityDividend dividend,
            final BigDecimal eligibleShares
    ) {
        PortfolioDividendEntitlement entitlement = new PortfolioDividendEntitlement();
        entitlement.setPortfolioRefId(asset.getPortfolioRefId());
        entitlement.setAssetRefId(asset.getId());
        entitlement.setSecurityDividendRefId(dividend.getId());
        entitlement.setSecurityRefId(dividend.getSecurityRefId());
        entitlement.setEligibleShares(eligibleShares);
        entitlement.setDividendPerShare(dividend.getDividendPerShare());
        entitlement.setGrossAmount(eligibleShares.multiply(dividend.getDividendPerShare()));
        entitlement.setCurrencyRefId(dividend.getCurrencyRefId());
        entitlement.setExDate(dividend.getExDate());
        entitlement.setPaymentDate(dividend.getPaymentDate());
        entitlement.setStatus("CALCULATED");
        entitlement.setCreatedDate(LocalDateTime.now());
        entitlement.setLastModifiedDate(LocalDateTime.now());
        entitlement.setVersion(0L);
        return entitlement;
    }
}