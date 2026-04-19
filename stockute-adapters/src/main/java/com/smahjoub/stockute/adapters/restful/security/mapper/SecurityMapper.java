package com.smahjoub.stockute.adapters.restful.security.mapper;

import com.smahjoub.stockute.adapters.restful.security.dto.SecurityDTO;
import com.smahjoub.stockute.domain.model.Security;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SecurityMapper {

    SecurityDTO toDto(Security security);
}