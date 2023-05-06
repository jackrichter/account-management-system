package com.backend.test.service;

import com.backend.test.entity.SavingsAccount;
import com.backend.test.entity.Transaction;

import java.util.List;

public interface AccountService {

    public List<SavingsAccount> getAllAccounts();
    public List<Transaction> getTenTransactions();
    public SavingsAccount getOneAccount(Long id);
    public SavingsAccount getBalance(Long id);
    public SavingsAccount withdrawAmount(Long id, Double amount);
    public SavingsAccount depositAmount(Long id, Double amount);
    public SavingsAccount createAccount(SavingsAccount newAccount);
    public Transaction createTransaction(Transaction newTransaction);
    public void updateAccount(SavingsAccount account);
}
