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
class AssetControllerPureUnitTest {

    @Mock
    private AssetService assetService;

    @Mock
    private AssetMapper assetMapper;

    @InjectMocks
    private AssetController assetController;

    @Test
    void getAllAssetsForPortfolio_returnsAssetDtoFlux() {
        // given
        long portfolioId = 1L;
        Currency usd = new Currency(1L, "Dollar", "$", "USD");
        Asset asset1 = new Asset(10L, "AAPL", "NASDAQ", "Apple Inc.", 5.0,
                new BigDecimal("150.00"), portfolioId, 1L, usd);
        Asset asset2 = new Asset(11L, "MSFT", "NASDAQ", "Microsoft Corp.", 3.0,
                new BigDecimal("300.00"), portfolioId, 1L, usd);

        AssetDTO dto1 = new AssetDTO(10L, "AAPL", "NASDAQ", "Apple Inc.", 5.0,
                new BigDecimal("150.00"), 1L, "USD");
        AssetDTO dto2 = new AssetDTO(11L, "MSFT", "NASDAQ", "Microsoft Corp.", 3.0,
                new BigDecimal("300.00"), 1L, "USD");

        when(assetService.getAllAssetsForPortfolio(portfolioId))
                .thenReturn(Flux.just(asset1, asset2));
        when(assetMapper.toAssetDTO(asset1)).thenReturn(dto1);
        when(assetMapper.toAssetDTO(asset2)).thenReturn(dto2);

        // when
        Flux<AssetDTO> result = assetController.getAllAssetsForPortfolio(portfolioId);

        // then
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
        // given
        long portfolioId = 999L;
        when(assetService.getAllAssetsForPortfolio(portfolioId))
                .thenReturn(Flux.empty());

        // when
        Flux<AssetDTO> result = assetController.getAllAssetsForPortfolio(portfolioId);

        // then
        StepVerifier.create(result)
                .verifyComplete();

        verify(assetService).getAllAssetsForPortfolio(portfolioId);
        verifyNoInteractions(assetMapper);
    }

    @Test
    void getAllAssetsForPortfolio_serviceThrowsError_propagatesError() {
        // given
        long portfolioId = 1L;
        RuntimeException exception = new RuntimeException("Service error");
        when(assetService.getAllAssetsForPortfolio(portfolioId))
                .thenReturn(Flux.error(exception));

        // when & then
        Flux<AssetDTO> result = assetController.getAllAssetsForPortfolio(portfolioId);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable == exception)
                .verify();

        verify(assetService).getAllAssetsForPortfolio(portfolioId);
        verifyNoInteractions(assetMapper);
    }
}
