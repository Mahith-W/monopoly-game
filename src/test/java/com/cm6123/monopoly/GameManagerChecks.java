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
    The first time I used mockito I had a go at it and it failed. I know the reason why now(in this commit) and it's because
    I was doing logic inside a mock which doesn't just work (should have used a spy). After I wrote the failed code I copied
    and gave it to gpt with the error message on the console asking it what went wrong. I don't have access to that chat anymore
    unfortunately because I was not signed in to ChatGPT. Above is the best way I can describe the prompt, the code GPT gave did
    explain the difference of a mock and a real object and suggest I should use a real object for logical methods and mocks for well
    the classes that don't need the logic. Another error after that was doNothing() for a method that returned a value which also
    was wrong, but then later on after doing some tweaks myself the code did end up working. This test below I have used AI for. The
    few tests below that are mocks are just me having a go at everything and with practice by the time I came to mocks in other classes (like Board).
    I knew what I was doing. Sorry I couldn't provide the link to the prompt, not being signed in meant the history of the chat was not
    saved online just local while the browser was open.
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

    /*
    This test was me having a go at everything, There is a mock of the Board Class which is unnessasary.
     */
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
    /*
    I understood mojority of mocks here, I still have that board class as an unnessary mock.
     */
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
    /*
    By the time I got to here I pretty much knew what mocks were and had a good experience with it.
     */
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
