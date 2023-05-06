package com.backend.test.controller;

import com.backend.test.entity.SavingsAccount;
import com.backend.test.entity.Transaction;
import com.backend.test.exception.AccountNotFoundException;
import com.backend.test.service.AccountService;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccounSystemtController {

    private AccountService accountService;

    public AccounSystemtController(AccountService service) {
        this.accountService = service;
    }

    // Ex: http://localhost:8080/api/accounts (GET)
    @GetMapping("/accounts")
    public ResponseEntity<List<SavingsAccount>> getAllAccounts() {
        List<SavingsAccount> accounts = this.accountService.getAllAccounts();

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    // Ex: http://localhost:8080/api/accounts/1 (GET)
    @GetMapping("/accounts/{id}")
    public ResponseEntity<SavingsAccount> getAccount(@PathVariable("id") Long id) throws AccountNotFoundException {
        SavingsAccount account = this.accountService.getOneAccount(id);

        if (account == null) {
            throw new AccountNotFoundException("id: " + id);
        }

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    // Ex: http://localhost:8080/api/transactions  -> Returns 10 transactions  (GET)
    @GetMapping("/transactions")
    public List<Transaction> getTransactionsPage() {
        return this.accountService.getTenTransactions();
    }

    // Ex: http://localhost:8080/api/accounts/balance/1 (GET)
    @GetMapping("/accounts/balance/{id}")
    public ResponseEntity<SavingsAccount> getBalanceById(@PathVariable("id") Long id) throws AccountNotFoundException {
        SavingsAccount account = this.accountService.getBalance(id);

        if (account == null) {
            throw new AccountNotFoundException("id: " + id);
        }
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    // Ex: http://localhost:8080/api/accounts (POST)
    @PostMapping("/accounts")
    public ResponseEntity<SavingsAccount> createSavingsAccount(@Valid @RequestBody SavingsAccount account) {
        SavingsAccount saved = this.accountService.createAccount(account);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    // Ex: http://localhost:8080/api/transactions/2  (POST for account with id = 2)
    @PostMapping("/transactions/{id}")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction, @PathVariable("id") Long id) throws AccountNotFoundException {
        SavingsAccount account = this.accountService.getOneAccount(id);

        if (account == null) {
            throw new AccountNotFoundException("id: " + id);
        }
        transaction.setAccount(account);

        Transaction saved = this.accountService.createTransaction(transaction);

        // Update Savings Account
        account.addTransaction(transaction);
        this.accountService.updateAccount(account);

        // Build a URI for this operation
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(saved.getTransactionId())
            .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    // Ex: http://localhost:8080/api/accounts/2/withdraw/100 (PUT)
    @PutMapping("accounts/{id}/withdraw/{amount}")
    public SavingsAccount withdraw(@PathVariable("id") Long id, @PathVariable("amount") Double amount) {
        SavingsAccount updated = this.accountService.withdrawAmount(id, amount);
        return  updated;
    }

    // Ex: http://localhost:8080/api/accounts/2/deposit/150 (PUT)
    @PutMapping("accounts/{id}/deposit/{amount}")
    public SavingsAccount deposit(@PathVariable("id") Long id, @PathVariable("amount") Double amount) {
        SavingsAccount updated = this.accountService.depositAmount(id, amount);
        return  updated;
    }
}
