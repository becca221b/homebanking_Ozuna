package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import java.util.List;

public interface ClientService {

    public void saveClient(Client client);

    public List<ClientDTO> getClientsDTO();

    public ClientDTO getClientDTO(Long id);

    public Client findById(Long id);

    public boolean existsByEmail(String email);

    public Client findByEmail(String email);
}
