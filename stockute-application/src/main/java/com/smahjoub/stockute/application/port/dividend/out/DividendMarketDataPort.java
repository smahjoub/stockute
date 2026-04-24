package com.smahjoub.stockute.application.port.dividend.out;

import reactor.core.publisher.Flux;

public interface DividendMarketDataPort {

    Flux<DividendHistoryItem> getDividendHistory(String symbol);
}