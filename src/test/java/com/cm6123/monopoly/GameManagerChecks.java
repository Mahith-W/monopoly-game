package com.cm6123.monopoly;

import com.cm6123.monopoly.game.Board;
import com.cm6123.monopoly.game.GameManager;
import com.cm6123.monopoly.game.Player;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

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

        gameManager.playerTurnSystem(1,board);

        verify(players.get(1), times(1)).rollDice();
        verify(players.get(2), times(1)).rollDice();

        // GameManager gameManager = spy(new GameManager());

        // Player player1 = mock(Player.class);
        // Player player2 = mock(Player.class);
        // Board board = mock(Board.class);

        // HashMap<Integer, Player> players = new HashMap<>();
        // players.put(1, player1);
        // players.put(2, player2);

        // doReturn(players).when(gameManager).createPlayers(2);
        // gameManager.createPlayers(2);
        // doNothing().when(gameManager).determineWinner(any());

        // when(player1.rollDice()).thenReturn(3);
        // when(player2.rollDice()).thenReturn(2);

        // gameManager.playerTurnSystem(1, board);
        // verify(player1, times(1)).rollDice();
        // verify(player2, times(1)).rollDice();
        // verify(board).setNewPlayerPosition(player1, 4);
        // verify(board).setNewPlayerPosition(player2, 3);

    }
}
