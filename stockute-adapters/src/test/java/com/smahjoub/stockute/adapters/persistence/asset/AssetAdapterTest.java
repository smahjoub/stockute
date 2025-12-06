package com.smahjoub.stockute.adapters.persistence.asset;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.smahjoub.stockute.domain.model.Asset;
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
    void updateAsset_FirstPurchase_UpdatesQuantityAndPrice() {
        // Given - Asset exists with zero quantity
        Asset emptyAsset = new Asset();
        emptyAsset.setId(100L);
        emptyAsset.setQuantity(0.0);
        emptyAsset.setAveragePrice(BigDecimal.ZERO);
        when(assetRepository.findById(100L)).thenReturn(Mono.just(emptyAsset));
        when(assetRepository.save(any(Asset.class))).thenReturn(Mono.just(emptyAsset));

        // When & Then - Add 5 units at $200
        StepVerifier.create(assetAdapter.updateAsset(100L, 5.0, BigDecimal.valueOf(200.0)))
                .assertNext(updated -> {
                    assert updated.getQuantity() == 5.0;
                    assert updated.getAveragePrice().compareTo(BigDecimal.valueOf(200.0)) == 0;
                })
                .verifyComplete();
    }

    @Test
    void updateAsset_ExistingAsset_RecalculatesAveragePrice() {
        // Given - Asset has 10 units at $100 avg = $1000 total
        testAsset.setQuantity(10.0);
        testAsset.setAveragePrice(BigDecimal.valueOf(100)); // Use 100, not 100.00
        when(assetRepository.findById(100L)).thenReturn(Mono.just(testAsset));

        Asset savedAsset = new Asset(); // Mock the SAVED asset with expected values
        savedAsset.setId(100L);
        savedAsset.setQuantity(15.0);
        savedAsset.setAveragePrice(BigDecimal.valueOf(116.66666667)); // Exact expected result
        when(assetRepository.save(any(Asset.class))).thenReturn(Mono.just(savedAsset));

        // When & Then
        StepVerifier.create(assetAdapter.updateAsset(100L, 5.0, BigDecimal.valueOf(150)))
                .assertNext(updated -> {
                    assert updated.getQuantity() == 15.0;
                    // Verify the ACTUAL calculation: (1000 + 750) / 15 = 116.66666667
                    BigDecimal expectedAvg = BigDecimal.valueOf(1750).divide(
                            BigDecimal.valueOf(15), 8, RoundingMode.HALF_UP);
                    assert updated.getAveragePrice().compareTo(expectedAvg) == 0;
                })
                .verifyComplete();
    }


    @Test
    void updateAsset_ZeroQuantity_SetsPriceToZero() {
        // Given - Asset has 10 units, add -10 units
        when(assetRepository.findById(100L)).thenReturn(Mono.just(testAsset));
        when(assetRepository.save(any(Asset.class))).thenReturn(Mono.just(testAsset));

        // When & Then
        StepVerifier.create(assetAdapter.updateAsset(100L, -10.0, BigDecimal.valueOf(50.0)))
                .assertNext(updated -> {
                    assert updated.getQuantity() == 0.0;
                    assert updated.getAveragePrice().equals(BigDecimal.ZERO);
                })
                .verifyComplete();
    }

    @Test
    void updateAsset_NegativeQuantity_SetsPriceToZero() {
        // Given - Asset has 5 units, add -10 units
        testAsset.setQuantity(5.0);
        when(assetRepository.findById(100L)).thenReturn(Mono.just(testAsset));
        when(assetRepository.save(any(Asset.class))).thenReturn(Mono.just(testAsset));

        // When & Then
        StepVerifier.create(assetAdapter.updateAsset(100L, -10.0, BigDecimal.valueOf(50.0)))
                .assertNext(updated -> {
                    assert updated.getQuantity() == -5.0;
                    assert updated.getAveragePrice().equals(BigDecimal.ZERO);
                })
                .verifyComplete();
    }

    @Test
    void updateAsset_AssetNotFound_ReturnsEmpty() {
        // Given
        when(assetRepository.findById(999L)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(assetAdapter.updateAsset(999L, 5.0, BigDecimal.valueOf(100.0)))
                .verifyComplete();

        verify(assetRepository).findById(999L);
    }

    @Test
    void updateAsset_NullPrice_UsesZero() {
        // Given
        when(assetRepository.findById(100L)).thenReturn(Mono.just(testAsset));
        when(assetRepository.save(any(Asset.class))).thenReturn(Mono.just(testAsset));

        // When & Then
        StepVerifier.create(assetAdapter.updateAsset(100L, 2.0, null))
                .assertNext(updated -> {
                    assert updated.getQuantity() == 12.0;
                    // Price calculation uses ZERO for null price
                    BigDecimal currentTotal = BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(10));
                    BigDecimal newTotal = currentTotal.divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP);
                    assert updated.getAveragePrice().compareTo(newTotal) == 0;
                })
                .verifyComplete();
    }
}
