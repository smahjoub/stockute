package com.smahjoub.stockute.adapters.external.earningsapi;

import com.smahjoub.stockute.adapters.config.EarningsApiProperties;
import com.smahjoub.stockute.adapters.external.earningsapi.dto.EarningsApiDividendResponse;
import com.smahjoub.stockute.adapters.external.earningsapi.mapper.EarningsApiDividendMapper;
import com.smahjoub.stockute.application.port.dividend.out.DividendCalendarPort;
import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendCalendarItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class EarningsApiDividendCalendarAdapter implements DividendCalendarPort {

    private final WebClient.Builder webClientBuilder;
    private final EarningsApiProperties earningsApiProperties;
    private final EarningsApiDividendMapper mapper;

    @Override
    public Flux<SecurityDividendCalendarItem> getUpcomingDividends() {
        return webClientBuilder.build()
                .get()
                .uri(earningsApiProperties.getBaseUrl(), uriBuilder -> uriBuilder
                        .path("/v1/calendar/dividends")
                        .queryParam("apikey", earningsApiProperties.getApiKey())
                        .build())
                .retrieve()
                .bodyToFlux(EarningsApiDividendResponse.class)
                .map(mapper::toDomain);
    }
}