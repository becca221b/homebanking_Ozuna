package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;




@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

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



        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @RequestMapping("/current")

    public Client getAll(Authentication authentication) {

        return clientRepository.findByEmail(authentication.getName());

    }

    public ClientDTO fromDTO(ClientDTO objDTO){
        return objDTO;
    }


}
