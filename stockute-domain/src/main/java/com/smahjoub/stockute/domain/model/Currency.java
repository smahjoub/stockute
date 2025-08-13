package com.smahjoub.stockute.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table("currencies")
public class Currency extends Entity {
    @Id
    private Long id;
    private String name;
    private String symbol;
    private String code;
}
