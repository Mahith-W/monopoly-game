package com.cm6123.monopoly.game;

public abstract class SpaceSquare {
    /**
     * Holds the value of the current square object's position on the board.
     */
    private final int squarePosition;
    /**
     * Holds the value for what type of square this is (Home,Road or Property).
     */
    private final String squareType;

    /**
     * The constructor for all the squares on the board. This is the parent class for this.
     * @param positionOfTheSquare Gives in the position of the square on the board
     * @param typeOfSquare Gives in the type of square for that object.
     */
    SpaceSquare(final int positionOfTheSquare,final String typeOfSquare) {
        this.squarePosition = positionOfTheSquare;
        this.squareType = typeOfSquare;
    }
    /**
     * Gets the type of square each SpaceSquare object is.
     * @return The type of the SpaceSquare (either Home,Road or Property).
     */
    public String getTypeOfSquare() {
        return this.squareType;
    }
    /**
     * Gets the position of the square.
     * @return The position of the current square object.
     */
    public int getPositionOfSquare() {
        return this.squarePosition;
    }
}
