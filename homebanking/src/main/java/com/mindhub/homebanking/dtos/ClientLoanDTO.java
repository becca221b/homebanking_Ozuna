package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientLoanDTO {

    private long id;

    private long loan_id;

    private String name;

    double amount;

    double payments;





    public ClientLoanDTO(ClientLoan clientLoan){
        id= clientLoan.getId();
        loan_id= clientLoan.getLoan().getId();
        name= clientLoan.getLoan().getName();
        amount= clientLoan.getAmount();
        payments= clientLoan.getPayments();

    }

    public long getId() {
        return id;
    }

    public long getLoan_id() {
        return loan_id;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public double getPayments() {
        return payments;
    }


}
