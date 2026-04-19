package com.smahjoub.stockute.adapters.external.alphavantage;

import com.smahjoub.stockute.adapters.config.AlphaVantageProperties;
import com.smahjoub.stockute.adapters.external.alphavantage.dto.AlphaVantageBestMatchDTO;
import com.smahjoub.stockute.adapters.external.alphavantage.mapper.AlphaVantageSecuritySearchMapper;
import com.smahjoub.stockute.domain.model.Security;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AlphaVantageSecuritySearchAdapterTest {

    private MockWebServer mockWebServer;
    private AlphaVantageSecuritySearchAdapter adapter;
    private AlphaVantageSecuritySearchMapper mapper;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        mapper = mock(AlphaVantageSecuritySearchMapper.class);

        AlphaVantageProperties props = new AlphaVantageProperties();
        props.setBaseUrl(mockWebServer.url("/").toString());
        props.setApiKey("test-key");

        adapter = new AlphaVantageSecuritySearchAdapter(
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
    void search_shouldReturnMappedSecurities() {

        // Mock AlphaVantage JSON response
        String json = """
                {
                  "bestMatches": [
                    {
                      "1. symbol": "AAPL",
                      "2. name": "Apple Inc",
                      "3. type": "Equity",
                      "4. region": "United States",
                      "5. marketOpen": "09:30",
                      "6. marketClose": "16:00",
                      "7. timezone": "UTC-04",
                      "8. currency": "USD",
                      "9. matchScore": "1.0000"
                    }
                  ]
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(json)
                .addHeader("Content-Type", "application/json"));

        // Mock mapper behavior
        Security mapped = new Security();
        mapped.setSymbol("AAPL");
        mapped.setName("Apple Inc");
        mapped.setActive(true);

        when(mapper.toSecurity(any(AlphaVantageBestMatchDTO.class)))
                .thenReturn(mapped);

        // Execute
        Flux<Security> result = adapter.search("AAPL");

        // Verify
        StepVerifier.create(result)
                .expectNext(mapped)
                .verifyComplete();

        verify(mapper, times(1)).toSecurity(any(AlphaVantageBestMatchDTO.class));
    }
}
