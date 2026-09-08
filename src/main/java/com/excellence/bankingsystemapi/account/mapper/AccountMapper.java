

package com.excellence.bankingsystemapi.account.mapper;

import com.excellence.bankingsystemapi.account.dto.response.AccountBalanceResponseDTO;
import com.excellence.bankingsystemapi.account.dto.response.AccountResponseDTO;
import com.excellence.bankingsystemapi.account.dto.response.AccountSummaryResponseDTO;
import com.excellence.bankingsystemapi.model.Account;
import com.excellence.bankingsystemapi.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "customerNumber",
            source = "customer.customerNumber")

    @Mapping(
            target = "customerName",
            expression= "java(account.getCustomer().getFirstName() + \" \" + account.getCustomer().getLastName())"
    )
    AccountResponseDTO toResponse(Account account);


    AccountSummaryResponseDTO toSummaryResponse(Account account);


    AccountBalanceResponseDTO toBalanceResponse(Account account);

    default String mapCustomerName(Customer customer){
        if(customer == null) {
            return null;
        }
        return customer.getFirstName() + " " + customer.getLastName();
    }

}