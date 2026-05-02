package com.cm6123.monopoly;

import com.cm6123.monopoly.game.Player;
import com.cm6123.monopoly.game.Property;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
}
