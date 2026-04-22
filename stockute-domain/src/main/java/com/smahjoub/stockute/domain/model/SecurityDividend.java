package com.smahjoub.stockute.domain.model;

import com.smahjoub.stockute.domain.model.enums.DividendType;
import com.smahjoub.stockute.domain.model.enums.DividendStatus;
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

@Table("security_dividends")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SecurityDividend extends Entity {

    @Id
    @Column("id")
    private Long id;

    @Column("security_ref_id")
    private Long securityRefId;

    @Column("dividend_type")
    private DividendType dividendType;

    @Column("status")
    private DividendStatus status;

    @Column("ex_date")
    private LocalDateTime exDate;

    @Column("record_date")
    private LocalDateTime recordDate;

    @Column("payment_date")
    private LocalDateTime paymentDate;

    @Column("dividend_per_share")
    private BigDecimal dividendPerShare;

    @Column("currency_ref_id")
    private Long currencyRefId;

    @Column("declared_date")
    private LocalDateTime declaredDate;

    @Column("source")
    private String source;

    @Column("notes")
    private String notes;

    @Column("created_date")
    private LocalDateTime createdDate;

    @Column("last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column("version")
    private Long version;
}