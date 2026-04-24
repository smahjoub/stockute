package com.smahjoub.stockute.adapters.external.alphavantage;

import com.smahjoub.stockute.adapters.config.AlphaVantageProperties;
import com.smahjoub.stockute.adapters.external.alphavantage.dto.AlphaVantageDividendItem;
import com.smahjoub.stockute.adapters.external.alphavantage.mapper.AlphaVantageDividendMapper;
import com.smahjoub.stockute.application.port.dividend.out.DividendHistoryItem;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AlphaVantageDividendAdapterTest {

    private MockWebServer mockWebServer;
    private AlphaVantageDividendAdapter adapter;
    private AlphaVantageDividendMapper mapper;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        mapper = mock(AlphaVantageDividendMapper.class);

        AlphaVantageProperties props = new AlphaVantageProperties();
        props.setBaseUrl(mockWebServer.url("/").toString());
        props.setApiKey("test-key");

        adapter = new AlphaVantageDividendAdapter(
                WebClient.builder(),
                props,
                mapper
        );
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getDividendHistory_shouldReturnMappedDividendItems() {

        // Mock AlphaVantage JSON response
        String json = """
                {
                  "symbol": "IBM",
                  "data": [
                    {
                      "ex_dividend_date": "2026-05-08",
                      "declaration_date": "2026-04-22",
                      "record_date": "2026-05-08",
                      "payment_date": "2026-06-10",
                      "amount": "1.69"
                    }
                  ]
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(json)
                .addHeader("Content-Type", "application/json"));

        // Mock mapper behavior
        DividendHistoryItem mapped = new DividendHistoryItem(
                LocalDateTime.of(2026, 5, 8, 0, 0),
                LocalDateTime.of(2026, 4, 22, 0, 0),
                LocalDateTime.of(2026, 5, 8, 0, 0),
                LocalDateTime.of(2026, 6, 10, 0, 0),
                new BigDecimal("1.69")
        );

        when(mapper.toDividendHistoryItem(any(AlphaVantageDividendItem.class)))
                .thenReturn(mapped);

        // Execute
        Flux<DividendHistoryItem> result = adapter.getDividendHistory("IBM");

        // Verify
        StepVerifier.create(result)
                .expectNext(mapped)
                .verifyComplete();

        verify(mapper, times(1)).toDividendHistoryItem(any(AlphaVantageDividendItem.class));
    }
}
