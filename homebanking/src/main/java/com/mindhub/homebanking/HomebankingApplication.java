package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {


	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers

			Client client1= new Client("Melba", "Morel","melba@mindhub.com", passwordEncoder.encode("pass123"));
			Client client2= new Client("Lionel","Messi","lio.10@yahoo.com.ar", passwordEncoder.encode("qatar2022"));


			clientRepository.save(client1);
            clientRepository.save(client2);

			Account account1= new Account();
			Account account2= new Account();
            Account account3= new Account("VIN003",10000);

			account1.setDate(LocalDate.now());
			account1.setBalance(5000);
			account1.setNumber("VIN001");

			account2.setDate(LocalDate.now().plusDays(1));
			account2.setBalance(7500);
			account2.setNumber("VIN002");

			client1.addAccount(account1);
			client1.addAccount(account2);
            client2.addAccount(account3);


			accountRepository.save(account1);
			accountRepository.save(account2);
            accountRepository.save(account3);

			Transaction transaction1= new Transaction(2500,"laundry",TransactionType.DEBIT);
			account1.addTransaction(transaction1);
			transactionRepository.save(transaction1);

			Transaction transaction2= new Transaction(20000,"salary",TransactionType.CREDIT);
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
            ClientLoan clientLoan3= new ClientLoan(100000,24);
            ClientLoan clientLoan4= new ClientLoan(200000,36);

			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
            client2.addClientLoan(clientLoan3);
            client2.addClientLoan(clientLoan4);

			loan1.addClientLoan(clientLoan1);
			loan2.addClientLoan(clientLoan2);
            loan2.addClientLoan(clientLoan3);
            loan3.addClientLoan(clientLoan4);


            clientLoanRepository.save(clientLoan1);
            clientLoanRepository.save(clientLoan2);
            clientLoanRepository.save(clientLoan3);
            clientLoanRepository.save(clientLoan4);

			Card card1= new Card(client1,CardType.DEBIT,CardColor.GOLD,"1234-5678-9112-3456",123);
			Card card2= new Card(client1,CardType.CREDIT,CardColor.TITANIUM,"1234-5678-9012-3456",789);
			Card card3= new Card(client2,CardType.CREDIT,CardColor.SILVER,"1234-5678-9000-0000",456);

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);


		};
	}


}
