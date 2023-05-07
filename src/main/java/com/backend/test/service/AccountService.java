package com.backend.test.service;

import com.backend.test.entity.SavingsAccount;
import com.backend.test.entity.Transaction;
import com.backend.test.exception.AccountAlreadyExistsException;
import com.backend.test.exception.BalanceExceededException;

import java.util.List;

public interface AccountService {

    public List<SavingsAccount> getAllAccounts();
    public List<Transaction> getLastTenTransactions();
    public SavingsAccount getOneAccount(Long id);
    public SavingsAccount getBalance(Long id);
    public SavingsAccount withdrawAmount(Long id, Double amount) throws BalanceExceededException;
    public SavingsAccount depositAmount(Long id, Double amount);
    public SavingsAccount createAccount(SavingsAccount newAccount) throws AccountAlreadyExistsException;
    public Transaction createTransaction(Transaction newTransaction);
    public SavingsAccount updateAccount(SavingsAccount account);
    public void deleteAccount(Long id);
}
