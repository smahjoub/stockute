package com.smahjoub.stockute.adapters.restful.dividend.mapper;

import com.smahjoub.stockute.adapters.restful.dividend.dto.PortfolioDividendStatsDto;
import com.smahjoub.stockute.application.port.dividend.in.response.PortfolioDividendStats;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PortfolioDividendStatsMapper {
    PortfolioDividendStatsDto toDto(PortfolioDividendStats response);
}