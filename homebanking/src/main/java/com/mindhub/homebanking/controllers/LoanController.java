package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientLoanService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;


@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientLoanService clientLoanService;

    @RequestMapping(value="/loans", method= RequestMethod.GET)
    public List<LoanDTO> getLoans(){
        return loanService.getLoans();
    }

    @Transactional
    @RequestMapping(path = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> askLoan(@RequestBody LoanApplicationDTO loanApplicationDTO,
                                          Authentication authentication) {

        double amount= loanApplicationDTO.getAmount();
        int payments= loanApplicationDTO.getPayments();
        String accountToNumber= loanApplicationDTO.getAccountToNumber();
        Loan loan= loanService.findById(loanApplicationDTO.getLoanId());
        Account account= accountService.findByNumber(accountToNumber);

        if(!clientService.existsByEmail(authentication.getName())){
            return new ResponseEntity<>("Debe iniciar sesión", HttpStatus.FORBIDDEN);
        }

        Client client= clientService.findByEmail(authentication.getName());

        if (amount<=0 || payments<=0) {

            return new ResponseEntity<>("El monto y cuotas debe ser mayor a cero", HttpStatus.FORBIDDEN);

        }

        if (String.valueOf(amount).isBlank() || String.valueOf(payments).isBlank() || accountToNumber.isEmpty()) {

            return new ResponseEntity<>("Debes completar todos los campos", HttpStatus.FORBIDDEN);

        }

        if(loan==null){
            return new ResponseEntity<>("Préstamo no existe", HttpStatus.FORBIDDEN);
        }

        if(amount>loan.getMaxAmount()){
            return new ResponseEntity<>("El monto solicitado supera el monto máximo", HttpStatus.FORBIDDEN);
        }

        if (account==null) {
            return new ResponseEntity<>("La cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("La cuenta de destino no pertenece al cliente", HttpStatus.FORBIDDEN);
        }

        double saldoSuma= account.getBalance()+amount;
        account.setBalance(saldoSuma);

        ClientLoan clientLoan= new ClientLoan(amount,payments,client,loan);
        clientLoanService.saveClientLoan(clientLoan);

        Transaction loanTransaction= new Transaction(amount,loan.getName()+" loan approved", TransactionType.CREDIT, accountService.findByNumber(accountToNumber));
        transactionRepository.save(loanTransaction);

        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
