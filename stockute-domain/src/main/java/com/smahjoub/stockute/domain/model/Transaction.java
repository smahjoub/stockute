package com.smahjoub.stockute.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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

    @Column("security_ref_id")
    private Long securityRefId;

    @Column("type")
    private String type;

    @Column("quantity")
    private BigDecimal quantity;

    @Column("price")
    private BigDecimal price;

    @Column("fees")
    private BigDecimal fees;

    @Column("gross_amount")
    private BigDecimal grossAmount;

    @Column("tax_amount")
    private BigDecimal taxAmount;

    @Column("net_amount")
    private BigDecimal netAmount;

    @Column("currency_ref_id")
    private Long currencyRefId;

    @Column("fx_rate")
    private BigDecimal fxRate;

    @Column("amount_in_portfolio_currency")
    private BigDecimal amountInPortfolioCurrency;

    @Column("dividend_per_share")
    private BigDecimal dividendPerShare;

    @Column("shares_held_on_record_date")
    private BigDecimal sharesHeldOnRecordDate;

    @Column("record_date")
    private LocalDateTime recordDate;

    @Column("payment_date")
    private LocalDateTime paymentDate;

    @Column("related_transaction_id")
    private Long relatedTransactionId;

    @Column("transaction_date")
    private LocalDateTime transactionDate;

    @Column("notes")
    private String notes;

    @Column("created_date")
    private LocalDateTime createdDate;

    @Column("last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column("version")
    private Long version;
}