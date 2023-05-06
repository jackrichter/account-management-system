package com.backend.test.service.impl;

import com.backend.test.entity.SavingsAccount;
import com.backend.test.entity.Transaction;
import com.backend.test.repository.SavingsAccountRepository;
import com.backend.test.repository.TransactionRepository;
import com.backend.test.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private SavingsAccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public AccountServiceImpl(SavingsAccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<SavingsAccount> getAllAccounts() {
        return this.accountRepository.findAll();
    }

    @Override
    public List<Transaction> getTenTransactions() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> page = this.transactionRepository.findAll(pageable);

        return  page.get().collect(Collectors.toList());
    }

    @Override
    public SavingsAccount getOneAccount(Long id) {
        Optional<SavingsAccount> optional = this.accountRepository.findById(id);

        if (!optional.isPresent()) {
            return null;
        }
        return optional.get();
    }

    @Override
    public SavingsAccount getBalance(Long id) {
        return this.getOneAccount(id);
    }

    @Override
    public SavingsAccount withdrawAmount(Long id, Double amount) {
        SavingsAccount existing = this.getOneAccount(id);
        existing.setBalance(existing.getBalance() - amount);
        SavingsAccount updated = this.accountRepository.save(existing);

        // Create a transaction for the withdrawal
        Transaction withdrawalTransaction = this.createLocalTransaction(-amount, existing);
        this.createTransaction(withdrawalTransaction);

        return updated;
    }

    @Override
    public SavingsAccount depositAmount(Long id, Double amount) {
        SavingsAccount existing = this.getOneAccount(id);
        existing.setBalance(existing.getBalance() + amount);
        SavingsAccount updated = this.accountRepository.save(existing);

        // Create a transaction for the deposit
        Transaction depositTransaction = this.createLocalTransaction(amount, existing);
        this.createTransaction(depositTransaction);

        return updated;
    }

    @Override
    public SavingsAccount createAccount(SavingsAccount newAccount) {
        SavingsAccount saved = this.accountRepository.save(newAccount);
        return saved;
    }

    @Override
    public Transaction createTransaction(Transaction newTransaction) {
        Transaction saved = this.transactionRepository.save(newTransaction);
        return saved;
    }

    public void updateAccount(SavingsAccount account) {
        this.accountRepository.save(account);
    }

    private Transaction createLocalTransaction(Double amount, SavingsAccount account) {
        Transaction transaction = new Transaction(amount, new Date(System.currentTimeMillis()));
        transaction.setAccount(account);
        return transaction;
    }
}
