
package com.smahjoub.stockute.adapters.restful.security.controller;

import com.smahjoub.stockute.adapters.restful.security.dto.SecurityDTO;
import com.smahjoub.stockute.adapters.restful.security.mapper.SecurityMapper;
import com.smahjoub.stockute.application.port.security.in.SearchSecurityUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
@RequestMapping("/v1/securities")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SecurityController {

    private final SearchSecurityUseCase searchSecurityUseCase;
    private final SecurityMapper securityMapper;

    @GetMapping("/search")
    public Flux<SecurityDTO> search(@RequestParam("tickerSymbol") String tickerSymbol) {
        return searchSecurityUseCase.search(tickerSymbol)
                .map(securityMapper::toDto);
    }
}