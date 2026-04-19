package com.smahjoub.stockute.adapters.external.alphavantage;

import com.smahjoub.stockute.adapters.config.AlphaVantageProperties;
import com.smahjoub.stockute.adapters.external.alphavantage.dto.AlphaVantageSearchResponse;
import com.smahjoub.stockute.adapters.external.alphavantage.mapper.AlphaVantageSecuritySearchMapper;
import com.smahjoub.stockute.application.port.security.out.SecuritySearchPort;
import com.smahjoub.stockute.domain.model.Security;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class AlphaVantageSecuritySearchAdapter implements SecuritySearchPort {

    private final WebClient.Builder webClientBuilder;
    private final AlphaVantageProperties alphaVantageProperties;
    private final AlphaVantageSecuritySearchMapper mapper;

    @Override
    public Flux<Security> search(String tickerSymbol) {
        return webClientBuilder.build()
                .get()
                .uri(alphaVantageProperties.getBaseUrl(), uriBuilder -> uriBuilder
                        .path("/query")
                        .queryParam("function", "SYMBOL_SEARCH")
                        .queryParam("keywords", tickerSymbol)
                        .queryParam("apikey", alphaVantageProperties.getApiKey())
                        .build())
                .retrieve()
                .bodyToMono(AlphaVantageSearchResponse.class)
                .flatMapMany(response -> {
                    if (response == null || response.getBestMatches() == null) {
                        return Flux.empty();
                    }
                    return Flux.fromIterable(response.getBestMatches());
                })
                .map(mapper::toSecurity);
    }
}