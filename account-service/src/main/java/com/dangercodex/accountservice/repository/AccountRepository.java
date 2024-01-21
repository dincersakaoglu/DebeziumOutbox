package com.dangercodex.accountservice.repository;

import com.dangercodex.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}