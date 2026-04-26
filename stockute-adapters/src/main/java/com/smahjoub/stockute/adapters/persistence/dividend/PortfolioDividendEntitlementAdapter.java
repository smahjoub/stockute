package com.smahjoub.stockute.adapters.persistence.dividend;

import com.smahjoub.stockute.application.port.dividend.out.PortfolioDividendEntitlementPort;
import com.smahjoub.stockute.domain.model.PortfolioDividendEntitlement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PortfolioDividendEntitlementAdapter implements PortfolioDividendEntitlementPort {

    private final PortfolioDividendEntitlementRepository repository;

    @Override
    public Mono<Void> deleteBySecurityDividendRefId(final Long securityDividendRefId) {
        return repository.deleteBySecurityDividendRefId(securityDividendRefId);
    }

    @Override
    public Flux<PortfolioDividendEntitlement> saveAll(
            final Publisher<PortfolioDividendEntitlement> entitlements
    ) {
        return repository.saveAll(entitlements);
    }

    @Override
    public Flux<PortfolioDividendEntitlement> getEntitlementsForPortfolioAsset(final Long portfolioRefId, final Long assetRefId) {
        return repository.findByPortfolioRefIdAndAssetRefId(portfolioRefId, assetRefId);
    }

    @Override
    public Flux<PortfolioDividendEntitlement> findAllByPortfolioRefIdAndPaymentDateBetween(
            final Long portfolioRefId,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        return repository.findAllByPortfolioRefIdAndPaymentDateBetween(portfolioRefId, startDate, endDate);
    }
}