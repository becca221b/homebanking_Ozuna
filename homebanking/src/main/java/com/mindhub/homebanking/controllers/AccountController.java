package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Client client;

    @RequestMapping("/accounts")//GET
    //servlet
    public List<AccountDTO> getAccounts(){
        List<Account> listAccount = accountRepository.findAll();

        List<AccountDTO> listClientDTO =
                listAccount.stream()
                        .map(account-> new AccountDTO(account))
                                .collect(Collectors.toList());
        return listClientDTO;
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)

    public ResponseEntity<Object> createAccount(

            @RequestParam String number, @RequestParam LocalDate date,

            @RequestParam double balance) {



        if (client.getAccounts().size()==3) {

            return new ResponseEntity<>("403 prohibido", HttpStatus.FORBIDDEN);

        }


        accountRepository.save(new Account("VIN005",0.00));

        return new ResponseEntity<>("201 creada",HttpStatus.CREATED);

    }

}
