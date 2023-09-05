package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class LoanDTO {
    @RequestMapping(value="/loans", method= RequestMethod.GET)

    public ResponseEntity<String> addLoan(@RequestBody Loan loan) {

        loanRepository.save(loan);

    }
}
