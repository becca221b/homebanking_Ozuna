package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {
    private long id;
    private LocalDateTime date;
    private double amount;
    private String description;
    private TransactionType type;

    public TransactionDTO(Transaction transaction){
        id= transaction.getId();
        date= transaction.getDate();
        amount= transaction.getAmount();
        description= transaction.getDescription();
        type= transaction.getType();
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public TransactionType getType() {
        return type;
    }
}
