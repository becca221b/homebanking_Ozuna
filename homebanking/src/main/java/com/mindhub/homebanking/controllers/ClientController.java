package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;



import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


import static com.mindhub.homebanking.controllers.AccountController.getRandomNumber;


@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    //método que retorna el listado de clientes
   //servlet
    @RequestMapping("/clients") //GET
    public List<ClientDTO> getClients(){
        return clientService.getClientsDTO();
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClientDTO(id);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/clients", method = RequestMethod.POST)

    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {



        if (firstName.isEmpty()){
            return new ResponseEntity<>("Name is missing", HttpStatus.FORBIDDEN);
        }
        if (lastName.isEmpty()) {
            return new ResponseEntity<>("Lastname is missing", HttpStatus.FORBIDDEN);
        }
        if (email.isEmpty()){
            return new ResponseEntity<>("Mail is missing", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty()) {

            return new ResponseEntity<>("Password is missing", HttpStatus.FORBIDDEN);

        }



        if (clientService.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }


        Client client= new Client(firstName, lastName, email, passwordEncoder.encode(password));

        clientService.saveClient(client);

        String number= "VIN"+getRandomNumber(00000001,99999999);

        Account currentAccount = new Account(number, LocalDate.now(), client);

        accountService.saveAccount(currentAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @RequestMapping("clients/current")
    public ClientDTO getClient(Authentication authentication) {

        return new ClientDTO(clientService.findByEmail(authentication.getName()));

    }

}
