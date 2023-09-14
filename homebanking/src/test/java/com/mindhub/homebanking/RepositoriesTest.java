package com.mindhub.homebanking;



import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;


    @Test
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans,hasSize(greaterThan(0)));

    }

    @Test
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }

    @Test
    public void existAccounts() {
        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts,hasSize(greaterThan(0)));
    }

    @Test
    void numberAccountStart() {
        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts, hasItem(hasProperty("number", startsWith("VIN"))));
    }

    @Test
    public void existCards(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, is(not(empty())));
    }

    @Test
    void existsClients() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }

    @Test
    void existsTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasSize(greaterThan(0)));
    }





}
