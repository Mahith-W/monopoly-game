package com.cm6123.monopoly;

import com.cm6123.monopoly.game.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    public void WhenTheBoardSizeIsTenItShouldHaveFiveProperties() { //Test has changed because of the need to make space of Underground Squares.
        Board board = new Board(10);
        int numOfProperties = board.calculateTheAmountOfProperties();
        assertEquals(5,numOfProperties);
    }

    @Test
    public void WhenTheBoardSizeIsTenItShouldHaveFivePropertiesInTheHashMap() { //Test has changed because of the need to make space of Underground Squares.
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
        assertEquals(5,count);
    }

    @Test
    public void WhenTheBoardSizeIsGreaterThan11TheNumberOfPropertiesOnTheBoardShouldRoughlyBe60Percent() { //Roughly because of the int rounder
        Board board = new Board(12);
        HashMap<Integer, SpaceSquare> hashBoard = board.initiateBoard();
        int numOfProperties = board.calculateTheAmountOfProperties();
        System.out.println(numOfProperties);
        board.addPropertiesTOBoard(hashBoard,numOfProperties);

        int count = 0;
        for (SpaceSquare spaceSquare : hashBoard.values()) {
            if (spaceSquare instanceof Property){
                count++;
            }
        }
        float percentageOfProperties = count / (float) 12;
        assertTrue(percentageOfProperties <= 0.65 && percentageOfProperties >= 0.55);

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

    @Test
    public void IfABoardMapIsCreatedWithJustOneUndergroundStationThenThatBoardShouldBeRejected() {
        Board board = new Board(10);
        HashMap<Integer, SpaceSquare> hashBoard = board.initiateBoard();
        hashBoard = board.addUnderGroundStation(hashBoard,1);

        int count = 0;

        for (SpaceSquare spaceSquare : hashBoard.values()) {
            if (spaceSquare instanceof UndergroundStation){
                count++;
            }
        }

        assertEquals(0,count);
    }

    @Test
    public void IfABoardMapIsRequestedToBeCreatedWithTwoUndergroundStationsThenThereShouldBeTwoUndergroundStations() {
        Board board = new Board(10);
        HashMap<Integer, SpaceSquare> hashBoard = board.initiateBoard();
        hashBoard = board.addUnderGroundStation(hashBoard,2);

        int count = 0;
        for (SpaceSquare spaceSquare : hashBoard.values()) {
            if (spaceSquare instanceof UndergroundStation){
                count++;
            }
        }
        assertEquals(2,count);
    }

    @Test
    public void WhenTwoUnderGroundStationsAreAddedThenTheirPositionsShouldBeAccessible() {
        Board board = new Board(10);
        HashMap<Integer, SpaceSquare> hashBoard = board.initiateBoard();
        UndergroundStation station1 = new UndergroundStation(4);
        UndergroundStation station2 = new UndergroundStation(5);

        hashBoard.put(4,station1);
        hashBoard.put(5,station2);

        HashMap<SpaceSquare,Integer> positionsOfUStations = board.findPositionsOfUndergroundStations(hashBoard);

        assertEquals(4,positionsOfUStations.get(station1));
        assertEquals(5,positionsOfUStations.get(station2));
    }

    @Test
    public void WhenTwoUnderGroundStationsAreCreatedUsingAFunctionThanManuallyWeShouldBeAbleToGetTheirPositions() {
        Board board = new Board(10);
        HashMap<Integer, SpaceSquare> hashBoard = board.initiateBoard();

        board.addUnderGroundStation(hashBoard,2);

        HashMap<SpaceSquare,Integer> positionsOfUStations = board.findPositionsOfUndergroundStations(hashBoard);


        for(SpaceSquare spaceSquare : positionsOfUStations.keySet()){
            assertEquals(spaceSquare,hashBoard.get(positionsOfUStations.get(spaceSquare)));
        }
    }

    @Test
    public void WhenAPlayerLandsOnAnUndergroundStationThenThetransferPlayerFunctionShouldBeTriggered() {
        Player player1 = mock(Player.class);
        Board board = spy(new Board(10));
        HashMap<Integer, SpaceSquare> hashBoard = board.initiateBoard();

        UndergroundStation station1 = new UndergroundStation(4);
        UndergroundStation station2 = new UndergroundStation(8);

        hashBoard.put(4,station1);
        hashBoard.put(8,station2);
        HashMap<SpaceSquare,Integer> positionsOfUStations = board.findPositionsOfUndergroundStations(hashBoard);

        when(player1.getPosition()).thenReturn(4);
        board.landedOn(hashBoard,player1);

        verify(board,times(1)).transferPlayerFromStationToStation(positionsOfUStations,player1,hashBoard);
    }

    @Test
    public void WhenAPlayerLandsOnAnUndergroundStationThenThePlayersNewPositionShouldBeOntheOtherStation() {
        Player player1 = new Player(1);
        Board board = new Board(10);
        HashMap<Integer, SpaceSquare> hashBoard = board.initiateBoard();

        UndergroundStation station1 = new UndergroundStation(4);
        UndergroundStation station2 = new UndergroundStation(8);

        hashBoard.put(4,station1);
        hashBoard.put(8,station2);

        player1.setPosition(4);
        board.landedOn(hashBoard,player1);

        assertEquals(8,player1.getPosition());
    }

    @Test
    public void WhenThreeUndergroundStationsAreRequestedToBeAddedThereShouldBeThreeOfThemOntheGameBoard() {
        Board board = new Board(10);
        HashMap<Integer, SpaceSquare> hashBoard = board.initiateBoard();
        board.addUnderGroundStation(hashBoard,3);

        int count = 0;
        for (SpaceSquare spaceSquare : hashBoard.values()) {
            if (spaceSquare instanceof UndergroundStation){
                count++;
            }
        }
        assertEquals(3,count);
    }
}
