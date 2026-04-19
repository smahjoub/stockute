package com.smahjoub.stockute.adapters.persistence.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import com.smahjoub.stockute.application.port.security.out.SecurityPort;
import com.smahjoub.stockute.domain.model.Security;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SecurityAdapter implements SecurityPort {

    private final SecurityRepository securityRepository;

    @Override
    public Flux<Security> searchBySymbolOrName(String keyword) {
        return Flux.merge(
                        securityRepository.findBySymbolContainingIgnoreCase(keyword),
                        securityRepository.findByNameContainingIgnoreCase(keyword)
                )
                .distinct(Security::getSymbol);
    }

    @Override
    public Mono<Security> findBySymbol(String symbol) {
        return securityRepository.findBySymbol(symbol);
    }

    @Override
    public Mono<Security> save(Security security) {
        return securityRepository.save(security);
    }

    @Override
    public Flux<Security> saveAll(Flux<Security> securities) {
        return securityRepository.saveAll(securities);
    }
}