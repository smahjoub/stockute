package com.smahjoub.stockute.adapters.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "earningsapi")
public class EarningsApiProperties {
    private String baseUrl;
    private String apiKey;
}