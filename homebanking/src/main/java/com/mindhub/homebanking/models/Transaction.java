package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime date;
    private double amount;
    private String description;

    private TransactionType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "account_id")
    private Account account;

    public Transaction(){
        date= LocalDateTime.now();
    }
    public Transaction(double amount, String description, TransactionType type){
        this.date= LocalDateTime.now();
        if (type==TransactionType.DEBIT) {
            this.amount= amount*(-1);
        }else{
            this.amount= amount;
        }
        this.description= description;
        this.type= type;
    }

    public Transaction(double amount, String description, TransactionType type, Account account){
        this.date= LocalDateTime.now();
        if (type==TransactionType.DEBIT) {
            this.amount= amount*(-1);
        }else{
            this.amount= amount;
        }
        this.description= description;
        this.type= type;
        this.account= account;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account){
        this.account= account;
    }

    public long getId() {
        return id;
    }

    public TransactionType getType(){
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
