package com.smahjoub.stockute.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("portfolios")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Portfolio extends Entity {
    @Id
    private Long id;

    private String name;

    private String notes;

    private Long currencyRefId;

    private Long userRefId;
}