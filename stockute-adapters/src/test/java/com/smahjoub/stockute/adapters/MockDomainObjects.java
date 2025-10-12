package com.smahjoub.stockute.adapters;


import com.smahjoub.stockute.adapters.restful.portfolio.dto.CreatePortfolioDTO;
import com.smahjoub.stockute.adapters.restful.portfolio.dto.PortfolioDTO;
import com.smahjoub.stockute.adapters.restful.portfolio.dto.UpdatePortfolioDTO;
import com.smahjoub.stockute.domain.model.Currency;
import com.smahjoub.stockute.domain.model.Portfolio;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class MockDomainObjects {

    public static final Currency USD = new Currency(1L, "US Dollar", "$", "USD");
    public static final Currency EUR = new Currency(2L, "Euro", "€", "EUR");

    public static final Portfolio PORTFOLIO_1;
    public static final Portfolio PORTFOLIO_2;

    public static final CreatePortfolioDTO CREATE_PORTFOLIO_DTO_1_MOCK =
            new CreatePortfolioDTO("Growth Portfolio", "Aggressive growth strategy", USD.getId());


    public static final CreatePortfolioDTO CREATE_PORTFOLIO_DTO_2_MOCK =
            new CreatePortfolioDTO("Retirement Portfolio", "Long-term retirement savings", EUR.getId());
    public static final PortfolioDTO PORTFOLIO_DTO_1_MOCK =
            new PortfolioDTO(1L, "Growth Portfolio", "Aggressive growth strategy",
                    USD.getName(), LocalDateTime.of(2020, 1, 1, 1, 1),
                    LocalDateTime.of(2020, 1, 1, 1, 1), 1L);
    public static final UpdatePortfolioDTO UPDATE_PORTFOLIO_DTO_1_MOCK =
            new UpdatePortfolioDTO("Growth Portfolio Updated", "Updated notes", USD.getId());
    public static final PortfolioDTO PORTFOLIO_DTO_2_MOCK =
            new PortfolioDTO(2L, "Retirement Portfolio", "Long-term retirement savings",
                    EUR.getName(), LocalDateTime.of(2021, 2, 2, 2, 2),
                    LocalDateTime.of(2021, 2, 2, 2, 2), 2L);

    static {
        PORTFOLIO_1 = new Portfolio();
        PORTFOLIO_1.setId(10L);
        PORTFOLIO_1.setName("Growth Portfolio");
        PORTFOLIO_1.setNotes("Aggressive growth strategy");
        PORTFOLIO_1.setCurrencyRefId(USD.getId());
        PORTFOLIO_1.setUserRefId(100L);
        PORTFOLIO_1.setCurrency(USD);

        PORTFOLIO_2 = new Portfolio();
        PORTFOLIO_2.setId(11L);
        PORTFOLIO_2.setName("Retirement Portfolio");
        PORTFOLIO_2.setNotes("Long-term retirement savings");
        PORTFOLIO_2.setCurrencyRefId(EUR.getId());
        PORTFOLIO_2.setUserRefId(101L);
        PORTFOLIO_2.setCurrency(EUR);
    }
}