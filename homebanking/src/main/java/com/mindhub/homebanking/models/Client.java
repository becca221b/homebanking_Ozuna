package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Entity
public class  Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;

    private String email;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    List<ClientLoan> clientLoans = new ArrayList<>();

    public Client() { }

    public Client(String first, String last, String mail) {
        this.firstName = first;
        this.lastName = last;
        this.email = mail;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return firstName + " " + lastName;
    }

    public Set<Account> getAccounts(){
        return accounts;
    }
    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }

    public List<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(List<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public List<Loan> getLoans(){
        return clientLoans.stream()
                .map(elements->elements.getLoan())
                .collect(toList());
    }
}

