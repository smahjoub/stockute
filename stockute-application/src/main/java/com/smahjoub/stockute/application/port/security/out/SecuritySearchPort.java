package com.smahjoub.stockute.application.port.security.out;

import com.smahjoub.stockute.domain.model.Security;
import reactor.core.publisher.Flux;

public interface SecuritySearchPort {
    Flux<Security> search(String tickerSymbol);
}
