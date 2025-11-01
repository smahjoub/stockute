package com.smahjoub.stockute.adapters.restful.transaction.mapper;

import com.smahjoub.stockute.adapters.restful.transaction.dto.TransactionDTO;
import com.smahjoub.stockute.domain.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDTO toDto(Transaction transaction);
}
