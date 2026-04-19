package com.smahjoub.stockute.application.service.asset;

import com.smahjoub.stockute.application.port.asset.out.AssetPort;
import com.smahjoub.stockute.application.port.security.out.SecurityPort;
import com.smahjoub.stockute.application.service.currency.CurrencyService;
import com.smahjoub.stockute.domain.model.Asset;
import com.smahjoub.stockute.domain.model.Currency;
import com.smahjoub.stockute.domain.model.Security;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetPort assetPort;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private SecurityPort securityPort;

    @InjectMocks
    private AssetService assetService;

    private Currency currency;
    private Security security;

    @BeforeEach
    void setUp() {
        currency = new Currency();
        currency.setId(1L);
        currency.setCode("USD");

        security = new Security();
        security.setId(100L);
        security.setSymbol("AAPL");
        security.setRegion("United States");
    }

    @Test
    void getAllAssetsForPortfolio_enrichesAssetsWithCurrencyAndSecurity() {
        Long portfolioId = 1L;

        Asset asset1 = new Asset();
        asset1.setId(10L);
        asset1.setCurrencyRefId(1L);
        asset1.setSecurityRefId(100L);

        Asset asset2 = new Asset();
        asset2.setId(11L);
        asset2.setCurrencyRefId(1L);
        asset2.setSecurityRefId(100L);

        when(assetPort.findAllByPortfolio(portfolioId))
                .thenReturn(Flux.just(asset1, asset2));
        when(currencyService.getCurrencyBYId(1L))
                .thenReturn(Mono.just(currency));
        when(securityPort.findById(100L))
                .thenReturn(Mono.just(security));

        Flux<Asset> result = assetService.getAllAssetsForPortfolio(portfolioId);

        StepVerifier.create(result)
                .assertNext(asset -> {
                    assertEquals(10L, asset.getId());
                    assertNotNull(asset.getCurrency());
                    assertEquals("USD", asset.getCurrency().getCode());
                    assertNotNull(asset.getSecurity());
                    assertEquals("AAPL", asset.getSecurity().getSymbol());
                    assertEquals("United States", asset.getSecurity().getRegion());
                })
                .assertNext(asset -> {
                    assertEquals(11L, asset.getId());
                    assertNotNull(asset.getCurrency());
                    assertEquals("USD", asset.getCurrency().getCode());
                    assertNotNull(asset.getSecurity());
                    assertEquals("AAPL", asset.getSecurity().getSymbol());
                    assertEquals("United States", asset.getSecurity().getRegion());
                })
                .verifyComplete();

        verify(assetPort).findAllByPortfolio(portfolioId);
        verify(currencyService, times(2)).getCurrencyBYId(1L);
        verify(securityPort, times(2)).findById(100L);
    }

    @Test
    void getAllAssetsForPortfolio_currencyNotFound_returnsAssetWithoutCurrencyAndSecurity() {
        Long portfolioId = 1L;

        Asset asset = new Asset();
        asset.setId(10L);
        asset.setCurrencyRefId(999L);
        asset.setSecurityRefId(100L);

        when(assetPort.findAllByPortfolio(portfolioId))
                .thenReturn(Flux.just(asset));
        when(currencyService.getCurrencyBYId(999L))
                .thenReturn(Mono.empty());
        when(securityPort.findById(100L))
                .thenReturn(Mono.just(security));

        Flux<Asset> result = assetService.getAllAssetsForPortfolio(portfolioId);

        StepVerifier.create(result)
                .assertNext(resultAsset -> {
                    assertEquals(10L, resultAsset.getId());
                    assertNull(resultAsset.getCurrency());
                    assertNull(resultAsset.getSecurity());
                })
                .verifyComplete();

        verify(assetPort).findAllByPortfolio(portfolioId);
        verify(currencyService).getCurrencyBYId(999L);
        verify(securityPort).findById(100L);
    }

    @Test
    void getAllAssetsForPortfolio_securityNotFound_returnsAssetWithoutSecurity() {
        Long portfolioId = 1L;

        Asset asset = new Asset();
        asset.setId(10L);
        asset.setCurrencyRefId(1L);
        asset.setSecurityRefId(999L);

        when(assetPort.findAllByPortfolio(portfolioId))
                .thenReturn(Flux.just(asset));
        when(currencyService.getCurrencyBYId(1L))
                .thenReturn(Mono.just(currency));
        when(securityPort.findById(999L))
                .thenReturn(Mono.empty());

        Flux<Asset> result = assetService.getAllAssetsForPortfolio(portfolioId);

        StepVerifier.create(result)
                .assertNext(resultAsset -> {
                    assertEquals(10L, resultAsset.getId());
                    assertNotNull(resultAsset.getCurrency());
                    assertEquals("USD", resultAsset.getCurrency().getCode());
                    assertNull(resultAsset.getSecurity());
                })
                .verifyComplete();

        verify(assetPort).findAllByPortfolio(portfolioId);
        verify(currencyService).getCurrencyBYId(1L);
        verify(securityPort).findById(999L);
    }

    @Test
    void getAllAssetsForPortfolio_emptyResult_completesWithoutValues() {
        Long portfolioId = 99L;

        when(assetPort.findAllByPortfolio(portfolioId))
                .thenReturn(Flux.empty());

        Flux<Asset> result = assetService.getAllAssetsForPortfolio(portfolioId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(assetPort).findAllByPortfolio(portfolioId);
        verifyNoInteractions(currencyService, securityPort);
    }

    @Test
    void getAllAssetsForPortfolio_multipleDifferentCurrenciesAndSecurities_enrichesEachCorrectly() {
        Long portfolioId = 1L;

        Asset asset1 = new Asset();
        asset1.setId(10L);
        asset1.setCurrencyRefId(1L);
        asset1.setSecurityRefId(100L);

        Asset asset2 = new Asset();
        asset2.setId(11L);
        asset2.setCurrencyRefId(2L);
        asset2.setSecurityRefId(200L);

        Currency currencyUSD = new Currency();
        currencyUSD.setId(1L);
        currencyUSD.setCode("USD");

        Currency currencyEUR = new Currency();
        currencyEUR.setId(2L);
        currencyEUR.setCode("EUR");

        Security security1 = new Security();
        security1.setId(100L);
        security1.setSymbol("AAPL");
        security1.setRegion("United States");

        Security security2 = new Security();
        security2.setId(200L);
        security2.setSymbol("MSFT");
        security2.setRegion("United States");

        when(assetPort.findAllByPortfolio(portfolioId))
                .thenReturn(Flux.just(asset1, asset2));
        when(currencyService.getCurrencyBYId(1L))
                .thenReturn(Mono.just(currencyUSD));
        when(currencyService.getCurrencyBYId(2L))
                .thenReturn(Mono.just(currencyEUR));
        when(securityPort.findById(100L))
                .thenReturn(Mono.just(security1));
        when(securityPort.findById(200L))
                .thenReturn(Mono.just(security2));

        Flux<Asset> result = assetService.getAllAssetsForPortfolio(portfolioId);

        StepVerifier.create(result)
                .assertNext(asset -> {
                    assertEquals(10L, asset.getId());
                    assertEquals("USD", asset.getCurrency().getCode());
                    assertEquals("AAPL", asset.getSecurity().getSymbol());
                })
                .assertNext(asset -> {
                    assertEquals(11L, asset.getId());
                    assertEquals("EUR", asset.getCurrency().getCode());
                    assertEquals("MSFT", asset.getSecurity().getSymbol());
                })
                .verifyComplete();

        verify(assetPort).findAllByPortfolio(portfolioId);
        verify(currencyService).getCurrencyBYId(1L);
        verify(currencyService).getCurrencyBYId(2L);
        verify(securityPort).findById(100L);
        verify(securityPort).findById(200L);
    }
}