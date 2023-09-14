package com.mindhub.homebanking;


import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@SpringBootTest
public class CardUtilsTests {

    @Test

    public void cardNumberIsCreated(){

        String cardNumber = CardUtils.getCardNumber();

        assertThat(cardNumber,is(not(emptyOrNullString())));

    }

    @Test

    public void cardNumberLongEnough(){

        int cardNumberLength = CardUtils.getCardNumber().length();

        assertThat(cardNumberLength,is(19));

    }

    @Test

    public void cvvIsCreated(){

        int cvvNumber = CardUtils.getCvv();


        assertThat(cvvNumber, notNullValue(int.class));

    }

    @Test

    public void cvvIsLongEnough(){

        int cvvNumber = CardUtils.getCvv();

        assertThat(cvvNumber, both(greaterThan(100)).and(lessThan(999)));

    }

}
