package com.smahjoub.stockute.adapters.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "earningsapi")
public class EarningsApiProperties {
    private String baseUrl;
    private String apiKey;
}