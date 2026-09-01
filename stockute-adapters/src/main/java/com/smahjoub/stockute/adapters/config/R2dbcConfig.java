package com.smahjoub.stockute.adapters.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import java.util.List;
import com.smahjoub.stockute.adapters.persistence.converter.DividendStatusToStringConverter;
import com.smahjoub.stockute.adapters.persistence.converter.DividendTypeToStringConverter;
import com.smahjoub.stockute.adapters.persistence.converter.StringToDividendStatusConverter;
import com.smahjoub.stockute.adapters.persistence.converter.StringToDividendTypeConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

@Configuration
@EnableR2dbcAuditing
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    private final ConnectionFactory connectionFactory;

    public R2dbcConfig(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Bean
    @Override
    public R2dbcCustomConversions r2dbcCustomConversions() {
        return new R2dbcCustomConversions(
                getStoreConversions(),
                List.of(
                        new StringToDividendTypeConverter(),
                        new DividendTypeToStringConverter(),
                        new StringToDividendStatusConverter(),
                        new DividendStatusToStringConverter()
                )
        );
    }
}