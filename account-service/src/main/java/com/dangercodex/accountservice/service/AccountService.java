package com.dangercodex.accountservice.service;

import com.dangercodex.accountservice.converter.AccountConverter;
import com.dangercodex.accountservice.converter.OutboxConverter;
import com.dangercodex.accountservice.dto.CreateAccountDto;
import com.dangercodex.accountservice.model.Account;
import com.dangercodex.accountservice.model.enums.MailStatus;
import com.dangercodex.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final OutboxService outboxService;

    public Account createAccount(CreateAccountDto dto) {
        Account newAccount = AccountConverter.fromDto(dto);
        newAccount.setMailStatus(MailStatus.CREATED);
        Account savedAccount = accountRepository.save(newAccount);
        log.info("Account created: {}", savedAccount);

        outboxService.createOutbox(OutboxConverter.convertToOutbox(savedAccount));
        log.info("Outbox created");

        return savedAccount;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}

