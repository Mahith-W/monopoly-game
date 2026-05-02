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
    public void WhenThePlayerPassesTheHomeSquareTheBalanceIsIncreasedBy200() {
        Player player = new Player(1);
        Board board = new Board(10);
        board.initiateBoard();
        board.setNewPlayerPosition(player, 10);

        assertEquals(1200.00F, player.getPlayerBalance());
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
}
