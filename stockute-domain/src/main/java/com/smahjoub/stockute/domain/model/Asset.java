package com.smahjoub.stockute.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("assets")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Asset extends Entity {
    @Id
    private Long id ;

    private String ticker;

    private String exchange;

    private String name;

    private double quantity;

    private BigDecimal price;

    private Long portfolioRefId;

    private Long currencyRefId;
}