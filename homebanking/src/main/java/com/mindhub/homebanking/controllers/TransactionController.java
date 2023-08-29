package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Transactional
    @RequestMapping("/transactions")//GET
    //servlet
    public List<TransactionDTO> getTransactions(Authentication authentication){
        List<Transaction> listTransaction = transactionRepository.findAll();

        List<TransactionDTO> listAccountDTO =
                listTransaction.stream()
                        .map(transaction-> new TransactionDTO(transaction))
                        .collect(Collectors.toList());
        return listAccountDTO;
    }


    @RequestMapping(path = "/clients/current/accounts/transactions", method = RequestMethod.POST)

    public ResponseEntity<Object> createTransaction(

            @RequestParam double amount, @RequestParam String description,

            @RequestParam String number_origin, @RequestParam String number_destiny,
            Authentication authentication) {



        if (amount!=0 || description.isEmpty() || number_origin.isEmpty() || number_destiny.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }



        if (number_destiny ==  number_destiny) {

            return new ResponseEntity<>("La cuenta de destino es igual a la de origen", HttpStatus.FORBIDDEN);

        }

        if (accountRepository.findByNumber(number_destiny)==null) {

            return new ResponseEntity<>("La cuenta de destino no existe", HttpStatus.FORBIDDEN);

        }

        if (accountRepository.findByNumber(number_origin).getBalance()>=amount) {

            return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);

        }
        String description_origin= description+number_destiny;
        String description_destiny= description+number_origin;

        Transaction debitTransaction= new Transaction(amount,description_origin, TransactionType.DEBIT);
        Transaction creditTransaction= new Transaction(amount,description_destiny, TransactionType.CREDIT);

        accountRepository.findByNumber(number_origin).addTransaction(debitTransaction);
        transactionRepository.save(debitTransaction);
        double saldoResta= accountRepository.findByNumber(number_origin).getBalance()-amount;
        accountRepository.findByNumber(number_origin).setBalance(saldoResta);
        accountRepository.save(accountRepository.findByNumber(number_origin));

        accountRepository.findByNumber(number_destiny).addTransaction(creditTransaction);
        transactionRepository.save(creditTransaction);
        double saldoSuma= accountRepository.findByNumber(number_destiny).getBalance()+amount;
        accountRepository.findByNumber(number_destiny).setBalance(saldoSuma);
        accountRepository.save(accountRepository.findByNumber(number_destiny));



        return new ResponseEntity<>(HttpStatus.CREATED);

    }


}
