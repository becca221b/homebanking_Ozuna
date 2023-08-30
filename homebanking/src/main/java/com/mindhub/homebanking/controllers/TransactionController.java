package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;


@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    ClientRepository clientRepository;


    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)

    public ResponseEntity<Object> createTransaction(

            @RequestParam double amount, @RequestParam String description,

            @RequestParam String accountFromNumber, @RequestParam String accountToNumber,
            Authentication authentication) {

        Client client= clientRepository.findByEmail(authentication.getName());


        if (amount==0 || description.isBlank() || accountFromNumber.isBlank() || accountToNumber.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }



        if (accountToNumber ==  accountFromNumber) {

            return new ResponseEntity<>("La cuenta de destino es igual a la de origen", HttpStatus.FORBIDDEN);

        }

        if (accountRepository.findByNumber(accountToNumber)==null) {

            return new ResponseEntity<>("La cuenta de destino no existe", HttpStatus.FORBIDDEN);

        }

        if (accountRepository.findByNumber(accountFromNumber).getBalance()<=amount) {

            return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);

        }
        String description_origin= description+accountToNumber;
        String description_destiny= description+accountFromNumber;

        Transaction debitTransaction= new Transaction(amount,description_origin, TransactionType.DEBIT);
        Transaction creditTransaction= new Transaction(amount,description_destiny, TransactionType.CREDIT);


        transactionRepository.save(debitTransaction);
        double saldoResta= accountRepository.findByNumber(accountFromNumber).getBalance()-amount;
        accountRepository.findByNumber(accountFromNumber).setBalance(saldoResta);
        accountRepository.save(accountRepository.findByNumber(accountFromNumber));
        accountRepository.findByNumber(accountFromNumber).addTransaction(debitTransaction);


        transactionRepository.save(creditTransaction);
        double saldoSuma= accountRepository.findByNumber(accountToNumber).getBalance()+amount;
        accountRepository.findByNumber(accountToNumber).setBalance(saldoSuma);
        accountRepository.save(accountRepository.findByNumber(accountToNumber));
        accountRepository.findByNumber(accountToNumber).addTransaction(creditTransaction);


        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
