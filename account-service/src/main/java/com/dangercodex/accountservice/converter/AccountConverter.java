package com.dangercodex.accountservice.converter;

import com.dangercodex.accountservice.dto.CreateAccountDto;
import com.dangercodex.accountservice.model.Account;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountConverter {
    public static Account fromDto(CreateAccountDto dto) {
        Account account = new Account();
        account.setUsername(dto.getUsername());
        account.setMail(dto.getMail());
        account.setPassword(dto.getPassword());
        return account;
    }
}