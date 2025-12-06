package com.smahjoub.stockute.adapters.restful.transaction.mapper;

import com.smahjoub.stockute.adapters.restful.transaction.dto.CreateTransactionDTO;
import com.smahjoub.stockute.domain.model.Transaction;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;


@Mapper(componentModel = "spring")
public abstract class CreateTransactionMapper {

    /**
     * Maps a CreateTransactionDTO to a Transaction entity.
     *
     * @param dto the CreateTransactionDTO to map
     * @return the mapped Transaction entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "portfolioRefId", ignore = true)
    @Mapping(target = "assetRefId", ignore = true)
    public abstract Transaction toDomain(CreateTransactionDTO dto);


    @BeforeMapping
    protected void setCreationAndModificationDate(CreateTransactionDTO dto, @MappingTarget Transaction transaction) {
        final LocalDateTime now = LocalDateTime.now();
        transaction.setCreatedDate(now);
        transaction.setLastModifiedDate(now);
        transaction.setVersion(0L);
    }

}
