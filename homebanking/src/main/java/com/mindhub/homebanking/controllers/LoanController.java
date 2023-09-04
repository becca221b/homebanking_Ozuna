package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientRepository clientRepository;

    public ResponseEntity<Object> askLoan(

            @RequestParam double amount, @RequestParam String name,

            @RequestParam int payments, @RequestParam String accountToNumber,
            Authentication authentication) {

        Loan loan= loanRepository.findByName(name);

        Client client= clientRepository.findByEmail(authentication.getName()


        if (amount<=0 || payments<=0) {

            return new ResponseEntity<>("El monto y cuotas debe ser mayor a cero", HttpStatus.FORBIDDEN);

        }

        if (String.valueOf(amount).isBlank() || String.valueOf(payments).isBlank() || accountToNumber.isEmpty()) {

            return new ResponseEntity<>("Debes completar todos los campos", HttpStatus.FORBIDDEN);

        }

     //VERIFICAR QUE EL PRESTAMO EXISTA
        if(loan==null){
            return new ResponseEntity<>("Préstamo no existe", HttpStatus.FORBIDDEN);
        }

        if(amount>loan.getMaxAmount()){
            return new ResponseEntity<>("El monto solicitado supera el monto máximo", HttpStatus.FORBIDDEN);
        }

        if (accountRepository.findByNumber(accountToNumber)==null) {

            return new ResponseEntity<>("La cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(accountRepository.findByNumber(accountToNumber))){
            return new ResponseEntity<>("La cuenta de destino no pertenece al cliente", HttpStatus.FORBIDDEN);
        }

        Transaction loanTransaction= new Transaction(amount,name+" loan approved", TransactionType.CREDIT);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
