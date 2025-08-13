package com.smahjoub.stockute.adapters.restful.portfolio.mapper;

import com.smahjoub.stockute.adapters.restful.portfolio.dto.PortfolioDTO;
import com.smahjoub.stockute.domain.model.Portfolio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PortfolioMapper {
    PortfolioDTO toPortfolioDTO(Portfolio portfolio);
    Portfolio toPortfolio(PortfolioDTO dto);
}

