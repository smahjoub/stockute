package com.smahjoub.stockute.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories(basePackages = "com.smahjoub.stockute.adapters.persistence")
@SpringBootApplication
public class StockuteApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockuteApplication.class, args);
    }

}