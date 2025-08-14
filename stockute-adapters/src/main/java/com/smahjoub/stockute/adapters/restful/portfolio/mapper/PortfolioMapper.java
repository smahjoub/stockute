package com.smahjoub.stockute.adapters.restful.portfolio.mapper;

import com.smahjoub.stockute.adapters.restful.portfolio.dto.PortfolioDTO;
import com.smahjoub.stockute.domain.model.Portfolio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PortfolioMapper {
    @Mapping(source = "currency.name", target = "currency")
    PortfolioDTO toPortfolioDTO(Portfolio portfolio);
}

