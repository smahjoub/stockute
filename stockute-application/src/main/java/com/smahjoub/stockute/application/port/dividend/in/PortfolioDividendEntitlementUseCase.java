package com.smahjoub.stockute.application.port.dividend.in;

import com.smahjoub.stockute.domain.model.PortfolioDividendEntitlement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PortfolioDividendEntitlementUseCase {


    Mono<Void> rebuildEntitlementsForSecurityDividend(final Long securityDividendId);

    Flux<PortfolioDividendEntitlement> getDividendEntitlementsForPortfolio(final Long portfolioRefId, final Long assetRefId);
}
