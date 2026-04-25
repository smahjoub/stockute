package com.smahjoub.stockute.adapters.persistence.dividend;

import com.smahjoub.stockute.domain.model.PortfolioDividendEntitlement;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PortfolioDividendEntitlementRepository
        extends ReactiveCrudRepository<PortfolioDividendEntitlement, Long> {

    Mono<Void> deleteBySecurityDividendRefId(Long securityDividendRefId);

    Flux<PortfolioDividendEntitlement> findByPortfolioRefIdAndAssetRefId(Long portfolioRefId, Long assetRefId);
}