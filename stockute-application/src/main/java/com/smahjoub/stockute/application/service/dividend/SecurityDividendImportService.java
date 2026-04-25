package com.smahjoub.stockute.application.service.dividend;

import com.smahjoub.stockute.application.port.dividend.in.PortfolioDividendEntitlementUseCase;
import com.smahjoub.stockute.application.port.dividend.out.DividendMarketDataPort;
import com.smahjoub.stockute.application.port.security.out.SecurityPort;
import com.smahjoub.stockute.domain.model.Security;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SecurityDividendImportService {

    private final SecurityPort securityPort;
    private final DividendMarketDataPort dividendMarketDataPort;
    private final SecurityDividendUpsertService securityDividendUpsertService;
    private final PortfolioDividendEntitlementUseCase portfolioDividendEntitlementService;

    public Mono<Void> importDividendHistoryForAllSecurities() {
        return securityPort.findAll()
                .filter(this::isEligibleForDividendImport)
                .concatMap(this::importDividendHistoryForSecurity)
                .then();
    }

    private Mono<Void> importDividendHistoryForSecurity(final Security security) {
        return dividendMarketDataPort.getDividendHistory(security.getSymbol())
                .concatMap(item ->
                        securityDividendUpsertService.upsert(security, item)
                                .flatMap(savedDividend ->
                                        portfolioDividendEntitlementService
                                                .rebuildEntitlementsForSecurityDividend(savedDividend.getId())
                                )
                )
                .then()
                .onErrorResume(error -> {
                    log.warn("Dividend import failed for securityId={} symbol={}",
                            security.getId(),
                            security.getSymbol(),
                            error);
                    return Mono.empty();
                });
    }

    private boolean isEligibleForDividendImport(final Security security) {
        return security.getSymbol() != null && !security.getSymbol().isBlank();
    }
}