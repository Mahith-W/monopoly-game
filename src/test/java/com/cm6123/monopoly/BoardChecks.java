package com.cm6123.monopoly;

import com.cm6123.monopoly.game.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BoardChecks {

    @Test
    public void WhenTheBoardIsInitiatedTheFirstSquareHasToBeTheHomeSquare() {
        Board board = new Board(16);
        board.initiateBoard();
        assertEquals("Home", board.getBoardMap().get(1).getTypeOfSquare());
        board.getBoardMap().clear();
    }

    @Test
    public void WhenYouMakeABoardOfSizeTenTheSizeOfTheHashMapHasToBeTen() {
        Board board = new Board(10);
        board.initiateBoard();
        assertEquals(10,board.getBoardMap().size());
        board.getBoardMap().clear();
    }

    @Test
    public void WhenThePlayerPassesTheHomeSquareTheBalanceIsIncreasedBy200IfTheyOwnNoProperties() {
        Player player = new Player(1);
        Board board = new Board(10);
        board.initiateBoard();
        board.setNewPlayerPosition(player, 10);

        assertEquals(1200.00F, player.getPlayerBalance());
    }

    @Test
    public void WhenThePlayerPassesTheHomeSquareTheBalanceIsIncreasedBy200Plus1PercentOfRentalsIfTheyOwnOneProperty() {
        Player player = new Player(1);
        Board board = new Board(10);
        HashMap<Integer,SpaceSquare> gameBoard = board.initiateBoard();
        Property property = new Property(3,300.00F,30.00F,"May Fair");
        gameBoard.put(3, property);
        player.setPosition(3);

        property.playerBuysProperty(player);
        board.setNewPlayerPosition(player, 10);

        assertEquals(900.30F, player.getPlayerBalance());

    }

    @Test
    public void WhenThePlayerPassesTheHomeSquareTheBalanceIsIncreasedBy200Plus1PercentOfRentalsIfTheyOwnTwoProperties() {
        Player player = new Player(1);
        Board board = new Board(10);
        HashMap<Integer,SpaceSquare> gameBoard = board.initiateBoard();
        Property property = new Property(3,300.00F,30.00F,"May Fair");
        Property property2 = new Property(4,300.00F,30.00F,"Fair May");
        gameBoard.put(3, property);
        gameBoard.put(4, property2);
        player.setPosition(3);
        property.playerBuysProperty(player);
        player.setPosition(4);
        property2.playerBuysProperty(player);

        board.setNewPlayerPosition(player, 10);

        assertEquals(600.60F, player.getPlayerBalance());

    }

    @Test
    public void WhenAPlayerIsOnThe14thSquareOfA16SquareBoardAndTheDiceRolls4TheirNewPositionShouldBe2 () {
        Player player = new Player(1);
        Board board = new Board(16);
        HashMap<Integer, SpaceSquare> gameBoard = board.initiateBoard();
        player.setPosition(14);
        board.setNewPlayerPosition(player, 4);

        assertEquals(2,player.getPosition());

    }

    @Test
    public void WhenTheBoardSizeIsTenItShouldHaveSixProperties() {
        Board board = new Board(10);
        int numOfProperties = board.calculateTheAmountOfProperties();
        assertEquals(6,numOfProperties);
    }

    @Test
    public void WhenTheBoardSizeIsTenItShouldHaveSixPropertiesInTheHashMap() {
        Board board = new Board(10);
        HashMap<Integer, SpaceSquare> hashBoard = board.initiateBoard();
        int numOfProperties = board.calculateTheAmountOfProperties();
        board.addPropertiesTOBoard(hashBoard,numOfProperties);
        int count = 0;
        for (SpaceSquare spaceSquare : hashBoard.values()) {
            if (spaceSquare instanceof Property){
                count++;
            }
        }
        assertEquals(6,count);
    }

    @Test
    public void WhenAPlayerLandsOnAPropertyAndTheyAreNotTheOwnerOfThePropertyTheyNeedToPayRent () {
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        Board board = new Board(10);
        HashMap<Integer, SpaceSquare> hashBoard = board.initiateBoard();
        Property property = new Property(3,300.00F,30.00F,"May Fair");

        hashBoard.put(3,property);
        player1.setPosition(3);
        property.playerBuysProperty(player1);
        player2.setPosition(3);
        board.landedOn(hashBoard,player2);

        assertEquals(970.00F, player2.getPlayerBalance());
        assertEquals(730.00F, player1.getPlayerBalance());
    }

    @Test
    public void WhenTheOwnerOfThePropertyLandsOnTheirPropertyNoMoneyShouldBeTaken () {
        Player player1 = new Player(1);
        Board board = new Board(10);
        HashMap<Integer, SpaceSquare> hashBoard = board.initiateBoard();
        Property property = new Property(3,300.00F,30.00F,"May Fair");

        hashBoard.put(3,property);
        player1.setPosition(3);
        property.playerBuysProperty(player1);
        board.landedOn(hashBoard,player1);

        assertEquals(700.00F, player1.getPlayerBalance());
    }

    @Test
    public void WhenABoardWithTheSizeOf10IsCreatedThenTheCalculatedNumberOfCycleSquaresIsOne () {
        Board board = new Board(10);
        int numOfCycleRace = board.calculateAmountOfCycleSquares();

        assertEquals(1,numOfCycleRace);
    }

    @Test
    public void WhenABoardWithTheSizeOf50IsCreatedThenTheCalculatedNumberOfCycleSquaresIsFive () {
        Board board = new Board(50);
        int numOfCycleRace = board.calculateAmountOfCycleSquares();

        assertEquals(5,numOfCycleRace);
    }

    @Test
    public void WhenABoardWithTheSizeOf10TheNumberOfCycleSquaresCreatedOnTheBoardShouldBeOne () {
        Board board = new Board(10);
        int numOfCycleRace = board.calculateAmountOfCycleSquares();
        HashMap<Integer,SpaceSquare> gameBoard = board.initiateBoard();
        gameBoard = board.addPropertiesTOBoard(gameBoard,1);

        gameBoard = board.addCycleSquare(gameBoard,numOfCycleRace);

        int count = 0;

        for (SpaceSquare spaceSquare : gameBoard.values()) {
            if (spaceSquare instanceof CycleRace){
                count++;
            }
        }

        assertEquals(1,count);
    }

    @Test
    public void WhenABoardWithTheSizeOf50TheNumberOfCycleSquaresCreatedOnTheBoardShouldBeFive () {
        Board board = new Board(50);
        int numOfCycleRace = board.calculateAmountOfCycleSquares();
        HashMap<Integer,SpaceSquare> gameBoard = board.initiateBoard();
        gameBoard = board.addPropertiesTOBoard(gameBoard,1);
        gameBoard = board.addCycleSquare(gameBoard,numOfCycleRace);

        int count = 0;

        for (SpaceSquare spaceSquare : gameBoard.values()) {
            if (spaceSquare instanceof CycleRace){
                count++;
            }
        }

        assertEquals(5,count);
    }

    @Test
    public void WhenAPlayerHasLandedOnTheCycleRaceSquaresetAsOnACycleSquareStatusFunctionShouldBeTriggered() {
        Player player1 = mock(Player.class);

        Board board = new Board(10);
        HashMap<Integer, SpaceSquare> hashBoard = board.initiateBoard();
        CycleRace cycleRace = new CycleRace(3);
        hashBoard.put(3,cycleRace);

        when(player1.getPosition()).thenReturn(3);

        board.landedOn(hashBoard,player1);

        verify(player1,times(1)).setAsOnACycleSquareStatus(true);
    }

}
