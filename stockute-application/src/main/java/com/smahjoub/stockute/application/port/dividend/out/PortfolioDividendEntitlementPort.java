package com.smahjoub.stockute.application.port.dividend.out;

import com.smahjoub.stockute.domain.model.PortfolioDividendEntitlement;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PortfolioDividendEntitlementPort {

    Mono<Void> deleteBySecurityDividendRefId(Long securityDividendRefId);

    Flux<PortfolioDividendEntitlement> saveAll(Publisher<PortfolioDividendEntitlement> entitlements);

    Flux<PortfolioDividendEntitlement> getEntitlementsForPortfolioAsset(final Long portfolioRefId, final Long assetRefId);
}