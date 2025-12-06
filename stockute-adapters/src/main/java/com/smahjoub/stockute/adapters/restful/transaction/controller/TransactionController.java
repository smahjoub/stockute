package com.smahjoub.stockute.adapters.restful.transaction.controller;

import com.smahjoub.stockute.adapters.restful.transaction.dto.CreateTransactionDTO;
import com.smahjoub.stockute.adapters.restful.transaction.dto.TransactionDTO;
import com.smahjoub.stockute.adapters.restful.transaction.mapper.CreateTransactionMapper;
import com.smahjoub.stockute.adapters.restful.transaction.mapper.TransactionMapper;
import com.smahjoub.stockute.application.service.transaction.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
@AllArgsConstructor
public class TransactionController {
    private final CreateTransactionMapper createTransactionMapper;
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;

    @PostMapping("/portfolios/{portfolioId}/transactions")
    public Mono<TransactionDTO> create(@PathVariable("portfolioId") final Long portfolioId, @RequestBody final CreateTransactionDTO createTransactionDTO) {
        return transactionService.createTransaction(createTransactionDTO.assetName(),
                        createTransactionDTO.ticker(), createTransactionDTO.exchange(),
                        createTransactionMapper.toDomain(createTransactionDTO), portfolioId)
                .map(transactionMapper::toDto);
    }

    @GetMapping("/portfolios/{portfolioId}/assets/{assetId}/transactions")
    public Flux<TransactionDTO> getAllTransactionsForAssetInPortfolio(@PathVariable("portfolioId") final Long portfolioId,
                                                                      @PathVariable("assetId") final Long asserId) {
        return transactionService.getAllTransactionsForAssetInPortfolio(portfolioId, asserId)
                .map(transactionMapper::toDto);
    }
}
