package com.smahjoub.stockute.adapters.restful.currency.mapper;

import com.smahjoub.stockute.adapters.restful.currency.dto.CurrencyDTO;
import com.smahjoub.stockute.domain.model.Currency;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyDTO toDTO(Currency currency);
}