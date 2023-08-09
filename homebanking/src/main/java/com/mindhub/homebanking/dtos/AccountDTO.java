package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;

public class AccountDTO {
    private long id;
    String number;
    LocalDate date;
    double balance;

    public AccountDTO(Account account){
        id= account.getId();
        number= account.getNumber();
        date = account.getDate();
        balance= account.getBalance();
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
}
