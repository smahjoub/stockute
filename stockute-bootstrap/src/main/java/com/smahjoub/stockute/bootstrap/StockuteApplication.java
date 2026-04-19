package com.smahjoub.stockute.bootstrap;

import com.smahjoub.stockute.adapters.config.AlphaVantageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories(basePackages = "com.smahjoub.stockute.adapters.persistence")
@ComponentScan(basePackages = "com.smahjoub.stockute")
@EnableConfigurationProperties(AlphaVantageProperties.class)
@SpringBootApplication
public class StockuteApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockuteApplication.class, args);
    }
}