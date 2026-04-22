package com.smahjoub.stockute.adapters.persistence.dividend;

import com.smahjoub.stockute.domain.model.SecurityDividend;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
}