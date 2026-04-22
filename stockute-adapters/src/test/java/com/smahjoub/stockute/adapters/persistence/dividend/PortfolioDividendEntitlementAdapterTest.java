package com.smahjoub.stockute.adapters.persistence.dividend;

import com.smahjoub.stockute.domain.model.PortfolioDividendEntitlement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PortfolioDividendEntitlementAdapterTest {

    @Mock
    private PortfolioDividendEntitlementRepository repository;

    @InjectMocks
    private PortfolioDividendEntitlementAdapter adapter;

    @Test
    void deleteBySecurityDividendRefId_delegatesToRepository() {
        when(repository.deleteBySecurityDividendRefId(10L)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteBySecurityDividendRefId(10L))
                .verifyComplete();

        verify(repository).deleteBySecurityDividendRefId(10L);
    }

    @Test
    void saveAll_delegatesToRepository() {
        PortfolioDividendEntitlement entitlement1 = new PortfolioDividendEntitlement();
        entitlement1.setId(1L);
        entitlement1.setGrossAmount(BigDecimal.valueOf(15.00));

        PortfolioDividendEntitlement entitlement2 = new PortfolioDividendEntitlement();
        entitlement2.setId(2L);
        entitlement2.setGrossAmount(BigDecimal.valueOf(7.50));

        when(repository.saveAll(any(Publisher.class)))
                .thenReturn(Flux.just(entitlement1, entitlement2));

        Flux<PortfolioDividendEntitlement> input = Flux.just(entitlement1, entitlement2);

        StepVerifier.create(adapter.saveAll(input))
                .expectNext(entitlement1, entitlement2)
                .verifyComplete();

        verify(repository).saveAll(any(Publisher.class));
    }
}