package com.smahjoub.stockute.application.service.security;

import com.smahjoub.stockute.application.port.currency.out.CurrencyPort;
import com.smahjoub.stockute.application.port.security.in.SearchSecurityUseCase;
import com.smahjoub.stockute.application.port.security.out.SecurityPort;
import com.smahjoub.stockute.application.port.security.out.SecuritySearchPort;
import com.smahjoub.stockute.domain.model.Security;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SecurityService implements SearchSecurityUseCase {

    private final SecurityPort securityPort;
    private final SecuritySearchPort securitySearchPort;
    private final CurrencyPort currencyPort;

    @Override
    public Flux<Security> search(final String tickerSymbol) {
        if (tickerSymbol == null || tickerSymbol.isBlank()) {
            return Flux.empty();
        }

        return securityPort.searchBySymbolOrName(tickerSymbol)
                .collectList()
                .flatMapMany(localResults -> {
                    if (!localResults.isEmpty()) {
                        return Flux.fromIterable(localResults)
                                .flatMap(this::enrichCurrencyCodeFromRefId);
                    }

                    return securitySearchPort.search(tickerSymbol)
                            .flatMap(this::enrichCurrencyRefId)
                            .collectList()
                            .flatMapMany(externalResults -> {
                                if (externalResults.isEmpty()) {
                                    return Flux.empty();
                                }

                                return securityPort.saveAll(Flux.fromIterable(externalResults).map(result -> {
                                    final LocalDateTime now = LocalDateTime.now();
                                    result.setCreatedDate(now);
                                    result.setLastModifiedDate(now);
                                    result.setVersion(0L);
                                    return result;
                                }));
                            });
                });
    }

    private Mono<Security> enrichCurrencyRefId(Security security) {
        if (security.getCurrency() == null || security.getCurrency().isBlank()) {
            return Mono.just(security);
        }

        return currencyPort.findByCode(security.getCurrency())
                .map(currency -> {
                    security.setCurrencyRefId(currency.getId());
                    return security;
                })
                .defaultIfEmpty(security);
    }

    private Mono<Security> enrichCurrencyCodeFromRefId(Security security) {
        if (security.getCurrencyRefId() == null) {
            return Mono.just(security);
        }

        return currencyPort.findById(security.getCurrencyRefId())
                .map(currency -> {
                    security.setCurrency(currency.getCode());
                    return security;
                })
                .defaultIfEmpty(security);
    }
}