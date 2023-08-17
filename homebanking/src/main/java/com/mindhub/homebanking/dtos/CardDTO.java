package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;


public class CardDTO {

    private long id;

    private String cardholder;

    private CardType type;

    private CardColor color;

    private long number;

    private LocalDate fromDate;

    private LocalDate thruDate;

    private int cvv;


    public CardDTO(Card card){
        id= card.getId();
        cardholder= card.getCardholder();
        type= card.getType();
        color= card.getColor();
        number= card.getNumber();
        fromDate= card.getFromDate();
        thruDate= card.getThruDate();
        cvv= card.getCvv();
    }

    public long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public long getNumber() {
        return number;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public int getCvv() {
        return cvv;
    }
}
