package com.smahjoub.stockute.adapters.restful.asset.controller;

import com.smahjoub.stockute.adapters.restful.asset.dto.AssetDTO;
import com.smahjoub.stockute.adapters.restful.asset.mapper.AssetMapper;
import com.smahjoub.stockute.application.service.asset.AssetService;
import com.smahjoub.stockute.domain.model.Asset;
import com.smahjoub.stockute.domain.model.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetControllerTest {

    @Mock
    private AssetService assetService;

    @Mock
    private AssetMapper assetMapper;

    @InjectMocks
    private AssetController assetController;

    @Test
    void getAllAssetsForPortfolio_returnsAssetDtoFlux() {
        long portfolioId = 1L;

        Currency usd = new Currency(1L, "Dollar", "$", "USD");

        Asset asset1 = new Asset(
                10L,
                "Apple Inc.",
                5.0,
                new BigDecimal("150.00"),
                portfolioId,
                1L,
                1000L,
                usd,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                null
        );

        Asset asset2 = new Asset(
                11L,
                "Microsoft Corp.",
                3.0,
                new BigDecimal("300.00"),
                portfolioId,
                1L,
                1001L,
                usd,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                null
        );

        AssetDTO dto1 = new AssetDTO(
                10L,
                "AAPL",
                "United States",
                "Apple Inc.",
                5.0,
                new BigDecimal("150.00"),
                1L,
                "USD",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        AssetDTO dto2 = new AssetDTO(
                11L,
                "MSFT",
                "United States",
                "Microsoft Corp.",
                3.0,
                new BigDecimal("300.00"),
                1L,
                "USD",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        when(assetService.getAllAssetsForPortfolio(portfolioId))
                .thenReturn(Flux.just(asset1, asset2));
        when(assetMapper.toAssetDTO(asset1)).thenReturn(dto1);
        when(assetMapper.toAssetDTO(asset2)).thenReturn(dto2);

        Flux<AssetDTO> result = assetController.getAllAssetsForPortfolio(portfolioId);

        StepVerifier.create(result)
                .expectNext(dto1)
                .expectNext(dto2)
                .verifyComplete();

        verify(assetService).getAllAssetsForPortfolio(portfolioId);
        verify(assetMapper).toAssetDTO(asset1);
        verify(assetMapper).toAssetDTO(asset2);
    }

    @Test
    void getAllAssetsForPortfolio_serviceReturnsEmpty_returnsEmptyFlux() {
        long portfolioId = 999L;

        when(assetService.getAllAssetsForPortfolio(portfolioId))
                .thenReturn(Flux.empty());

        Flux<AssetDTO> result = assetController.getAllAssetsForPortfolio(portfolioId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(assetService).getAllAssetsForPortfolio(portfolioId);
        verifyNoInteractions(assetMapper);
    }

    @Test
    void getAllAssetsForPortfolio_serviceThrowsError_propagatesError() {
        long portfolioId = 1L;
        RuntimeException exception = new RuntimeException("Service error");

        when(assetService.getAllAssetsForPortfolio(portfolioId))
                .thenReturn(Flux.error(exception));

        Flux<AssetDTO> result = assetController.getAllAssetsForPortfolio(portfolioId);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable == exception)
                .verify();

        verify(assetService).getAllAssetsForPortfolio(portfolioId);
        verifyNoInteractions(assetMapper);
    }
}