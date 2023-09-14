package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import static com.mindhub.homebanking.utils.CardUtils.getCardNumber;
import static com.mindhub.homebanking.utils.CardUtils.getCvv;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;




    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(@RequestParam CardType cardType, @RequestParam CardColor cardColor,
                                             Authentication authentication) {

        Client client= clientService.findByEmail(authentication.getName());

        if (client.getCards().size()==3) {

            return new ResponseEntity<>("Can't have more than 3 cards", HttpStatus.FORBIDDEN);

        }

        String number;
        do{
            number = getCardNumber();
        }while(cardService.existsByNumber(number));

        int cvv = getCvv();


        Card currentCard= new Card(client,cardType,cardColor,number,cvv);

        client.addCard(currentCard);
        cardService.saveCard(currentCard);


        return new ResponseEntity<>("201 creada",HttpStatus.CREATED);
    }




}
