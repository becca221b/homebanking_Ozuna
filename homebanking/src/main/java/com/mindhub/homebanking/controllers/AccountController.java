package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    private Client client;

    @Autowired
    private ClientRepository clientRepository;


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

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.GET)
    public List<AccountDTO> getClientAccounts (Authentication authentication)
    {
        Client client = this.clientRepository.findByEmail(authentication.getName());

        return client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    public static int getRandomNumber(int min, int max){
        return (int)((Math.random()*(max-min))+min);
    }
    int min=00000001;
    int max=99999999;

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {

       Client client= clientRepository.findByEmail(authentication.getName());



        if (client.getAccounts().size()==3) {

            return new ResponseEntity<>("403 prohibido", HttpStatus.FORBIDDEN);

        }

        String number= "VIN"+getRandomNumber(min,max);

        Account currentAccount = new Account(number, LocalDate.now(), client);

        accountRepository.save(currentAccount);
        clientRepository.save(client);
        return new ResponseEntity<>("201 creada",HttpStatus.CREATED);
    }

}
