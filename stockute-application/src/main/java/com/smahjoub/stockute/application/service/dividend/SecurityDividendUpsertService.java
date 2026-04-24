package com.smahjoub.stockute.application.service.dividend;

import com.smahjoub.stockute.application.port.dividend.out.DividendHistoryItem;
import com.smahjoub.stockute.application.port.dividend.out.SecurityDividendPort;
import com.smahjoub.stockute.domain.model.Security;
import com.smahjoub.stockute.domain.model.SecurityDividend;
import com.smahjoub.stockute.domain.model.enums.DividendStatus;
import com.smahjoub.stockute.domain.model.enums.DividendType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SecurityDividendUpsertService {

    private final SecurityDividendPort securityDividendPort;

    public Mono<SecurityDividend> upsert(final Security security, final DividendHistoryItem item) {
        final LocalDateTime exDate = item.exDate();
        final LocalDateTime paymentDate = item.paymentDate();
        final BigDecimal dividendPerShare = item.amount();

        return securityDividendPort.findByBusinessKey(
                        security.getId(),
                        exDate,
                        paymentDate,
                        dividendPerShare
                )
                .flatMap(existing -> {
                    existing.setDeclaredDate(item.declarationDate());
                    existing.setRecordDate(item.recordDate());
                    existing.setPaymentDate(paymentDate);
                    existing.setDividendPerShare(dividendPerShare);
                    existing.setStatus(DividendStatus.CONFIRMED);
                    existing.setLastModifiedDate(LocalDateTime.now());
                    return securityDividendPort.save(existing);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    SecurityDividend dividend = new SecurityDividend();
                    dividend.setSecurityRefId(security.getId());
                    dividend.setDividendType(DividendType.REGULAR);
                    dividend.setStatus(DividendStatus.CONFIRMED);
                    dividend.setExDate(exDate);
                    dividend.setDeclaredDate(item.declarationDate());
                    dividend.setRecordDate(item.recordDate());
                    dividend.setPaymentDate(paymentDate);
                    dividend.setDividendPerShare(dividendPerShare);
                    dividend.setCurrencyRefId(security.getCurrencyRefId());
                    dividend.setCreatedDate(LocalDateTime.now());
                    dividend.setLastModifiedDate(LocalDateTime.now());
                    dividend.setVersion(0L);
                    return securityDividendPort.save(dividend);
                }));
    }
}