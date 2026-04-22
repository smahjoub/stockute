package com.smahjoub.stockute.adapters.persistence.dividend;

import com.smahjoub.stockute.domain.model.SecurityDividend;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SecurityDividendRepository
        extends ReactiveCrudRepository<SecurityDividend, Long> {
}
