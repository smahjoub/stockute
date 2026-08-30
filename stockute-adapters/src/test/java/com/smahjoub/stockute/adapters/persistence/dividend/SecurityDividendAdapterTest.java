package com.smahjoub.stockute.adapters.persistence.dividend;

import com.smahjoub.stockute.domain.model.SecurityDividend;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityDividendAdapterTest {

    @Mock
    private SecurityDividendRepository repository;

    @InjectMocks
    private SecurityDividendAdapter adapter;

    @Test
    void findById_WhenFound_ReturnsSecurityDividend() {
        SecurityDividend dividend = new SecurityDividend();
        dividend.setId(10L);

        when(repository.findById(10L)).thenReturn(Mono.just(dividend));

        StepVerifier.create(adapter.findById(10L))
                .expectNext(dividend)
                .verifyComplete();

        verify(repository).findById(10L);
    }

    @Test
    void findById_WhenNotFound_ReturnsEmpty() {
        when(repository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById(999L))
                .verifyComplete();

        verify(repository).findById(999L);
    }

    @Test
    void save_ShouldDelegateToRepository() {
        SecurityDividend dividend = new SecurityDividend();
        dividend.setId(5L);

        when(repository.save(dividend)).thenReturn(Mono.just(dividend));

        StepVerifier.create(adapter.save(dividend))
                .expectNext(dividend)
                .verifyComplete();

        verify(repository).save(dividend);
    }

    @Test
    void findByBusinessKey_ShouldDelegateToRepository() {
        Long securityRefId = 100L;
        LocalDateTime exDate = LocalDateTime.of(2026, 5, 8, 0, 0);
        LocalDateTime paymentDate = LocalDateTime.of(2026, 6, 10, 0, 0);
        BigDecimal amount = new BigDecimal("1.69");

        SecurityDividend dividend = new SecurityDividend();
        dividend.setId(77L);

        when(repository.findByBusinessKey(
                securityRefId, exDate, paymentDate, amount
        )).thenReturn(Mono.just(dividend));

        StepVerifier.create(adapter.findByBusinessKey(securityRefId, exDate, paymentDate, amount))
                .expectNext(dividend)
                .verifyComplete();

        verify(repository).findByBusinessKey(
                securityRefId, exDate, paymentDate, amount
        );
    }
}
