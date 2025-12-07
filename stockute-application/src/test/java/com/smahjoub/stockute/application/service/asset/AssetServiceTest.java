package com.smahjoub.stockute.application.service.asset;

import com.smahjoub.stockute.application.port.asset.out.AssetPort;
import com.smahjoub.stockute.application.service.currency.CurrencyService;
import com.smahjoub.stockute.domain.model.Asset;
import com.smahjoub.stockute.domain.model.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetPort assetPort;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private AssetService assetService;

    private Currency currency;

    @BeforeEach
    void setUp() {
        currency = new Currency();
        currency.setId(1L);
        currency.setCode("USD");
    }

    @Test
    void getAllAssetsForPortfolio_enrichesAssetsWithCurrency() {
        // given
        Long portfolioId = 1L;

        Asset asset1 = new Asset();
        asset1.setId(10L);
        asset1.setCurrencyRefId(1L);

        Asset asset2 = new Asset();
        asset2.setId(11L);
        asset2.setCurrencyRefId(1L);

        when(assetPort.findAllByPortfolio(portfolioId))
                .thenReturn(Flux.just(asset1, asset2));
        when(currencyService.getCurrencyBYId(1L))
                .thenReturn(Mono.just(currency));

        // when
        Flux<Asset> result = assetService.getAllAssetsForPortfolio(portfolioId);

        // then
        StepVerifier.create(result)
                .assertNext(asset -> {
                    assert asset.getId() == 10L;
                    assert asset.getCurrency() != null;
                    assert asset.getCurrency().getCode().equals("USD");
                })
                .assertNext(asset -> {
                    assert asset.getId() == 11L;
                    assert asset.getCurrency() != null;
                    assert asset.getCurrency().getCode().equals("USD");
                })
                .verifyComplete();

        verify(assetPort).findAllByPortfolio(portfolioId);
        verify(currencyService, times(2)).getCurrencyBYId(1L);
    }

    @Test
    void getAllAssetsForPortfolio_currencyNotFound_returnsAssetWithoutCurrency() {
        // given
        Long portfolioId = 1L;

        Asset asset = new Asset();
        asset.setId(10L);
        asset.setCurrencyRefId(999L);

        when(assetPort.findAllByPortfolio(portfolioId))
                .thenReturn(Flux.just(asset));
        when(currencyService.getCurrencyBYId(999L))
                .thenReturn(Mono.empty());

        // when
        Flux<Asset> result = assetService.getAllAssetsForPortfolio(portfolioId);

        // then
        StepVerifier.create(result)
                .assertNext(resultAsset -> {
                    assert resultAsset.getId() == 10L;
                    assert resultAsset.getCurrency() == null;
                })
                .verifyComplete();

        verify(assetPort).findAllByPortfolio(portfolioId);
        verify(currencyService).getCurrencyBYId(999L);
    }

    @Test
    void getAllAssetsForPortfolio_emptyResult_completesWithoutValues() {
        // given
        Long portfolioId = 99L;
        when(assetPort.findAllByPortfolio(portfolioId))
                .thenReturn(Flux.empty());

        // when
        Flux<Asset> result = assetService.getAllAssetsForPortfolio(portfolioId);

        // then
        StepVerifier.create(result)
                .verifyComplete();

        verify(assetPort).findAllByPortfolio(portfolioId);
        verifyNoInteractions(currencyService);
    }

    @Test
    void getAllAssetsForPortfolio_multipleDifferentCurrencies_enrichesEachCorrectly() {
        // given
        Long portfolioId = 1L;

        Asset asset1 = new Asset();
        asset1.setId(10L);
        asset1.setCurrencyRefId(1L);

        Asset asset2 = new Asset();
        asset2.setId(11L);
        asset2.setCurrencyRefId(2L);

        Currency currencyUSD = new Currency();
        currencyUSD.setId(1L);
        currencyUSD.setCode("USD");

        Currency currencyEUR = new Currency();
        currencyEUR.setId(2L);
        currencyEUR.setCode("EUR");

        when(assetPort.findAllByPortfolio(portfolioId))
                .thenReturn(Flux.just(asset1, asset2));
        when(currencyService.getCurrencyBYId(1L))
                .thenReturn(Mono.just(currencyUSD));
        when(currencyService.getCurrencyBYId(2L))
                .thenReturn(Mono.just(currencyEUR));

        // when
        Flux<Asset> result = assetService.getAllAssetsForPortfolio(portfolioId);

        // then
        StepVerifier.create(result)
                .assertNext(asset -> {
                    assert asset.getId() == 10L;
                    assert asset.getCurrency().getCode().equals("USD");
                })
                .assertNext(asset -> {
                    assert asset.getId() == 11L;
                    assert asset.getCurrency().getCode().equals("EUR");
                })
                .verifyComplete();

        verify(assetPort).findAllByPortfolio(portfolioId);
        verify(currencyService).getCurrencyBYId(1L);
        verify(currencyService).getCurrencyBYId(2L);
    }
}
