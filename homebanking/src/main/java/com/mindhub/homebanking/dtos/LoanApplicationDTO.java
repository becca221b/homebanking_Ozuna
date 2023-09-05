package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class LoanApplicationDTO {
    long id;

    double amount;

    int payments;

    String accountToNumber;

    @RequestMapping(value="/api/loans", method= RequestMethod.POST)

    public ResponseEntity<String> addLoan(@RequestBody Loan loan,
                                          Authentication authentication) {

        loanRepository.save(loan);

    }

}
