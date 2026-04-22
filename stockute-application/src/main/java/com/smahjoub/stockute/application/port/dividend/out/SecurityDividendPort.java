package com.smahjoub.stockute.application.port.dividend.out;

import com.smahjoub.stockute.domain.model.SecurityDividend;
import reactor.core.publisher.Mono;

public interface SecurityDividendPort {

    Mono<SecurityDividend> findById(Long id);
}