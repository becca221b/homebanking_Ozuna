package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
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
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientService clientService;


    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)

    public ResponseEntity<Object> createTransaction(

            @RequestParam double amount, @RequestParam String description,

            @RequestParam String accountFromNumber, @RequestParam String accountToNumber,
            Authentication authentication) {

        accountToNumber= accountToNumber.replace(" ","");
        accountFromNumber= accountFromNumber.replace(" ","");

        if(!clientService.existsByEmail(authentication.getName())){
            return new ResponseEntity<>("Debe iniciar sesión", HttpStatus.FORBIDDEN);
        }

        Client client= clientService.findByEmail(authentication.getName());
        Account accountDestiny= accountService.findByNumber(accountToNumber);
        Account accountOrigin= accountService.findByNumber(accountFromNumber);


        if (amount<=0) {
            return new ResponseEntity<>("Ammount must be more than 0", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank()){
            return new ResponseEntity<>("Description is missing", HttpStatus.FORBIDDEN);
        }
        if (accountFromNumber.isBlank()){
            return new ResponseEntity<>("Source account number is missing", HttpStatus.FORBIDDEN);
        }
        if (accountToNumber.isEmpty()){
            return new ResponseEntity<>("Target account number is missing", HttpStatus.FORBIDDEN);
        }



        if (accountToNumber.equals(accountFromNumber)) {

            return new ResponseEntity<>("Source and target accounts are the same", HttpStatus.FORBIDDEN);

        }

        if (accountDestiny==null) {

            return new ResponseEntity<>("Target account doesnt exist", HttpStatus.FORBIDDEN);

        }

        if (accountOrigin.getBalance()<=amount) {

            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);

        }

        if (!client.getAccounts().contains(accountOrigin)){
            return new ResponseEntity<>("Source account doesnt belong to you", HttpStatus.FORBIDDEN);
        }


        String description_origin= description+accountToNumber;
        String description_destiny= description+accountFromNumber;

        Transaction debitTransaction= new Transaction(amount,description_origin, TransactionType.DEBIT);
        Transaction creditTransaction= new Transaction(amount,description_destiny, TransactionType.CREDIT);


        transactionService.saveTransaction(debitTransaction);
        double saldoResta= accountOrigin.getBalance()-amount;
        accountOrigin.setBalance(saldoResta);
        accountService.saveAccount(accountOrigin);
        accountOrigin.addTransaction(debitTransaction);


        transactionService.saveTransaction(creditTransaction);
        double saldoSuma= accountDestiny.getBalance()+amount;
        accountDestiny.setBalance(saldoSuma);
        accountService.saveAccount(accountDestiny);
        accountDestiny.addTransaction(creditTransaction);


        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
