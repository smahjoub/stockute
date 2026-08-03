package com.smahjoub.stockute.adapters.restful.dividend.mapper;

import com.smahjoub.stockute.adapters.restful.dividend.dto.DividendCalendarItemDto;
import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendCalendarItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DividendCalendarMapper {
    DividendCalendarItemDto toDto(SecurityDividendCalendarItem item);
}
