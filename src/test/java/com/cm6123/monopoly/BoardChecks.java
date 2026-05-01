package com.cm6123.monopoly;

import com.cm6123.monopoly.game.Board;
import com.cm6123.monopoly.game.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardChecks {

    @Test
    public void WhenTheBoardIsInitiatedTheFirstSquareHasToBeTheHomeSquare() {
        Board board = new Board(16);
        board.initiateBoard();
        assertEquals("Home", board.getBoardMap().get(1).getTypeOfSquare());
        board.getBoardMap().clear();
    }

    @Test
    public void WhenYouMakeAABoardOfSizeTenTheSizeOfTheHashMapHasToBeTen() {
        Board board = new Board(10);
        board.initiateBoard();
        assertEquals(10,board.getBoardMap().size());
        board.getBoardMap().clear();
    }

    @Test
    public void WhenThePlayerPassesTheHomeSquareTheBalanceIsIncreasedBy200() {
        Player player = new Player(1);
        Board board = new Board(10);
        board.initiateBoard();
        board.setNewPlayerPosition(player, 10);

        assertEquals(1200.00F, player.getPlayerBalance());
    }
}
