package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;

public class LoanDTO {

    @Autowired
    LoanRepository loanRepository;

    @RequestMapping(value="/loans", method= RequestMethod.GET)

    public List<LoanDTO> getLoans(){
            List<Loan> loanList= loanRepository.findAll();

            List<LoanDTO> loanDTOList=
                loanList.stream().map(loan-> new LoanDTO()).collect(Collectors.toList());
            return loanDTOList;
    }


}
