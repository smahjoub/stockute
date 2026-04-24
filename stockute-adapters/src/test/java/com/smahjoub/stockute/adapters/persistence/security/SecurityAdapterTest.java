package com.smahjoub.stockute.adapters.persistence.security;

import com.smahjoub.stockute.domain.model.Security;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SecurityAdapterTest {

    private SecurityRepository repository;
    private SecurityAdapter adapter;

    @BeforeEach
    void setup() {
        repository = mock(SecurityRepository.class);
        adapter = new SecurityAdapter(repository);
    }

    @Test
    void searchBySymbolOrName_shouldMergeAndDistinctBySymbol() {
        // GIVEN
        String keyword = "app";

        Security s1 = new Security();
        s1.setSymbol("AAPL");
        s1.setName("Apple Inc");

        Security s2 = new Security();
        s2.setSymbol("AAPL"); // duplicate symbol
        s2.setName("Apple Incorporated");

        Security s3 = new Security();
        s3.setSymbol("APPS");
        s3.setName("Digital Turbine");

        when(repository.findBySymbolContainingIgnoreCase(keyword))
                .thenReturn(Flux.just(s1, s2));

        when(repository.findByNameContainingIgnoreCase(keyword))
                .thenReturn(Flux.just(s3));

        // WHEN
        StepVerifier.create(adapter.searchBySymbolOrName(keyword))
                .expectNextMatches(sec -> sec.getSymbol().equals("AAPL"))
                .expectNextMatches(sec -> sec.getSymbol().equals("APPS"))
                .verifyComplete();

        // THEN
        verify(repository).findBySymbolContainingIgnoreCase(keyword);
        verify(repository).findByNameContainingIgnoreCase(keyword);
    }

    @Test
    void findBySymbol_shouldDelegateToRepository() {
        Security s = new Security();
        s.setSymbol("AAPL");

        when(repository.findBySymbol("AAPL"))
                .thenReturn(Mono.just(s));

        StepVerifier.create(adapter.findBySymbol("AAPL"))
                .assertNext(sec -> assertThat(sec.getSymbol()).isEqualTo("AAPL"))
                .verifyComplete();

        verify(repository).findBySymbol("AAPL");
    }

    @Test
    void save_shouldDelegateToRepository() {
        Security s = new Security();
        s.setSymbol("AAPL");

        when(repository.save(s)).thenReturn(Mono.just(s));

        StepVerifier.create(adapter.save(s))
                .expectNext(s)
                .verifyComplete();

        verify(repository).save(s);
    }

    @Test
    void saveAll_shouldDelegateToRepository() {
        Security s1 = new Security();
        Security s2 = new Security();

        Flux<Security> input = Flux.just(s1, s2);

        when(repository.saveAll(input)).thenReturn(input);

        StepVerifier.create(adapter.saveAll(input))
                .expectNext(s1)
                .expectNext(s2)
                .verifyComplete();

        verify(repository).saveAll(input);
    }


    @Test
    void findById_shouldReturnSecurity() {
        Security s = new Security();
        s.setId(42L);

        when(repository.findById(42L)).thenReturn(Mono.just(s));

        StepVerifier.create(adapter.findById(42L))
                .expectNext(s)
                .verifyComplete();

        verify(repository).findById(42L);
    }

    @Test
    void findById_whenNotFound_shouldReturnEmpty() {
        when(repository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById(999L))
                .verifyComplete();

        verify(repository).findById(999L);
    }


    @Test
    void findAll_shouldReturnAllSecurities() {
        Security s1 = new Security();
        s1.setId(1L);

        Security s2 = new Security();
        s2.setId(2L);

        when(repository.findAll()).thenReturn(Flux.just(s1, s2));

        StepVerifier.create(adapter.findAll())
                .expectNext(s1)
                .expectNext(s2);

    }

}
