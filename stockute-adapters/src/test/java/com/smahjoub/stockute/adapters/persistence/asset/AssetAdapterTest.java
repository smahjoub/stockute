package com.smahjoub.stockute.adapters.persistence.asset;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.smahjoub.stockute.domain.model.Asset;
import com.smahjoub.stockute.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@ExtendWith(MockitoExtension.class)
class AssetAdapterTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetAdapter assetAdapter;

    private Asset testAsset;

    @BeforeEach
    void setUp() {
        testAsset = new Asset();
        testAsset.setId(100L);
        testAsset.setPortfolioRefId(1L);
        testAsset.setTicker("AAPL");
        testAsset.setExchange("NASDAQ");
        testAsset.setCurrencyRefId(1L);
        testAsset.setQuantity(10.0);
        testAsset.setAveragePrice(BigDecimal.valueOf(100.00));
    }

    @Test
    void findAllByPortfolio_ReturnsAssets() {
        // Given
        Asset asset2 = new Asset();
        asset2.setId(101L);
        when(assetRepository.findAllByPortfolio(1L)).thenReturn(Flux.just(testAsset, asset2));

        // When & Then
        StepVerifier.create(assetAdapter.findAllByPortfolio(1L))
                .expectNextCount(2)
                .verifyComplete();

        verify(assetRepository).findAllByPortfolio(1L);
    }

    @Test
    void findAllByPortfolio_NoAssets_ReturnsEmpty() {
        // Given
        when(assetRepository.findAllByPortfolio(1L)).thenReturn(Flux.empty());

        // When & Then
        StepVerifier.create(assetAdapter.findAllByPortfolio(1L))
                .verifyComplete();

        verify(assetRepository).findAllByPortfolio(1L);
    }

    @Test
    void getAssetForPortfolio_Found_ReturnsAsset() {
        // Given
        when(assetRepository.findByPortfolioIdAndTickerAndExchange(1L, "AAPL", "NASDAQ", 1L))
                .thenReturn(Mono.just(testAsset));

        // When & Then
        StepVerifier.create(assetAdapter.getAssetForPortfolio(1L, "AAPL", "NASDAQ", 1L))
                .expectNext(testAsset)
                .verifyComplete();

        verify(assetRepository).findByPortfolioIdAndTickerAndExchange(1L, "AAPL", "NASDAQ", 1L);
    }

    @Test
    void getAssetForPortfolio_NotFound_ReturnsEmpty() {
        // Given
        when(assetRepository.findByPortfolioIdAndTickerAndExchange(1L, "BTC", "BINANCE", 1L))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(assetAdapter.getAssetForPortfolio(1L, "BTC", "BINANCE", 1L))
                .verifyComplete();

        verify(assetRepository).findByPortfolioIdAndTickerAndExchange(1L, "BTC", "BINANCE", 1L);
    }

    @Test
    void createAssetForPortfolio_CreatesNewAsset() {
        // Given
        Asset newAsset = new Asset();
        newAsset.setId(200L);
        newAsset.setName("Bitcoin");
        newAsset.setPortfolioRefId(1L);
        newAsset.setTicker("BTC");
        newAsset.setExchange("BINANCE");
        newAsset.setCurrencyRefId(1L);
        newAsset.setQuantity(0.0);
        newAsset.setAveragePrice(BigDecimal.ZERO);
        when(assetRepository.save(any(Asset.class))).thenReturn(Mono.just(newAsset));

        // When & Then
        StepVerifier.create(assetAdapter.createAssetForPortfolio("Bitcoin", 1L, "BTC", "BINANCE", 1L))
                .assertNext(asset -> {
                    assert asset.getName().equals("Bitcoin");
                    assert asset.getQuantity() == 0.0;
                    assert asset.getAveragePrice().equals(BigDecimal.ZERO);
                })
                .verifyComplete();

        verify(assetRepository).save(argThat(asset ->
                asset.getName().equals("Bitcoin") &&
                        asset.getTicker().equals("BTC") &&
                        asset.getQuantity() == 0.0));
    }

    @Test
    void updateAsset_WithVariousTransactionTypesAndFees() {
        // Given - Initial asset with quantity 10 and avg price 100, some accumulated fees and totals
        testAsset.setQuantity(10.0);
        testAsset.setAveragePrice(BigDecimal.valueOf(100));
        testAsset.setAccumulatedFees(BigDecimal.valueOf(5.0));
        testAsset.setTotalAmountInvested(BigDecimal.valueOf(1000.0));
        testAsset.setTotalGainLoss(BigDecimal.valueOf(100.0));

        when(assetRepository.findById(100L)).thenReturn(Mono.just(testAsset));
        when(assetRepository.save(any(Asset.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // BUY transaction adding 5 units at price 150 with 2 fees
        Transaction buyTx = new Transaction();
        buyTx.setQuantity(5);
        buyTx.setPrice(BigDecimal.valueOf(150));
        buyTx.setFees(BigDecimal.valueOf(2));
        buyTx.setType("BUY");

        StepVerifier.create(assetAdapter.updateAsset(100L, buyTx))
                .assertNext(updated -> {
                    assert updated.getQuantity() == 15.0;
                    BigDecimal expectedAvgPrice = BigDecimal.valueOf(1000 + 750)
                            .divide(BigDecimal.valueOf(15), 8, RoundingMode.HALF_UP);
                    assert updated.getAveragePrice().compareTo(expectedAvgPrice) == 0;

                    assert updated.getTotalAmountInvested().compareTo(BigDecimal.valueOf(1750)) == 0;
                    assert updated.getTotalGainLoss().compareTo(BigDecimal.valueOf(100)) == 0;
                })
                .verifyComplete();

        // Reset testAsset state independently for SELL
        Asset sellTestAsset = new Asset();
        sellTestAsset.setId(100L);
        sellTestAsset.setQuantity(10.0);
        sellTestAsset.setAveragePrice(BigDecimal.valueOf(100));
        sellTestAsset.setAccumulatedFees(BigDecimal.valueOf(5.0));
        sellTestAsset.setTotalAmountInvested(BigDecimal.valueOf(1000.0));
        sellTestAsset.setTotalGainLoss(BigDecimal.valueOf(100.0));

        when(assetRepository.findById(100L)).thenReturn(Mono.just(sellTestAsset));

        // SELL transaction subtracting 3 units at price 160 with 1 fees
        Transaction sellTx = new Transaction();
        sellTx.setQuantity(3);
        sellTx.setPrice(BigDecimal.valueOf(160));
        sellTx.setFees(BigDecimal.valueOf(1));
        sellTx.setType("SELL");

        StepVerifier.create(assetAdapter.updateAsset(100L, sellTx))
                .assertNext(updated -> {
                    assert updated.getQuantity() == 7.0;
                    assert updated.getTotalAmountInvested().compareTo(BigDecimal.valueOf(1000)) == 0;
                    assert updated.getTotalGainLoss().compareTo(BigDecimal.valueOf(580)) == 0;
                })
                .verifyComplete();

        verify(assetRepository, times(2)).findById(100L);
        verify(assetRepository, times(2)).save(any(Asset.class));
    }

}
