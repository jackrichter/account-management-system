package com.backend.test.repository;

import com.backend.test.entity.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    public Double findBalanceById(Long id);
    public Optional<SavingsAccount> findByAccountNumber(Long accountNr);
}
