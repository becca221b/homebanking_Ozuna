package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
			// save a couple of customers

			Client client1= new Client("Melba", "Morel","melba@mindhub.com");
			clientRepository.save(client1);

			Account account1= new Account();
			Account account2= new Account();

			account1.setDate(LocalDate.now());
			account1.setBalance(5000);
			account1.setNumber("VIN001");

			account2.setDate(LocalDate.now().plusDays(1));
			account2.setBalance(7500);
			account2.setNumber("VIN002");

			client1.addAccount(account1);
			client1.addAccount(account2);

			accountRepository.save(account1);
			accountRepository.save(account2);

			Transaction transaction1= new Transaction(2500,"laundry",TransactionType.DEBITO);
			account1.addTransaction(transaction1);
			transactionRepository.save(transaction1);

			Transaction transaction2= new Transaction(20000,"salary",TransactionType.CREDITO);
			account2.addTransaction(transaction2);
			transactionRepository.save(transaction2);

		};
	}


}
