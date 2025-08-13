package com.smahjoub.stockute.adapters.persistence.portfolio;


import com.smahjoub.stockute.domain.model.Portfolio;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends R2dbcRepository<Portfolio, Long> {

}
