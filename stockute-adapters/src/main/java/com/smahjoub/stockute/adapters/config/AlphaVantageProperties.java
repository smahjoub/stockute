package com.smahjoub.stockute.adapters.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "alphavantage")
public class AlphaVantageProperties {
    private String baseUrl;
    private String apiKey;
}