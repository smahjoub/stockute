package com.smahjoub.stockute.adapters.persistence.asset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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
        testAsset.setName("Apple Inc.");
        testAsset.setPortfolioRefId(1L);
        testAsset.setCurrencyRefId(1L);
        testAsset.setSecurityRefId(200L);
        testAsset.setQuantity(10.0);
        testAsset.setAveragePrice(BigDecimal.valueOf(100.00));
    }

    @Test
    void findAllByPortfolio_ReturnsAssets() {
        Asset asset2 = new Asset();
        asset2.setId(101L);

        when(assetRepository.findAllByPortfolio(1L)).thenReturn(Flux.just(testAsset, asset2));

        StepVerifier.create(assetAdapter.findAllByPortfolio(1L))
                .expectNextCount(2)
                .verifyComplete();

        verify(assetRepository).findAllByPortfolio(1L);
    }

    @Test
    void findAllByPortfolio_NoAssets_ReturnsEmpty() {
        when(assetRepository.findAllByPortfolio(1L)).thenReturn(Flux.empty());

        StepVerifier.create(assetAdapter.findAllByPortfolio(1L))
                .verifyComplete();

        verify(assetRepository).findAllByPortfolio(1L);
    }

    @Test
    void getAssetForPortfolio_Found_ReturnsAsset() {
        when(assetRepository.findByPortfolioIdAndSecurityRefIdAndCurrencyRefId(1L, 200L, 1L))
                .thenReturn(Mono.just(testAsset));

        StepVerifier.create(assetAdapter.getAssetForPortfolioBySecurityRefId(1L, 200L, 1L))
                .expectNext(testAsset)
                .verifyComplete();

        verify(assetRepository).findByPortfolioIdAndSecurityRefIdAndCurrencyRefId(1L, 200L, 1L);
    }

    @Test
    void getAssetForPortfolio_NotFound_ReturnsEmpty() {
        when(assetRepository.findByPortfolioIdAndSecurityRefIdAndCurrencyRefId(1L, 999L, 1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(assetAdapter.getAssetForPortfolioBySecurityRefId(1L, 999L, 1L))
                .verifyComplete();

        verify(assetRepository).findByPortfolioIdAndSecurityRefIdAndCurrencyRefId(1L, 999L, 1L);
    }

    @Test
    void createAssetForPortfolio_CreatesNewAsset() {
        Asset newAsset = new Asset();
        newAsset.setId(200L);
        newAsset.setName("Bitcoin");
        newAsset.setPortfolioRefId(1L);
        newAsset.setSecurityRefId(300L);
        newAsset.setCurrencyRefId(1L);
        newAsset.setQuantity(0.0);
        newAsset.setAveragePrice(BigDecimal.ZERO);

        when(assetRepository.save(any(Asset.class))).thenReturn(Mono.just(newAsset));

        StepVerifier.create(assetAdapter.createAssetForPortfolio("Bitcoin", 1L, 300L, 1L))
                .assertNext(asset -> {
                    assertEquals("Bitcoin", asset.getName());
                    assertEquals(0.0, asset.getQuantity());
                    assertEquals(0, asset.getAveragePrice().compareTo(BigDecimal.ZERO));
                })
                .verifyComplete();

        verify(assetRepository).save(argThat(asset ->
                asset.getName().equals("Bitcoin") &&
                        asset.getSecurityRefId().equals(300L) &&
                        asset.getCurrencyRefId().equals(1L) &&
                        asset.getQuantity() == 0.0 &&
                        asset.getAveragePrice().compareTo(BigDecimal.ZERO) == 0
        ));
    }

    @Test
    void updateAsset_WithVariousTransactionTypesAndFees() {
        testAsset.setQuantity(10.0);
        testAsset.setAveragePrice(BigDecimal.valueOf(100));
        testAsset.setAccumulatedFees(BigDecimal.valueOf(5.0));
        testAsset.setTotalAmountInvested(BigDecimal.valueOf(1000.0));
        testAsset.setTotalGainLoss(BigDecimal.valueOf(100.0));

        when(assetRepository.findById(100L)).thenReturn(Mono.just(testAsset));
        when(assetRepository.save(any(Asset.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Transaction buyTx = new Transaction();
        buyTx.setQuantity(BigDecimal.valueOf(5));
        buyTx.setPrice(BigDecimal.valueOf(150));
        buyTx.setFees(BigDecimal.valueOf(2));
        buyTx.setType("BUY");

        StepVerifier.create(assetAdapter.updateAsset(100L, buyTx))
                .assertNext(updated -> {
                    assertEquals(15.0, updated.getQuantity());

                    BigDecimal expectedAvgPrice = BigDecimal.valueOf(1750)
                            .divide(BigDecimal.valueOf(15), 8, RoundingMode.HALF_UP);
                    assertEquals(0, updated.getAveragePrice().compareTo(expectedAvgPrice));

                    assertEquals(0, updated.getTotalAmountInvested().compareTo(BigDecimal.valueOf(1750)));
                    assertEquals(0, updated.getTotalGainLoss().compareTo(BigDecimal.valueOf(100)));

                    assertEquals(0, updated.getAccumulatedFees().compareTo(BigDecimal.valueOf(4)));
                })
                .verifyComplete();

        Asset sellTestAsset = new Asset();
        sellTestAsset.setId(100L);
        sellTestAsset.setQuantity(10.0);
        sellTestAsset.setAveragePrice(BigDecimal.valueOf(100));
        sellTestAsset.setAccumulatedFees(BigDecimal.valueOf(5.0));
        sellTestAsset.setTotalAmountInvested(BigDecimal.valueOf(1000.0));
        sellTestAsset.setTotalGainLoss(BigDecimal.valueOf(100.0));

        when(assetRepository.findById(100L)).thenReturn(Mono.just(sellTestAsset));

        Transaction sellTx = new Transaction();
        sellTx.setQuantity(BigDecimal.valueOf(3));
        sellTx.setPrice(BigDecimal.valueOf(160));
        sellTx.setFees(BigDecimal.valueOf(1));
        sellTx.setType("SELL");

        StepVerifier.create(assetAdapter.updateAsset(100L, sellTx))
                .assertNext(updated -> {
                    assertEquals(7.0, updated.getQuantity());

                    BigDecimal expectedAvgPrice = BigDecimal.valueOf(1000 + 480)
                            .divide(BigDecimal.valueOf(7), 8, RoundingMode.HALF_UP);
                    assertEquals(0, updated.getAveragePrice().compareTo(expectedAvgPrice));

                    assertEquals(0, updated.getTotalAmountInvested().compareTo(BigDecimal.valueOf(1000)));
                    assertEquals(0, updated.getTotalGainLoss().compareTo(BigDecimal.valueOf(580)));
                    assertEquals(0, updated.getAccumulatedFees().compareTo(BigDecimal.valueOf(2)));
                })
                .verifyComplete();

        verify(assetRepository, times(2)).findById(100L);
        verify(assetRepository, times(2)).save(any(Asset.class));
    }
}