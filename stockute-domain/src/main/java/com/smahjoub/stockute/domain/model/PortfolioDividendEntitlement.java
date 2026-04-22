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

@Table("portfolio_dividend_entitlements")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PortfolioDividendEntitlement extends Entity {

    @Id
    @Column("id")
    private Long id;

    @Column("portfolio_ref_id")
    private Long portfolioRefId;

    @Column("asset_ref_id")
    private Long assetRefId;

    @Column("security_dividend_ref_id")
    private Long securityDividendRefId;

    @Column("security_ref_id")
    private Long securityRefId;

    @Column("eligible_shares")
    private BigDecimal eligibleShares;

    @Column("dividend_per_share")
    private BigDecimal dividendPerShare;

    @Column("gross_amount")
    private BigDecimal grossAmount;

    @Column("currency_ref_id")
    private Long currencyRefId;

    @Column("ex_date")
    private LocalDateTime exDate;

    @Column("payment_date")
    private LocalDateTime paymentDate;

    @Column("status")
    private String status;

    @Column("created_date")
    private LocalDateTime createdDate;

    @Column("last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column("version")
    private Long version;
}