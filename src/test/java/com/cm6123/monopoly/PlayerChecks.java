package com.cm6123.monopoly;

import com.cm6123.monopoly.game.Board;
import com.cm6123.monopoly.game.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}

