package com.backend.test.service.impl;

import com.backend.test.entity.SavingsAccount;
import com.backend.test.entity.Transaction;
import com.backend.test.exception.AccountAlreadyExistsException;
import com.backend.test.exception.AccountNotFoundException;
import com.backend.test.exception.BalanceExceededException;
import com.backend.test.repository.SavingsAccountRepository;
import com.backend.test.repository.TransactionRepository;
import com.backend.test.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The service responsible for database communication and consistency logic.
 */
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
    public List<Transaction> getLastTenTransactions() {

        // Prepare a page containing ten rows of data ordered descending
        Pageable pageable = PageRequest.of(0, 10, Sort.by("transactionId").descending());
        Page<Transaction> page = this.transactionRepository.findAll(pageable);

        return  page.get().collect(Collectors.toList());
    }

    @Override
    public SavingsAccount getOneAccount(Long id) {
        Optional<SavingsAccount> optional = this.accountRepository.findById(id);

        // No need for exception here since null is later used in logic
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
    public SavingsAccount withdrawAmount(Long id, Double amount) throws BalanceExceededException {
        SavingsAccount existing = this.getOneAccount(id);

        // Check if withdrawal is larger the disposable amount
        if (amount > existing.getBalance()) {
            throw new BalanceExceededException(String.format("Balance amount exceeded: %s", existing.getBalance()));
        }

        // Make sure that mathematics logic does not result in more than two decimals
        existing.setBalance(Math.floor((existing.getBalance() - amount) * 100) / 100);
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
    public SavingsAccount createAccount(SavingsAccount newAccount) throws AccountAlreadyExistsException {

        // The search for an existing account is done by matching account numbers
        Optional<SavingsAccount> accountOptional = this.accountRepository.findByAccountNumber(newAccount.getAccountNumber());
        if (accountOptional.isPresent()) {
            throw new AccountAlreadyExistsException(String.format("An account with number %s exists already", newAccount.getAccountNumber()));
        }

        SavingsAccount saved = this.accountRepository.save(newAccount);
        return saved;
    }

    @Override
    public Transaction createTransaction(Transaction newTransaction) {
        Transaction saved = this.transactionRepository.save(newTransaction);
        return saved;
    }

    public SavingsAccount updateAccount(SavingsAccount account) {
        SavingsAccount updatedAccount = this.accountRepository.save(account);
        return updatedAccount;
    }

    @Override
    public void deleteAccount(Long id) {
        this.accountRepository.deleteById(id);
    }

    private Transaction createLocalTransaction(Double amount, SavingsAccount account) {
        // Helper method
        Transaction transaction = new Transaction(amount, new Date(System.currentTimeMillis()));
        transaction.setAccount(account);
        return transaction;
    }
}
