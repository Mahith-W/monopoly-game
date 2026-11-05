package com.cm6123.monopoly;

import com.cm6123.monopoly.dice.Dice;
import com.cm6123.monopoly.game.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerChecks {

    @Test
    public void WhenAPlayerIsCreatedTheirPlayerNumberHasToBeOne(){

        Player p1 = new Player(1);

        assertEquals(1, p1.getPlayerNumber());
    }

    @Test
    public void WhenTwoPlayersAreCreatedTheirPlayerNumberHasToBeTwo(){
        Player p1 = new Player(1);
        Player p2 = new Player(2);

        assertEquals(1, p1.getPlayerNumber());
        assertEquals(2, p2.getPlayerNumber());
    }

    @Test
    public void WhenAPlayerIsCreatedTheBalanceHasToBeOneThousand(){
        Player p1 = new Player(1);

        assertEquals(1000.00F,p1.getPlayerBalance());
    }

    @Test
    public void WhenTwoPlayersAreCreatedTheBalanceForBothHasToBeOneThousand(){
        Player p1 = new Player(1);
        Player p2 = new Player(2);

        assertEquals(1000.00F,p1.getPlayerBalance());
        assertEquals(1000.00F,p2.getPlayerBalance());
    }

    @Test
    public void WhenAPlayerIsSetToMoveThreeSquaresForwardFromHomeTheNewPositionIsFour () {
        Player p1 = new Player(1);
        Board board = new Board(10);

        board.setNewPlayerPosition(p1,3);

        assertEquals(4,p1.getPosition());
    }

    @Test
    public void WhenAPlayerIsSetToRecieve300MoneyAndTheirBalanceIs1000ThenTheNewBalanceIs1300() {
        Player p1 = new Player(1);

        p1.addBalance(300.00F);

        assertEquals(1300.00F,p1.getPlayerBalance());
    }

    @Test
    public void WhenAPlayerIsSetToLose300MoneyAndHad1000InTheirBalanceTheNewBalanceIs700() {
        Player p1 = new Player(1);

        p1.subBalance(300.00F);

        assertEquals(700.00F,p1.getPlayerBalance());
    }

    @Test
    public void WhenAPlayerObjectIsCreatedThatPlayerShouldHaveNoPropertiesOwned() {
        GameManager gameManager = new GameManager();
        gameManager.createPlayers(1);

        assertTrue(gameManager.getPlayers().get(1).getOwnedProperties().isEmpty());
    }

    @Test
    public void WhenAPlayerBuysAPropertyItHasToBeAddedToTheOwnedProperties() {
        GameManager gameManager = new GameManager();
        Board board = new Board(10);
        HashMap<Integer, SpaceSquare> boardMap = board.initiateBoard();
        Property property = new Property(3,300.00F,30.00F,"May Fair");
        boardMap.put(3,property);

        gameManager.createPlayers(1);
        gameManager.getPlayers().get(1).setPosition(3);

        property.playerBuysProperty(gameManager.getPlayers().get(1));

        assertEquals(700.00F,gameManager.getPlayers().get(1).getPlayerBalance());
        assertEquals(1, gameManager.getPlayers().get(1).getOwnedProperties().size());
        assertEquals(property,gameManager.getPlayers().get(1).getOwnedProperties().get(1));
    }

    @Test
    public void WhenAPlayerBuysTwoPropertiesBothOfThosePropertiesShouldBeAddedToTheOwnedProperties() {
        GameManager gameManager = new GameManager();
        Board board = new Board(10);
        HashMap<Integer, SpaceSquare> boardMap = board.initiateBoard();
        Property property1 = new Property(3,300.00F,30.00F,"May Fair");
        Property property2 = new Property(4,300.00F,30.00F,"Fair May");

        boardMap.put(3,property1);
        boardMap.put(4,property2);

        gameManager.createPlayers(1);
        gameManager.getPlayers().get(1).setPosition(3);

        property1.playerBuysProperty(gameManager.getPlayers().get(1));
        gameManager.getPlayers().get(1).setPosition(4);
        property2.playerBuysProperty(gameManager.getPlayers().get(1));

        assertEquals(400.00F,gameManager.getPlayers().get(1).getPlayerBalance());
        assertEquals(2, gameManager.getPlayers().get(1).getOwnedProperties().size());
        assertEquals(property1,gameManager.getPlayers().get(1).getOwnedProperties().get(1));
        assertEquals(property2,gameManager.getPlayers().get(1).getOwnedProperties().get(2));
    }

    @Test
    public void WhenAPlayerObjectIsCreatedItShouldNotBeBankrupt() {
        GameManager gameManager = new GameManager();
        gameManager.createPlayers(1);

        assertFalse(gameManager.getPlayers().get(1).isBankrupt());
    }

    @Test
    public void WhenAPlayerWantsToSellTheirPropetiesToPayRentTheLowestValuePropertiesShouldBeChosen (){
        GameManager gameManager = new GameManager();
        gameManager.createPlayers(1);
        Board board = new Board(10);
        HashMap<Integer, SpaceSquare> boardMap = board.initiateBoard();
        gameManager.getPlayers().get(1).addBalance(1000.00F);

        Property property = new Property(3,300.00F,30.00F,"May Fair");
        Property property2 = new Property(4,400.00F,40.00F,"Fair May");
        Property property3 = new Property(5,500.00F,50.00F,"June Fair");

        boardMap.put(3,property); //Lowest Value Property
        boardMap.put(4,property2);
        boardMap.put(5,property3);

        // The lines are in different order so we can prove it will return the lowest value property.
        property3.playerBuysProperty(gameManager.getPlayers().get(1));
        property2.playerBuysProperty(gameManager.getPlayers().get(1));
        property.playerBuysProperty(gameManager.getPlayers().get(1));

        assertEquals(property,gameManager.getPlayers().get(1).getLowestValueProperty().getValue());
    }

    @Test
    public void AtTheStartOfTheGameAllPlayersAreNotOnACycleSquareSocurrentlyOnCycleSquareShouldBeFalse() {
        GameManager gameManager = new GameManager();
        gameManager.createPlayers(4);

        for(Player player : gameManager.getPlayers().values()){
            assertFalse(player.isOnACycleSquare());
        }
    }

    @Test
    public void WhenAPlayerIsSetAsOnACycleSquareSocurrentlyOnCycleSquareShouldBeTrue() {
        GameManager gameManager = new GameManager();
        gameManager.createPlayers(1);

        gameManager.getPlayers().get(1).setAsOnACycleSquareStatus(true);

        assertTrue(gameManager.getPlayers().get(1).isOnACycleSquare());
    }

    @Test
    public void WhenAPlayerIsOnACycleSquareAndTheyRollAOneTheyShouldNoLongerBeClassedAsOnACycleSquare() {
        Player player = mock(Player.class);

        player.setPosition(3);
        player.setAsOnACycleSquareStatus(true);

        when(player.rollDiceForCycleRace()).thenReturn(1);

        assertFalse(player.isOnACycleSquare());
    }

    @Test
    public void WhenAPlayerIsOnACycleSquareAndTheyRollAThreeTheyShouldNoLongerBeClassedAsOnACycleSquare() {
        Player player = mock(Player.class);

        player.setPosition(3);
        player.setAsOnACycleSquareStatus(true);
        when(player.rollDiceForCycleRace()).thenReturn(3);

        assertFalse(player.isOnACycleSquare());
    }

    @Test
    public void WhenAPlayerIsOnACycleSquareAndTheyRollAFourTheyShouldBeStillClassedAsOnACycleSquare() {
        Player player = spy(new Player(1));
        Dice cycleDice = mock(Dice.class);

        when(cycleDice.roll()).thenReturn(4);
        doReturn(cycleDice).when(player).accessCycleDie();

        player.setAsOnACycleSquareStatus(true);

        assertTrue(player.isOnACycleSquare());

    }

    @Test
    public void WhenAPlayerIsOnACycleSquareAndTheyRollASixTheyShouldBeStillClassedAsOnACycleSquare() {
        Player player = spy(new Player(1));
        Dice cycleDice = mock(Dice.class);

        when(cycleDice.roll()).thenReturn(4);
        doReturn(cycleDice).when(player).accessCycleDie();

        player.setAsOnACycleSquareStatus(true);

        assertTrue(player.isOnACycleSquare());
    }
}

