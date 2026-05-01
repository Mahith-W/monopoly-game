package com.cm6123.monopoly.game;

public class Property extends SpaceSquare{
    /**
     * Holds the purchase value of the property.
     */
    private float propertyValue;
    /**
     * Holds the rental value for the property.
     */
    private float rentalPrice;
    /**
     * Contains the player object of the player who owns the property.
     */
    private Player owner;
    /**
     * The constructor for the property class.
     *
     * @param positionOfTheSquare Gives in the position of the square on the board
     */
    Property(final int positionOfTheSquare) {
        super(positionOfTheSquare, "Property");
    }
}
