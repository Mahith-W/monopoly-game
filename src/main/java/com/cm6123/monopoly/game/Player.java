package com.cm6123.monopoly.game;

import com.cm6123.monopoly.dice.Dice;

public class Player {
    /**
     * Holds the player number for each player.
     */
    private final int playerNumber;
    /**
     * Holds the Player Balance.
     */
    private float balance;
    /**
     * Holds the current position of the player.
     */
    private int playerPosition;
    /**
     * Constructor for the player class.
     * @param currentPlayerNumber Holds the current player number to assign to the creating player
     *                            object.
     */
    public Player(final int currentPlayerNumber){
        this.playerNumber = currentPlayerNumber;
        this.balance = 1000;
        this.playerPosition = 1;
    }
    /**
     * Returns the current object's player number.
     * @return The player object's player number.
     */
    public int getPlayerNumber() {
        return playerNumber;
    }
    /**
     * Returns the balance of the current player.
     * @return The current balance of the player.
     */
    public float getPlayerBalance() {
        return balance;
    }
    /**
     * Returns the current position of the player.
     * @return The current position of the player.
     */
    public int getPosition() {
        return playerPosition;
    }
    /**
     * Rolls the two dice and returns the total number of spaces the player has to more next by.
     * @return the total number of spaces the player has to move by.
     */
    public int rollDice(){
        Dice dice1 = new Dice(6);
        Dice dice2 = new Dice(6);
        int addition = dice1.roll() + dice2.roll();
        return addition;
    }
    /**
     * Sets the new position for the player depending on the totalDiceScore they got.
     * @param newPosition The new position which was calculated in the board class.
     */
    public void setPosition(final int newPosition){
        this.playerPosition = newPosition;
    }
    /**
     * Increases the balance of the current player by the amount that's passed in.
     * @param newMoney The money which the player has recieved to be added to their balance.
     */
    public void addBalance(final float newMoney){
        this.balance = balance + newMoney;
    }
}
