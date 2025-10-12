package com.smahjoub.stockute.adapters.restful.portfolio.mapper;

import com.smahjoub.stockute.adapters.restful.portfolio.dto.UpdatePortfolioDTO;
import com.smahjoub.stockute.domain.model.Currency;
import com.smahjoub.stockute.domain.model.Portfolio;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;

class UpdatePortfolioRecordMapperTest {

    private final UpdatePortfolioRecordMapper mapper = Mappers.getMapper(UpdatePortfolioRecordMapper.class);

    @Test
    void testToPortfolioMapsCorrectly() {
        String name = "Updated Portfolio";
        String notes = "Updated notes";
        Long currencyRefId = 999L;
        UpdatePortfolioDTO dto = new UpdatePortfolioDTO(name, notes, currencyRefId);

        Portfolio portfolio = mapper.toPortfolio(dto);

        assertNull(portfolio.getId());
        assertNull(portfolio.getUserRefId());
        assertNull(portfolio.getCurrency());
        assertEquals(name, portfolio.getName());
        assertEquals(notes, portfolio.getNotes());
        assertEquals(currencyRefId, portfolio.getCurrencyRefId());
        assertNotNull(portfolio.getCreatedDate());
        assertNotNull(portfolio.getLastModifiedDate());
        LocalDateTime now = LocalDateTime.now();
        assertTrue(portfolio.getCreatedDate().isBefore(now.plusSeconds(1)));
        assertTrue(portfolio.getCreatedDate().isAfter(now.minusSeconds(1)));
        assertTrue(portfolio.getLastModifiedDate().isBefore(now.plusSeconds(1)));
        assertTrue(portfolio.getLastModifiedDate().isAfter(now.minusSeconds(1)));
    }

    @Test
    void testUpdatePortfolioFromDtoUpdatesCorrectly() {

        String newName = "Updated Portfolio";
        String newNotes = "Updated notes";
        Long newCurrencyRefId = 999L;
        UpdatePortfolioDTO dto = new UpdatePortfolioDTO(newName, newNotes, newCurrencyRefId);

        Long originalId = 1L;
        Long originalUserRefId = 2L;
        Currency originalCurrency = new Currency();
        LocalDateTime originalCreatedDate = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        Long originalVersion = 5L;

        Portfolio existingPortfolio = new Portfolio();
        existingPortfolio.setId(originalId);
        existingPortfolio.setName("Old Name");
        existingPortfolio.setNotes("Old Notes");
        existingPortfolio.setCurrencyRefId(888L);
        existingPortfolio.setUserRefId(originalUserRefId);
        existingPortfolio.setCurrency(originalCurrency);
        existingPortfolio.setCreatedDate(originalCreatedDate);
        existingPortfolio.setVersion(originalVersion);

        Portfolio updatedPortfolio = mapper.updatePortfolioFromDto(dto, existingPortfolio);

        // Assert
        assertEquals(originalId, updatedPortfolio.getId());
        assertEquals(originalUserRefId, updatedPortfolio.getUserRefId());
        assertEquals(originalCurrency, updatedPortfolio.getCurrency());
        assertEquals(originalCreatedDate, updatedPortfolio.getCreatedDate());

        assertEquals(newName, updatedPortfolio.getName());
        assertEquals(newNotes, updatedPortfolio.getNotes());
        assertEquals(newCurrencyRefId, updatedPortfolio.getCurrencyRefId());


        assertEquals(originalVersion + 1, updatedPortfolio.getVersion());


        assertNotEquals(originalCreatedDate, updatedPortfolio.getLastModifiedDate());
        assertNotNull(updatedPortfolio.getLastModifiedDate());
        LocalDateTime now = LocalDateTime.now();
        assertTrue(updatedPortfolio.getLastModifiedDate().isBefore(now.plusSeconds(1)));
        assertTrue(updatedPortfolio.getLastModifiedDate().isAfter(now.minusSeconds(1)));
    }

    @Test
    void testUpdatePortfolioFromDtoHandlesNullFieldsInDTO() {
        UpdatePortfolioDTO dto = new UpdatePortfolioDTO(null, null, null);

        Long originalId = 1L;
        Long originalUserRefId = 2L;
        Currency originalCurrency = new Currency();
        LocalDateTime originalCreatedDate = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        Long originalVersion = 5L;

        Portfolio existingPortfolio = new Portfolio();
        existingPortfolio.setId(originalId);
        existingPortfolio.setName("Old Name");
        existingPortfolio.setNotes("Old Notes");
        existingPortfolio.setCurrencyRefId(888L);
        existingPortfolio.setUserRefId(originalUserRefId);
        existingPortfolio.setCurrency(originalCurrency);
        existingPortfolio.setCreatedDate(originalCreatedDate);
        existingPortfolio.setVersion(originalVersion);


        Portfolio updatedPortfolio = mapper.updatePortfolioFromDto(dto, existingPortfolio);

        assertEquals(originalId, updatedPortfolio.getId());
        assertEquals(originalUserRefId, updatedPortfolio.getUserRefId());
        assertEquals(originalCurrency, updatedPortfolio.getCurrency());
        assertEquals(originalCreatedDate, updatedPortfolio.getCreatedDate());

        assertNull(updatedPortfolio.getName());
        assertNull(updatedPortfolio.getNotes());
        assertNull(updatedPortfolio.getCurrencyRefId());

        assertEquals(originalVersion + 1, updatedPortfolio.getVersion());

        assertNotEquals(originalCreatedDate, updatedPortfolio.getLastModifiedDate());
        LocalDateTime now = LocalDateTime.now();
        assertTrue(updatedPortfolio.getLastModifiedDate().isBefore(now.plusSeconds(1)));
        assertTrue(updatedPortfolio.getLastModifiedDate().isAfter(now.minusSeconds(1)));
    }
}
