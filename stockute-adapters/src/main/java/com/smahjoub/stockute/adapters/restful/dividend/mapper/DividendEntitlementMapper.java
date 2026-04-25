package com.smahjoub.stockute.adapters.restful.dividend.mapper;

import com.smahjoub.stockute.adapters.restful.dividend.dto.DividendEntitlementDto;
import com.smahjoub.stockute.domain.model.PortfolioDividendEntitlement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DividendEntitlementMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "eligibleShares", target = "eligibleShares")
    @Mapping(source = "dividendPerShare", target = "dividendPerShare")
    @Mapping(source = "grossAmount", target = "grossAmount")
    @Mapping(source = "currencyRefId", target = "currencyRefId")
    @Mapping(source = "exDate", target = "exDate")
    @Mapping(source = "paymentDate", target = "paymentDate")
    @Mapping(source = "status", target = "status")
    DividendEntitlementDto toDto(PortfolioDividendEntitlement entitlement);
}
