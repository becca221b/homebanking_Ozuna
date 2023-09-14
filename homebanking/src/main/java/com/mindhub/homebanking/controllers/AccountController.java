package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
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
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    //servlet
    public List<AccountDTO> getAccounts(){
        return accountService.getAccounts();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountService.findById(id));
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getClientAccounts (Authentication authentication)
    {
        Client client = this.clientService.findByEmail(authentication.getName());

        return client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    public static int getRandomNumber(int min, int max){
        return (int)((Math.random()*(max-min))+min);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {

       Client client= clientService.findByEmail(authentication.getName());

        if (client.getAccounts().size()==3) {

            return new ResponseEntity<>("Can't have more than 3 accounts", HttpStatus.FORBIDDEN);

        }

        String number;

       do{
            number= "VIN"+getRandomNumber(00000001,99999999);
        } while(accountService.existsByNumber(number));

        Account currentAccount = new Account(number, LocalDate.now(), client);

        accountService.saveAccount(currentAccount);
        clientService.saveClient(client);
        return new ResponseEntity<>("201 creada",HttpStatus.CREATED);
    }

}
