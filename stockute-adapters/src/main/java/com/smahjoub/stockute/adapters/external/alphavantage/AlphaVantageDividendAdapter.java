package com.smahjoub.stockute.adapters.external.alphavantage;

import com.smahjoub.stockute.adapters.config.AlphaVantageProperties;
import com.smahjoub.stockute.adapters.external.alphavantage.dto.AlphaVantageDividendResponse;
import com.smahjoub.stockute.adapters.external.alphavantage.mapper.AlphaVantageDividendMapper;
import com.smahjoub.stockute.application.port.dividend.out.DividendHistoryItem;
import com.smahjoub.stockute.application.port.dividend.out.DividendMarketDataPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AlphaVantageDividendAdapter implements DividendMarketDataPort {

    private final WebClient.Builder webClientBuilder;
    private final AlphaVantageProperties alphaVantageProperties;
    private final AlphaVantageDividendMapper alphaVantageDividendMapper;

    @Override
    public Flux<DividendHistoryItem> getDividendHistory(final String symbol) {
        return webClientBuilder.build().get()
                .uri(alphaVantageProperties.getBaseUrl(), uriBuilder -> uriBuilder
                        .path("/query")
                        .queryParam("function", "DIVIDENDS")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", alphaVantageProperties.getApiKey())
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("No error body")
                                .flatMap(body -> Mono.error(new IllegalStateException(
                                        "Alpha Vantage request failed for symbol " + symbol + ": " + body
                                )))
                )
                .bodyToMono(AlphaVantageDividendResponse.class)
                .flatMapMany(response -> Flux.fromIterable(response.getData()))
                .map(alphaVantageDividendMapper::toDividendHistoryItem);
    }
}
