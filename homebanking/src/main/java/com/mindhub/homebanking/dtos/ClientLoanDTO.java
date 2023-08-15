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

    private Client client;

    private Loan loan;



    public ClientLoanDTO(ClientLoan clientLoan){
        id= clientLoan.getId();
        loan_id= loan.getId();
        name= loan.getName();
        amount= clientLoan.getAmount();
        payments= clientLoan.getPayments();
    }


}
