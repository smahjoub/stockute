package com.smahjoub.stockute.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Table("currencies")
public class Currency extends Entity {
    @Id
    private Long id;
    private String name;
    private String symbol;
    private String code;
}
