package com.smahjoub.stockute.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("assets")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Asset extends Entity {
    @Id
    private Long id;
    @Column("ticker")
    private String ticker;
    @Column("exchange")
    private String exchange;
    @Column("name")
    private String name;
    @Column("quantity")
    private double quantity;
    @Column("average_price")
    private BigDecimal averagePrice;
    @Column("portfolio_ref_id")
    private Long portfolioRefId;
    @Column("currency_ref_id")
    private Long currencyRefId;
    @Transient
    private Currency currency;
}