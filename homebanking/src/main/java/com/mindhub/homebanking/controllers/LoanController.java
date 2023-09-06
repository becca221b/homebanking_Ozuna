package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @RequestMapping(value="/loans", method= RequestMethod.GET)

    public List<LoanDTO> getLoans(){
        List<Loan> loanList= loanRepository.findAll();

        List<LoanDTO> loanDTOList=
                loanList.stream().map(LoanDTO::new).collect(Collectors.toList());
        return loanDTOList;
    }

    @Transactional
    @RequestMapping(path = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> askLoan(@RequestBody LoanApplicationDTO loanApplicationDTO,
                                          Authentication authentication) {

        double amount= loanApplicationDTO.getAmount();
        int payments= loanApplicationDTO.getPayments();
        String accountToNumber= loanApplicationDTO.getAccountToNumber();
        Loan loan= loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);

        if(!clientRepository.existsByEmail(authentication.getName())){
            return new ResponseEntity<>("Debe iniciar sesión", HttpStatus.FORBIDDEN);
        }

        Client client= clientRepository.findByEmail(authentication.getName());

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

        if (accountRepository.findByNumber(accountToNumber)==null) {
            System.out.println(accountToNumber);
            System.out.println(accountRepository.findByNumber(accountToNumber));
            return new ResponseEntity<>("La cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(accountRepository.findByNumber(accountToNumber))){
            return new ResponseEntity<>("La cuenta de destino no pertenece al cliente", HttpStatus.FORBIDDEN);
        }

        Transaction loanTransaction= new Transaction(amount,loan.getName()+" loan approved", TransactionType.CREDIT, accountRepository.findByNumber(accountToNumber));
        transactionRepository.save(loanTransaction);
        double saldoSuma= accountRepository.findByNumber(accountToNumber).getBalance()+amount;
        accountRepository.findByNumber(accountToNumber).setBalance(saldoSuma);
        accountRepository.save(accountRepository.findByNumber(accountToNumber));

        accountRepository.findByNumber(accountToNumber).addTransaction(loanTransaction);

        ClientLoan clientLoan= new ClientLoan(amount,payments,client,loan);

        clientLoanRepository.save(clientLoan);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
