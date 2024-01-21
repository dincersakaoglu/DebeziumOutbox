package com.dangercodex.accountservice.controller;

import com.dangercodex.accountservice.dto.CreateAccountDto;
import com.dangercodex.accountservice.model.Account;
import com.dangercodex.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public Account createAccount(@RequestBody CreateAccountDto account) {
        return accountService.createAccount(account);
    }

    @GetMapping
    public List<Account> getAccounts() {
        return accountService.findAll();
    }
}
