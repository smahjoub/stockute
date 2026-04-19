package com.smahjoub.stockute.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("securities")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Security extends Entity {

    @Id
    @Column("id")
    private Long id;

    @Column("symbol")
    private String symbol;

    @Column("name")
    private String name;

    @Column("type")
    private String type;

    @Column("region")
    private String region;

    @Column("market_open")
    private String marketOpen;

    @Column("market_close")
    private String marketClose;

    @Column("timezone")
    private String timezone;

    @Column("currency_ref_id")
    private Long currencyRefId;

    @Transient
    private String currency;

    @Column("is_active")
    private boolean active;
}