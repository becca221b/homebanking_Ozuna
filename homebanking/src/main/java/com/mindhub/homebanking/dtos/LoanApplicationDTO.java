package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long loanId;

    private double amount;

    private int payments;

    private String accountToNumber;

    LoanApplicationDTO(){}

    public LoanApplicationDTO(long loanId, double amount, int payments, String accountToNumber) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.accountToNumber = accountToNumber;
    }

    public long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getAccountToNumber() {
        return accountToNumber;
    }
}
