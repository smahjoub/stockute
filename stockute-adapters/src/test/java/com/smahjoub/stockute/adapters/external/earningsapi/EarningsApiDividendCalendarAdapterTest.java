package com.smahjoub.stockute.adapters.external.earningsapi;

import com.smahjoub.stockute.adapters.config.EarningsApiProperties;
import com.smahjoub.stockute.adapters.external.earningsapi.dto.EarningsApiDividendResponse;
import com.smahjoub.stockute.adapters.external.earningsapi.mapper.EarningsApiDividendMapper;
import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendCalendarItem;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EarningsApiDividendCalendarAdapterTest {

    private MockWebServer mockWebServer;
    private EarningsApiDividendCalendarAdapter adapter;
    private EarningsApiDividendMapper mapper;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        mapper = mock(EarningsApiDividendMapper.class);

        EarningsApiProperties properties = new EarningsApiProperties();
        properties.setBaseUrl(mockWebServer.url("/").toString());
        properties.setApiKey("test-key");

        adapter = new EarningsApiDividendCalendarAdapter(
                WebClient.builder(),
                properties,
                mapper
        );
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getUpcomingDividends_returnsMappedCalendarItems() throws InterruptedException {
        String json = """
                [
                  {
                    "name": "Apple Inc.",
                    "symbol": "AAPL",
                    "dividend_ex_date": "2026-08-10",
                    "payment_date": "2026-08-20",
                    "dividend_rate": 0.24
                  },
                  {
                    "name": "Microsoft Corp.",
                    "symbol": "MSFT",
                    "dividend_ex_date": "2026-08-12",
                    "payment_date": "2026-08-22",
                    "dividend_rate": 0.68
                  }
                ]
                """;

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(json)
                .addHeader("Content-Type", "application/json"));

        SecurityDividendCalendarItem item1 = new SecurityDividendCalendarItem(
                "Apple Inc.",
                "AAPL",
                LocalDate.of(2026, 8, 10),
                LocalDate.of(2026, 8, 20),
                BigDecimal.valueOf(0.24)
        );
        SecurityDividendCalendarItem item2 = new SecurityDividendCalendarItem(
                "Microsoft Corp.",
                "MSFT",
                LocalDate.of(2026, 8, 12),
                LocalDate.of(2026, 8, 22),
                BigDecimal.valueOf(0.68)
        );

        when(mapper.toDomain(any(EarningsApiDividendResponse.class)))
                .thenReturn(item1)
                .thenReturn(item2);

        Flux<SecurityDividendCalendarItem> result = adapter.getUpcomingDividends();

        StepVerifier.create(result)
                .expectNext(item1)
                .expectNext(item2)
                .verifyComplete();

        verify(mapper, times(2)).toDomain(any(EarningsApiDividendResponse.class));

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/v1/calendar/dividends?apikey=test-key", request.getPath());
    }
}
