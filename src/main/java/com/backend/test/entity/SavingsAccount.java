package com.backend.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "savings_accounts")
@Entity
public class SavingsAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_number")
    @Min(value = 0, message = "Account number cannot be a negative number")
    private Long accountNumber;

    @Column(name = "first_name")
    @Size(min = 2, message = "Name should have at least 2 characters")
    private String firstName;

    @Column(name = "last_name")
    @Size(min = 2, message = "Surname should have at least 2 characters")
    private String lastName;

    @Column(name = "balance")
    @Min(value = 0L, message = "Negative balance is not allowed")
    private Double balance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Transaction> transactions;

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        this.setBalance(this.getBalance() + transaction.getAmount());
    }
}
