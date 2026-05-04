package com.cm6123.monopoly;

import com.cm6123.monopoly.game.Player;
import com.cm6123.monopoly.game.Property;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyChecks {

    @Test
    public void WhenAPropertyIsGivenItsValueAndRentalPriceItShouldReturnThoseValues() {
        Property property = new Property(1,300.00F,30.00F,"May Fair");

        assertEquals(300.00F,property.getValueOfProperty());
        assertEquals(30.00F,property.getRentalPrice());
    }

    @Test
    public void WhenAPlayerHasBoughtThePropertyTheirPlayerObjectShouldBeAccesible() {
        Property property = new Property(1,300.00F,30.00F,"May Fair");
        Player player = new Player(1);
        property.setOwner(player);

        assertEquals(player,property.returnOwner());
    }

    @Test
    public void WhenAPlayerDecidesToBuyThePropertyTheirBalanceIsReducedByThePropertyValueAndTheyAreMadeOwner() {
        Property property = new Property(1,300.00F,30.00F,"May Fair");
        Player player = new Player(1);
        float initialBalance = player.getPlayerBalance();
        property.playerBuysProperty(player);
        assertEquals((initialBalance-property.getValueOfProperty()),player.getPlayerBalance());
    }

    @Test
    public void WhenAPlayerDecidesToBuyThePropertyTheyNeedToHaveEnoughMoneyForIt() {
        Property property1 = new Property(1,300.00F,30.00F,"May Fair");
        Property property2 = new Property(2,300.00F,30.00F,"May Fair");
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        player1.subBalance(800.00F);
        property2.playerBuysProperty(player1);
        property2.playerBuysProperty(player2);

        assertNull(property1.returnOwner());
        assertEquals(property2.returnOwner(),player2);
    }

    @Test // REPLACE THIS FOR A TEST FOR A TEST FOR RENTAL INSTEAD.
    public void IfAPlayerHasAlreadyBoughtThePropertyAnotherPlayerShouldNotBeAbleToBuyThatProperty() {
        Property property = new Property(1,300.00F,30.00F,"May Fair");
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        property.playerBuysProperty(player1);
        property.playerBuysProperty(player2);

        assertEquals(player1,property.returnOwner());
    }

    @Test
    public void IfThePlayerCannotPayTheRentUsingJustMoneyAndTheyOwnPropertiesTheyShouldBeAbleToSellThePropertyToPayRent() {
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        //The Property Player One Owns.
        Property property = new Property(2,300.00F,30.00F,"May Fair");
        property.playerBuysProperty(player1);

        Property property2 = new Property(3,300.00F,30.00F,"Fair May");
        Property property3 = new Property(4,300.00F,30.00F,"Fair June");

        property2.playerBuysProperty(player2);
        property3.playerBuysProperty(player2);


        player2.subBalance(380.00F);
        boolean paymentSuccessful = property.carryOutRental(player2);

        assertEquals(140.00F,player2.getPlayerBalance());
        assertTrue(paymentSuccessful);
        assertEquals(730.00F,player1.getPlayerBalance());
        assertEquals(1,player2.getOwnedProperties().size());
    }

    @Test
    public void IfAPlayerOwnsNoPropertiesAndHasLessMoneyThanTheRentalRequirementThenTheirWholeBalanceIsPaidToTheOwner() {
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Property property = new Property(2,300.00F,30.00F,"May Fair");
        property.playerBuysProperty(player1);

        player2.subBalance(980.00F);
        property.carryOutRental(player2);

        assertEquals(0.00F,player2.getPlayerBalance());
        assertEquals(720.00F,player1.getPlayerBalance());
    }

    @Test
    public void WhenAPropertyIsSoldForRentalPaymentThatPropertyShouldBeAbleToBeBoughtAgain() {
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Property property = new Property(2,300.00F,30.00F,"May Fair");
        property.playerBuysProperty(player1);

        Property property2 = new Property(3,300.00F,30.00F,"Fair May");
        property2.playerBuysProperty(player2);
        player2.subBalance(680.00F);

        property.carryOutRental(player2);

        property2.playerBuysProperty(player1);

        assertEquals(430.00F,player1.getPlayerBalance());
        assertEquals(player1,property2.returnOwner());
    }
}
