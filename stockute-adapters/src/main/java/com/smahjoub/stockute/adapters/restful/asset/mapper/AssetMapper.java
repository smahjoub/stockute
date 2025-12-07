package com.smahjoub.stockute.adapters.restful.asset.mapper;

import com.smahjoub.stockute.adapters.restful.asset.dto.AssetDTO;
import com.smahjoub.stockute.domain.model.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    @Mapping(source = "currency.code", target = "currency")
    AssetDTO toAssetDTO(Asset asset);
}
