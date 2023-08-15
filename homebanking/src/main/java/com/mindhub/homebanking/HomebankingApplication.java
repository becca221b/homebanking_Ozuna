package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
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

			Loan loan1= new Loan("Hipotecario",500000, List.of(12,24,24,36,48,60));
			Loan loan2= new Loan("Personal",100000,List.of(6,12,24));
			Loan loan3= new Loan("Automotriz",300000,List.of(6,12,24,36));

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

            ClientLoan clientLoan1= new ClientLoan(400000,60);
            ClientLoan clientLoan2= new ClientLoan( 50000, 12);

			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			loan1.addClientLoan(clientLoan1);
			loan2.addClientLoan(clientLoan2);


            clientLoanRepository.save(clientLoan1);
            clientLoanRepository.save(clientLoan2);

			clientRepository.save(client1);




		};
	}


}
