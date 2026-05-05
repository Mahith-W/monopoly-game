package com.cm6123.monopoly.game;

import com.cm6123.monopoly.dice.Dice;

import java.util.HashMap;
import java.util.Map;

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
     * Holds a hashmap of the list of owned property objects of the current player.
     */
    private HashMap<Integer, Property> ownedProperties = new HashMap<>();
    /**
     * False if the player object is bankrupt or True if the player is bankrupt.
     */
    private boolean bankRupt = false;
    /**
     * Holds True if the player has landed on a cycle square, otherwise it is false.
     */
    private boolean currentlyOnCycleSquare = false;
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
        System.out.println("You rolled a total of " + addition);
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
        System.out.println("Player "+this.getPlayerNumber()+ "'s balance is now $" + this.balance);
    }
    /**
     * Takes away the player balance by the passed in amount.
     * @param subMoneyAmount The amount of money you want to be substracted from the player.
     */
    public void subBalance(final float subMoneyAmount){
        this.balance = balance - subMoneyAmount;
        System.out.println("Player "+this.getPlayerNumber()+ "'s balance is now $" + this.balance);
    }
    /**
     * Returns the hashmap containing the currently owned properties by this player object.
     * @return Returns the hashmap that contains owned properties by this player object.
     */
    public HashMap<Integer, Property> getOwnedProperties() {
        return this.ownedProperties;
    }
    /**
     * Adds a passed in Property object to the ownedProperties hashmap.
     * @param purchasingPropertyObject
     */
    public void addOwnedProperty(final Property purchasingPropertyObject) {
        if(this.ownedProperties.isEmpty()){
            ownedProperties.put(1,purchasingPropertyObject);
        }else{
            ownedProperties.put(ownedProperties.size()+1,purchasingPropertyObject);
        }
    }
    /**
     * Returns true if the player is bankrupt otherwise it returns false.
     * @return True if the player is bankrupt or False if the player is not bankrupt.
     */
    public boolean isBankrupt() {
        return bankRupt;
    }
    /**
     * Finds the lowest value property from the hashmap of stored properties and goes through each item.
     * @return The lowest value property the player owns.
     */
    public Map.Entry<Integer, Property> getLowestValueProperty() {
        Map.Entry<Integer,Property> lowestValue = null;
        for(Map.Entry<Integer, Property> entry : ownedProperties.entrySet()){
            if(lowestValue == null || entry.getValue().getValueOfProperty() < lowestValue.getValue().getValueOfProperty()){
                lowestValue = entry;
            }
        }
        return lowestValue;
    }
    /**
     * Sets the current player object as bankrupt, and they are out of the game.
     */
    public void setAsBankrupt() {
        this.bankRupt = true;
    }
    /**
     * Calculates the 1% of all rental price of all owned properties of the player. This is added to the 200 in the
     * Board class.
     * @return 1% of all owned property rental prices.
     */
    public float calculate1PercentOfRentalPrice() {
        float percentOfRentalPrice = 0;
        if(!ownedProperties.isEmpty()){
            for(Property property : ownedProperties.values()){
                percentOfRentalPrice += property.getRentalPrice()*0.01F;
            }
        }else {
            return percentOfRentalPrice;
        }
        return percentOfRentalPrice;
    }
    /**
     * A method to create and access a Die for the Cycle Race because It has to be mockable when testing.
     * @return A singular dice object which is rolled when the player lands on a Cycle Race Space.
     */
    public Dice accessCycleDie() {
        Dice cycleDice = new Dice(6);
        return cycleDice;
    }
    /**
     * Rolls a singular die for the cycle race function, and if the dice rolls 1,2,3 the player is free to move (Return True) and
     * 4,5,6 will mean they don't be able to move (returns False).
     * @return If the die lands 1,2,3 then the player is able to move (returns True) 4,5,6 means they won't be able to move.
     */
    public int rollDiceForCycleRace() {
        boolean playerCanMove = false;
        Dice cycleDice = accessCycleDie();
        int value = cycleDice.roll();
        if(value <= 3){
            playerCanMove = true;
            setAsOnACycleSquareStatus(false);
            return value;
        }
        return value;
    }
    /**
     * Returns True if the player is currently on a cycle square otherwise it Returns false.
     * @return True if the current player object is on a Cycle Square otherwise False.
     */
    public boolean isOnACycleSquare() {
        return this.currentlyOnCycleSquare;
    }
    /**
     * Sets the current player object as on a Cycle Square.
     * @param status Passes in the wanted status of the player's CycleSquare. Can pass in true for saying the player is in on
     *               a SpaceSquare or False for them not being on a cycle square anymore.
     */
    public void setAsOnACycleSquareStatus(final boolean status) {
        this.currentlyOnCycleSquare = status;
    }
}
