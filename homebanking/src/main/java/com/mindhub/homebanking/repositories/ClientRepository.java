package com.mindhub.homebanking.repositories;


import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RestController;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {
}
/*
@RestController
public class AppController{
    @Autowired
    private ClientRepository clientRepository;
}*/
