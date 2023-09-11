package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;


import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccounts();

    Account findById(Long id);

    boolean existsByNumber(String number);

    void saveAccount(Account account);

    Account findByNumber(String number);


}
