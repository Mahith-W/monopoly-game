package com.cm6123.monopoly;

import com.cm6123.monopoly.app.UserInputManager;
import com.cm6123.monopoly.game.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GameManagerChecks {

    @Test
    public void WhenIRequestOnePlayerToBeAddedToTheCreatePlayerTheHashMapLengthHasToBe1() {
        GameManager gameManager = new GameManager();
        gameManager.createPlayers(1);

        assertEquals(1, gameManager.getPlayers().size());
    }
    @Test
    public void WhenTwoPlayersAreRequestedToBeAddedToTheCreatePlayerTheHashMapLengthHasToBe2() {
        GameManager gameManager = new GameManager();
        gameManager.createPlayers(2);
        assertEquals(2, gameManager.getPlayers().size());
    }
    @Test
    public void TheKeyOfThePlayersHashMapMustBeTheSameAsThePlayerNumberInEachPlayerObject(){
        GameManager gameManager = new GameManager();
        HashMap<Integer, Player> players = gameManager.createPlayers(3);

        for (int i : players.keySet()) {
            assertEquals(i, gameManager.getPlayers().get(i).getPlayerNumber());
        }
    }
    @Test
    public void When2PlayersArePlayingTheWinnerHasToBeThePlayerThatHasTheHigherBalance() {
        GameManager gameManager = new GameManager();
        HashMap<Integer, Player> players = gameManager.createPlayers(2);
        players.get(1).addBalance(2000.00F); // Player 1 is higher
        players.get(2).addBalance(1000.00F);
        assertEquals(players.get(1),gameManager.determineWinner(players));
    }
/*
    AI was used in the test below to understand how mocks work and why my previous attempt failed (difference between
    a spy and a mock). The tests below this are me reflecting on that and progressively understood mocks better.
 */
    @Test
    public void When2PlayersArePlayingTheyBothShouldHaveOneTurnWhenTheNumberOfTurnsForTheGameIsOne () {
        GameManager gameManager = new GameManager();
        HashMap<Integer, Player> players = gameManager.createPlayers(1);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        players.remove(1);
        players.put(1, player1);
        players.put(2, player2);
        Board board = mock(Board.class);
        gameManager.setPlayers(players);

        when(players.get(1).rollDice()).thenReturn(3);
        when(players.get(2).rollDice()).thenReturn(2);

        gameManager.playerTurnSystem(1,board,board.initiateBoard());

        verify(players.get(1), times(1)).rollDice();
        verify(players.get(2), times(1)).rollDice();
    }


    @Test
    public void WhenAPlayerIsBankruptTheirTurnIsSkipped() {
        GameManager gameManager = new GameManager();
        HashMap<Integer,Player> players= gameManager.createPlayers(3);
        players.clear();
        Board board = mock(Board.class);
        HashMap<Integer,SpaceSquare> boardMap = board.initiateBoard();
        Property property = new Property(2,300.00F,30.00F,"May Fair");
        boardMap.put(2,property);

        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Player player3 = mock(Player.class);
        players.put(1, player1);
        players.put(2, player2);
        players.put(3, player3);

        when(players.get(1).rollDice()).thenReturn(3);
        when(players.get(2).rollDice()).thenReturn(2);
        when(players.get(3).rollDice()).thenReturn(3);
        when(player2.isBankrupt()).thenReturn(true);
        property.playerBuysProperty(player1);
        player2.subBalance(1000.00F);

        property.carryOutRental(player2);
        property.carryOutRental(player3);

        gameManager.playerTurnSystem(3,board,boardMap);

        verify(gameManager.getPlayers().get(1), times(3)).rollDice();
        verify(gameManager.getPlayers().get(2), times(0)).rollDice();
        verify(gameManager.getPlayers().get(3), times(3)).rollDice();
    }

    @Test
    public void WhenAPlayerHasNotLandedOnACircleSquareTheyShouldNotBeRollingTheCircleDieItShouldBeTheDefault2Dice() {
        GameManager gameManager = new GameManager();
        HashMap<Integer,Player> players= gameManager.createPlayers(1);
        Player player1 = mock(Player.class);
        players.put(1, player1);

        Board board = mock(Board.class);
        HashMap<Integer,SpaceSquare> boardMap = board.initiateBoard();

        gameManager.playerTurnSystem(1,board,boardMap);

        verify(player1, times(1)).rollDice();
        verify(player1, times(0)).rollDiceForCycleRace();
    }

    @Test
    public void WhenAPlayerHasLandedOnACircleSquareTheyShouldNotBeRollingTheNormalDiceItShouldBeTheCircleDie() {
        GameManager gameManager = new GameManager();
        HashMap<Integer,Player> players= gameManager.createPlayers(1);
        Player player1 = mock(Player.class);
        players.put(1, player1);

        Board board = mock(Board.class);
        HashMap<Integer,SpaceSquare> boardMap = board.initiateBoard();
        player1.setAsOnACycleSquareStatus(true);
        when(player1.isOnACycleSquare()).thenReturn(true);

        gameManager.playerTurnSystem(1,board,boardMap);

        verify(player1, times(0)).rollDice();
        verify(player1, times(1)).rollDiceForCycleRace();

    }

    @Test
    public void WhenAPlayerHasLandedOnACircleSquareAndTheyRollANumberLessThanOrEqualTo3TheyShouldMoveOutOfTheCircleSpaceAndMoveTheNumberOfSpaces() {
        GameManager gameManager = new GameManager();
        HashMap<Integer,Player> players= gameManager.createPlayers(1);
        Player player1 = mock(Player.class);
        players.put(1, player1);

        Board board = mock(Board.class);
        HashMap<Integer,SpaceSquare> boardMap = board.initiateBoard();

        when(player1.isOnACycleSquare()).thenReturn(true);
        when(player1.getPosition()).thenReturn(3);
        when(player1.rollDiceForCycleRace()).thenReturn(3);
        gameManager.playerTurnSystem(1,board,boardMap);

        when(player1.isOnACycleSquare()).thenReturn(false);
        when(player1.getPosition()).thenReturn(6);
        gameManager.playerTurnSystem(1,board,boardMap);

        verify(player1, times(1)).rollDice();
        verify(player1, times(1)).rollDiceForCycleRace();
    }

}
