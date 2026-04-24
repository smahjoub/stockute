package com.smahjoub.stockute.adapters.persistence.portfolio;

import com.smahjoub.stockute.domain.model.Portfolio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PortfolioAdapterTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @InjectMocks
    private PortfolioAdapter adapter;

    private Portfolio portfolio;

    @BeforeEach
    void setup() {
        portfolio = new Portfolio();
        portfolio.setId(1L);
        portfolio.setName("My Portfolio");
        portfolio.setNotes("Notes");
        portfolio.setCurrencyRefId(10L);
        portfolio.setUserRefId(99L);
    }

    @Test
    void save_ShouldDelegateToRepository() {
        when(portfolioRepository.save(any(Portfolio.class)))
                .thenReturn(Mono.just(portfolio));

        StepVerifier.create(adapter.save(portfolio))
                .expectNext(portfolio)
                .verifyComplete();

        verify(portfolioRepository).save(portfolio);
    }


    @Test
    void findById_WhenFound_ReturnsPortfolio() {
        when(portfolioRepository.findById(1L))
                .thenReturn(Mono.just(portfolio));

        StepVerifier.create(adapter.findById(1L))
                .expectNext(portfolio)
                .verifyComplete();

        verify(portfolioRepository).findById(1L);
    }

    @Test
    void findById_WhenNotFound_ReturnsEmpty() {
        when(portfolioRepository.findById(anyLong()))
                .thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById(999L))
                .verifyComplete();

        verify(portfolioRepository).findById(999L);
    }


    @Test
    void findAllByUserRefId_ShouldReturnPortfolios() {
        Portfolio p1 = new Portfolio();
        p1.setId(1L);
        p1.setUserRefId(99L);

        Portfolio p2 = new Portfolio();
        p2.setId(2L);
        p2.setUserRefId(99L);

        when(portfolioRepository.findAllByUserRefId(99L))
                .thenReturn(Flux.just(p1, p2));

        StepVerifier.create(adapter.findAllByUserRefId(99L))
                .expectNext(p1)
                .expectNext(p2)
                .verifyComplete();

        verify(portfolioRepository).findAllByUserRefId(99L);
    }

    @Test
    void deleteById_ShouldDelegateToRepository() {
        when(portfolioRepository.deleteById(1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteById(1L))
                .verifyComplete();

        verify(portfolioRepository).deleteById(1L);
    }
}
