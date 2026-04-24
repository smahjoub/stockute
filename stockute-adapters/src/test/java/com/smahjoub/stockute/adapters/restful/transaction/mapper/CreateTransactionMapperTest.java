package com.smahjoub.stockute.adapters.restful.transaction.mapper;

import com.smahjoub.stockute.adapters.restful.transaction.dto.CreateTransactionDTO;
import com.smahjoub.stockute.domain.model.Transaction;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CreateTransactionMapperTest {

    private final CreateTransactionMapper mapper =
            Mappers.getMapper(CreateTransactionMapper.class);

    @Test
    void toDomain_shouldMapFieldsAndSetAuditFields() {
        // GIVEN
        LocalDateTime txDate = LocalDateTime.of(2026, 4, 24, 10, 30);

        CreateTransactionDTO dto = new CreateTransactionDTO(
                "Apple Inc",
                5.0,
                1L,
                2L,
                new BigDecimal("150.25"),
                new BigDecimal("1.50"),
                "Test notes",
                "BUY",
                txDate
        );

        // WHEN
        Transaction result = mapper.toDomain(dto);

        // THEN — mapped fields
        assertThat(result.getType()).isEqualTo("BUY");
        assertThat(result.getQuantity()).isEqualByComparingTo("5.0");
        assertThat(result.getPrice()).isEqualByComparingTo("150.25");
        assertThat(result.getFees()).isEqualByComparingTo("1.50");
        assertThat(result.getNotes()).isEqualTo("Test notes");
        assertThat(result.getTransactionDate()).isEqualTo(txDate);
        assertThat(result.getCurrencyRefId()).isEqualTo(1L);
        assertThat(result.getSecurityRefId()).isEqualTo(2L);

        // THEN — fields ignored by @Mapping
        assertThat(result.getId()).isNull();
        assertThat(result.getPortfolioRefId()).isNull();
        assertThat(result.getAssetRefId()).isNull();

        // THEN — @BeforeMapping audit fields
        assertThat(result.getCreatedDate()).isNotNull();
        assertThat(result.getLastModifiedDate()).isNotNull();
        assertThat(result.getVersion()).isEqualTo(0L);

        // createdDate and lastModifiedDate must be equal
        assertThat(result.getCreatedDate()).isEqualTo(result.getLastModifiedDate());
    }
}
