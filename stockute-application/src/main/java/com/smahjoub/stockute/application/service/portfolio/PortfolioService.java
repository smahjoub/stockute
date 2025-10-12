package com.smahjoub.stockute.application.service.portfolio;


import com.smahjoub.stockute.application.port.currency.out.CurrencyPort;
import com.smahjoub.stockute.application.port.membership.in.UserUseCase;
import com.smahjoub.stockute.application.port.portfolio.in.PortfolioUseCase;
import com.smahjoub.stockute.application.port.portfolio.out.PortfolioPort;
import com.smahjoub.stockute.domain.model.Portfolio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class PortfolioService implements PortfolioUseCase {
    private final PortfolioPort repository;
    private final UserUseCase userUseCase;
    private final CurrencyPort currencyPort;

    @Override
    public Mono<Portfolio> createPortfolio(final String userName, final Portfolio portfolio) {
        return userUseCase.getUserByUsername(userName)
                .flatMap(user -> {
                    portfolio.setUserRefId(user.getId());
                    return repository.save(portfolio);
                }).flatMap(p -> getUserPortfolio(userName, p.getId()));
    }

    @Override
    public Mono<Portfolio> updatePortfolio(final String userName, final Portfolio portfolio) {
        return userUseCase.getUserByUsername(userName)
                .flatMap(user -> {
                    portfolio.setUserRefId(user.getId());
                    return repository.save(portfolio);
                });
    }

    @Override
    public Mono<Portfolio> getUserPortfolio(final String userName, final Long id) {
        return userUseCase.getUserByUsername(userName)
                .flatMap(user ->
                        repository.findById(id).flatMap(p -> {
                            if (!p.getUserRefId().equals(user.getId())) {
                                return Mono.error(new IllegalArgumentException("You do not have permission to delete this portfolio."));
                            }
                            return Mono.just(p);
                        })
                ).flatMap(portfolio ->
                        currencyPort.findById(portfolio.getCurrencyRefId())
                                .map(currency -> {
                                    portfolio.setCurrency(currency);
                                    return portfolio;
                                })
                );
    }

    @Override
    public Flux<Portfolio> getAllUserPortfolios(final String userName) {
        return userUseCase.getUserByUsername(userName)
                .flatMapMany(user -> repository.findAllByUserRefId(user.getId()));
    }

    @Override
    public Mono<Void> removePortfolio(final String userName, final Long id) {
        return getUserPortfolio(userName, id)
                .flatMap(p -> repository.deleteById(id))
                .then();
    }
}