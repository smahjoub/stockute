package com.smahjoub.stockute.adapters.restful.portfolio.mapper;

import com.smahjoub.stockute.adapters.restful.portfolio.dto.CreatePortfolioDTO;
import com.smahjoub.stockute.domain.model.Portfolio;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class CreatePortfolioRecordMapperTest {

    private final CreatePortfolioRecordMapper mapper = Mappers.getMapper(CreatePortfolioRecordMapper.class);


    @Test
    void testToPortfolioMapsCorrectly() {

        String name = "Test Portfolio";
        String notes = "This is a test portfolio.";
        Long currencyRefId = 100L;
        CreatePortfolioDTO dto = new CreatePortfolioDTO(name, notes, currencyRefId);

        Portfolio portfolio = mapper.toPortfolio(dto);


        assertEquals(name, portfolio.getName());
        assertEquals(notes, portfolio.getNotes());
        assertEquals(currencyRefId, portfolio.getCurrencyRefId());


        assertNull(portfolio.getId());
        assertNull(portfolio.getUserRefId());
        assertNull(portfolio.getCurrency());

        assertNotNull(portfolio.getCreatedDate());
        assertNotNull(portfolio.getLastModifiedDate());
        assertEquals(0L, portfolio.getVersion());


        LocalDateTime now = LocalDateTime.now();
        assertTrue(portfolio.getCreatedDate().isBefore(now.plusSeconds(1)));
        assertTrue(portfolio.getCreatedDate().isAfter(now.minusSeconds(1)));
        assertTrue(portfolio.getLastModifiedDate().isBefore(now.plusSeconds(1)));
        assertTrue(portfolio.getLastModifiedDate().isAfter(now.minusSeconds(1)));
    }

    @Test
    void testToPortfolioHandlesNullFieldsInDTO() {
        CreatePortfolioDTO dto = new CreatePortfolioDTO(null, null, null);

        Portfolio portfolio = mapper.toPortfolio(dto);

        assertNull(portfolio.getName());
        assertNull(portfolio.getNotes());
        assertNull(portfolio.getCurrencyRefId());

        assertNull(portfolio.getId());
        assertNull(portfolio.getUserRefId());
        assertNull(portfolio.getCurrency());

        assertNotNull(portfolio.getCreatedDate());
        assertNotNull(portfolio.getLastModifiedDate());
        assertEquals(0L, portfolio.getVersion()); // Should be set by BeforeMapping
    }
}