package com.smahjoub.stockute.application.port.security.in;

import com.smahjoub.stockute.domain.model.Security;
import reactor.core.publisher.Flux;

public interface SearchSecurityUseCase {

    Flux<Security> search(String tickerSymbol);
}
