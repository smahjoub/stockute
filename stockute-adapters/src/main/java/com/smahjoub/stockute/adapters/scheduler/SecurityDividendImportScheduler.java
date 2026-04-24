package com.smahjoub.stockute.adapters.scheduler;

import com.smahjoub.stockute.application.service.dividend.SecurityDividendImportService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SecurityDividendImportScheduler {

    private final SecurityDividendImportService securityDividendImportService;
    private final AtomicBoolean running = new AtomicBoolean(false);

    @Scheduled(cron = "${stockute.batch.dividend-import.cron:0 0 2 * * *}", zone = "Europe/Luxembourg")
    public void importDividendHistory() {
        if (!running.compareAndSet(false, true)) {
            log.info("Dividend history import skipped because a previous run is still in progress");
            return;
        }

        securityDividendImportService.importDividendHistoryForAllSecurities()
                .doOnSubscribe(subscription -> log.info("Starting dividend history import"))
                .doOnSuccess(unused -> log.info("Dividend history import completed"))
                .doOnError(error -> log.error("Dividend history import failed", error))
                .doFinally(signalType -> running.set(false))
                .subscribe();
    }
}