package com.smahjoub.stockute.adapters.restful.currency.controller;

import com.smahjoub.stockute.adapters.restful.currency.dto.CurrencyDTO;
import com.smahjoub.stockute.adapters.restful.currency.mapper.CurrencyMapper;
import com.smahjoub.stockute.application.port.currency.in.CurrencyUseCase;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
@AllArgsConstructor
@RequestMapping("/v1/currencies")
public class CurrencyController {

    private final CurrencyUseCase currencyUseCase;
    private final CurrencyMapper currencyMapper;

    @GetMapping
    public Flux<CurrencyDTO> getAllCurrencies() {
        return currencyUseCase.getAllCurrencies()
                .map(currencyMapper::toDTO);
    }
}