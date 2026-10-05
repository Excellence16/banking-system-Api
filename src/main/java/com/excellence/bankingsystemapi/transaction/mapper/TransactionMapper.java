package com.excellence.bankingsystemapi.transaction.mapper;

import com.excellence.bankingsystemapi.model.Transaction;
import com.excellence.bankingsystemapi.transaction.dto.response.TransactionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {


    @Mapping(source = "senderBankSnapshot", target = "senderBank")

    @Mapping(source = "senderNameSnapshot", target = "senderName")

    @Mapping(source = "senderAccountSnapshot", target = "senderAccountNumber")

    @Mapping(source = "receiverNameSnapshot", target = "receiverName")

    @Mapping(source = "receiverAccountSnapshot", target = "receiverAccountNumber")

    @Mapping(source = "receiverBankSnapshot", target = "receiverBank")

    @Mapping(source = "status", target = "transactionStatus")
    TransactionResponseDTO toResponseDTO(Transaction transaction);


}
