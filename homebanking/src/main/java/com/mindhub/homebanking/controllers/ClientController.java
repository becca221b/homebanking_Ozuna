package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.controllers.AccountController.getRandomNumber;


@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    //método que retorna el listado de clientes
   //servlet
    @RequestMapping("/clients") //GET
    public List<ClientDTO> getClients(){

        List<Client> listPerson= clientRepository.findAll();

        List<ClientDTO> listClientDTO =
                listPerson
                        .stream()
                        .map(client -> new ClientDTO(client))
                        .collect(Collectors.toList());

        return listClientDTO;
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/clients", method = RequestMethod.POST)

    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {



        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }



        if (clientRepository.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }


        Client client= new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(client);

        String number= "VIN"+getRandomNumber(00000001,99999999);

        Account currentAccount = new Account(number, LocalDate.now(), client);

        accountRepository.save(currentAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @RequestMapping("clients/current")
    public ClientDTO getClient(Authentication authentication) {

        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));

    }

}
