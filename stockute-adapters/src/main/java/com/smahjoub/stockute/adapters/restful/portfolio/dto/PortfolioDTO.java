package com.smahjoub.stockute.adapters.restful.portfolio.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDTO {
    private Long id;
    private String name;
    private String notes;
    private Long currencyRefId;
    private Long userRefId;
    private java.time.LocalDateTime createdDate;
    private java.time.LocalDateTime lastModifiedDate;
    private Long version;
}

