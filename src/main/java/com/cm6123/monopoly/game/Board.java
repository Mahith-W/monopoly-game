package com.cm6123.monopoly.game;

import com.cm6123.monopoly.app.UserInputManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Board {
    /**
     * Holds the size of the board.
     */
    private final int size;
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
            float passingHomeMoney = 200.00F + currentPlayer.calculate1PercentOfRentalPrice();
            System.out.println("You passed home and recieved $"+passingHomeMoney);
            currentPlayer.addBalance(passingHomeMoney);
        }else{
            newPosition = currentPlayer.getPosition()+totalDiceNumber;
        }
        currentPlayer.setPosition(newPosition);
        System.out.println("Player "+currentPlayer.getPlayerNumber()+" is now at position "+currentPlayer.getPosition());
    }
    /**
     * Calculates the amount of properties that is porpotional to the size of the board.
     * @return The number of properties we need to include in the board.
     */
    public int calculateTheAmountOfProperties(){
        int amountOfProperties = Math.toIntExact(Math.round(size * 0.6));
        return amountOfProperties;
    }
    /**
     * Uses the board made before and adds the properties to the map and updates it.
     * Link to code that I used as inspiration:- https://www.w3schools.com/java/java_files_read.asp.
     * @param mapWHomeAndRoad The hashmap of the map containing just a home and roads.
     * @param amountOfProperties The number of properties that are required for the board to contain.
     * @return The updated hashmap of the board containing the properties.
     */
    public HashMap<Integer,SpaceSquare> addPropertiesTOBoard(final HashMap<Integer,SpaceSquare> mapWHomeAndRoad, final int amountOfProperties){
        int max = mapWHomeAndRoad.size();
        int min = 2;
        Random rand = new Random();
        int counter = 0;
        File listOfPropertyNames = new File("txtFileFolder/listOfPropertyNames.txt");
        try (Scanner scanner = new Scanner(listOfPropertyNames)) {
            while (counter < amountOfProperties) {
                int propertyPosition = rand.nextInt(max - min + 1) + min;
                if (mapWHomeAndRoad.get(propertyPosition) instanceof Road) {
                    mapWHomeAndRoad.put(propertyPosition, new Property(propertyPosition, 300.00F, 30.00F, scanner.nextLine()));
                    counter++;
                }
                propertyPosition = rand.nextInt(max - min + 1) + min;
            }
        }catch (final FileNotFoundException e){
            System.out.println("File not found");
        }
        return mapWHomeAndRoad;
    }
    /**
     * Checks which type of block the player has landed on, depending on that we do the action.
     * @param mapWHomeRoadCircle This is the hashmap of the board with properties.
     * @param currentPlayer This is the Player object of the player who wants to buy the property.
     */
    public void landedOn(final HashMap<Integer,SpaceSquare> mapWHomeRoadCircle, final Player currentPlayer){
        if (mapWHomeRoadCircle.get(currentPlayer.getPosition()) instanceof Property currentProperty && currentProperty.returnOwner() == null){
            System.out.println("You landed on "+currentProperty.getPropertyName()+" with a purchase price of $"+currentProperty.getValueOfProperty());
            UserInputManager userInputManager = new UserInputManager();
            if(userInputManager.doesPlayerWantToBuyProperty()){
                currentProperty.playerBuysProperty(currentPlayer);
            }
        } else if (mapWHomeRoadCircle.get(currentPlayer.getPosition()) instanceof Property currentProperty && currentProperty.returnOwner() != currentPlayer) {
            System.out.println("You landed on "+currentProperty.getPropertyName()+" which is owned by Player "+currentProperty.returnOwner().getPlayerNumber());
            System.out.println("You have to pay RENT!");
            currentProperty.carryOutRental(currentPlayer);
        } else if (mapWHomeRoadCircle.get(currentPlayer.getPosition()) instanceof CycleRace) {
            System.out.println("You landed on Cycle Space and the Road is CLOSED, roll 1,2 or 3 to continue!");
            currentPlayer.setAsOnACycleSquareStatus(true);
        }
    }
    /**
     * Calculates the number of cycle squares the boardMap should contain. Since we need one square at least and the size of
     * the board has to be 10-50. If I put 10% of the board should be cycle squares. We should get a reasonable amount of cycle
     * squares.
     * @return The number of Cycle squares to put into the board.
     */
    public int calculateAmountOfCycleSquares() {
        int amountOfCycleSquares = Math.toIntExact(Math.round(size * 0.1));
        return amountOfCycleSquares;
    }
    /**
     * Adds the Cycle race squares to the board map depending on the amount of calculated number of cycle squares.
     * @param mapWHomeRoadAndProperty
     * @param amountOfCycleSquares
     * @return The boardMap containing the Home, Roads, Properties and Circle Square.
     */
    public HashMap<Integer, SpaceSquare> addCycleSquare(final HashMap<Integer,SpaceSquare> mapWHomeRoadAndProperty, final int amountOfCycleSquares) {
        int max = mapWHomeRoadAndProperty.size();
        int min = 2;
        Random rand = new Random();
        int counter = 0;
        while(counter < amountOfCycleSquares){
            int cyclePosition = rand.nextInt(max - min + 1) + min;
            if (mapWHomeRoadAndProperty.get(cyclePosition) instanceof Road) {
                mapWHomeRoadAndProperty.put(cyclePosition,new CycleRace(cyclePosition));
                counter++;
            }
            cyclePosition = rand.nextInt(max - min + 1) + min;
        }
        return mapWHomeRoadAndProperty;
    }
}
