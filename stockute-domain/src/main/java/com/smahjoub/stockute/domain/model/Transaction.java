package com.smahjoub.stockute.domain.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("transactions")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Transaction extends Entity {

    @Id
    @Column("id")
    private Long id;

    @Column("portfolio_ref_id")
    private Long portfolioRefId;

    @Column("asset_ref_id")
    private Long assetRefId;

    @Column("type")
    private String type;

    @Column("quantity")
    private double quantity;

    @Column("price")
    private BigDecimal price;

    @Column("fees")
    private BigDecimal fees;

    @Column("transaction_date")
    private LocalDateTime transactionDate;

    @Column("notes")
    private String notes;
}
