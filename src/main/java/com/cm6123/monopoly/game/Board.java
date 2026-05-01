package com.cm6123.monopoly.game;

import java.util.Collection;
import java.util.HashMap;

public class Board {
    /**
     * Holds the size of the board.
     */
    private int size;
    /**
     * The hash map which stores the objects of SpaceSquares inside and indexes them.
     */
    private HashMap<Integer,SpaceSquare> boardMap = new HashMap<>();
    /**
     * The constructor for the board object.
     * @param sizeOfBoard Passes in the size of the board.
     */
    public Board(final int sizeOfBoard) {
        this.size = sizeOfBoard;
    }
    /**
     * Adds the SpaceSquare objects into the hashmap boardMap.
     * @return The finished created board map.
     */
    public HashMap<Integer,SpaceSquare> initiateBoard() {
        System.out.println(size);
        HomeSpace homeSpace = new HomeSpace();
        boardMap.put(1, homeSpace);
        for (int i = 2; i <= size; i++) {
            boardMap.put(i, new Road(i));
        }
        return boardMap;
    }
    /**
     * Returns the hashmap of the board.
     * @return The hashmap containing the SpaceSquare objects inside.
     */
    public HashMap<Integer, SpaceSquare> getBoardMap() {
        return boardMap;
    }
    /**
     * Calculates and sets the new player position.
     * @param currentPlayer Takes in the current player object
     * @param totalDiceNumber Tales in the totalDiceNumber the player just rolled.
     */
    public void setNewPlayerPosition(final Player currentPlayer,final int totalDiceNumber){
        int newPosition;
        if (currentPlayer.getPosition()+totalDiceNumber > size){
            newPosition = (currentPlayer.getPosition()+totalDiceNumber)-size;
            currentPlayer.addBalance(200.00F);
        }else{
            newPosition = currentPlayer.getPosition()+totalDiceNumber;
        }
        currentPlayer.setPosition(newPosition);
    }
}
