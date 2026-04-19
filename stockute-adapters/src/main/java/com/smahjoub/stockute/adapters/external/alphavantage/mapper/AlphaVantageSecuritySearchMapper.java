package com.smahjoub.stockute.adapters.external.alphavantage.mapper;

import com.smahjoub.stockute.adapters.external.alphavantage.dto.AlphaVantageBestMatchDTO;
import com.smahjoub.stockute.domain.model.Security;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlphaVantageSecuritySearchMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currencyRefId", ignore = true)
    @Mapping(target = "active", constant = "true")
    Security toSecurity(AlphaVantageBestMatchDTO dto);
}