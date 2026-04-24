package com.smahjoub.stockute.application.port.dividend.out;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DividendHistoryItem(LocalDateTime exDate, LocalDateTime declarationDate, LocalDateTime recordDate,
                                  LocalDateTime paymentDate, BigDecimal amount) {
}