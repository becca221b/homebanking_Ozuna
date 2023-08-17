package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native",strategy = "native")
    private long id;

    private double amount;

    private double payments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loan;



    public ClientLoan(double amount, double payments){
        this.amount= amount;
        this.payments= payments;
    }



    public ClientLoan(){

    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPayments() {
        return payments;
    }

    public void setPayments(double payments) {
        this.payments = payments;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Loan getLoan() {
        return loan;
    }

    public Client getClient() {
        return client;
    }

}
