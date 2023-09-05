package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    String number;
    LocalDate date;
    double balance;

    private Set<TransactionDTO> transactions;

    public AccountDTO(Account account){
        id= account.getId();
        number= account.getNumber();
        date = account.getDate();
        balance= account.getBalance();
        transactions= account.getTransactions()
                .stream()
                .map(element-> new TransactionDTO(element))
                .collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactions(){return transactions;}
}
