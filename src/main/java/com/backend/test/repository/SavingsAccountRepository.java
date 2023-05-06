package com.backend.test.repository;

import com.backend.test.entity.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    public Double findBalanceById(Long id);
}
