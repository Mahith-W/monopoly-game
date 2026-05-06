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
    public int calculateTheAmountOfProperties() {
        int amountOfProperties;
        if (boardMap.size() <= 11) { // Done this because 3 Underground stations are a requirement, and we can't fit 3 on board sizes 10 and 11.
            return 5;
        } else {
            amountOfProperties = Math.toIntExact(Math.round(size * 0.6));
        }
        return amountOfProperties;
    }
    /**
     * Uses the board made before and adds the properties to the map and updates it.
     * Link to code that I used as inspiration:- https://www.w3schools.com/java/java_files_read.asp.
     * Also used ChatGPT here lightly to find the "trim()" function. Luckily I got the link for this one :- https://chatgpt.com/s/t_69fb242f5ea881919c0967633628365f
     * Since the link doesn't contain my prompt, I have also put a screenshot of my prompt and a part of GPT's respond in the "txtFileFolder" so it's easier to find.
     * @param mapWHomeAndRoad The hashmap of the map containing just a home and roads.
     * @param amountOfProperties The number of properties that are required for the board to contain.
     * @return The updated hashmap of the board containing the properties.
     */

    public HashMap<Integer,SpaceSquare> addPropertiesTOBoard(final HashMap<Integer,SpaceSquare> mapWHomeAndRoad, final int amountOfProperties){
        int max = mapWHomeAndRoad.size();
        int min = 2;
        Random rand = new Random();
        int counter = 0;
        File listOfPropertyNames = new File("txtFileFolder/PropertiesDetails.txt");
        try (Scanner scanner = new Scanner(listOfPropertyNames)) {
            while (counter < amountOfProperties) {
                int propertyPosition = rand.nextInt(max - min + 1) + min;
                if (mapWHomeAndRoad.get(propertyPosition) instanceof Road) {
                    mapWHomeAndRoad.put(propertyPosition, new Property(propertyPosition, scanner.nextFloat(), scanner.nextFloat(), scanner.nextLine().trim()));
                    counter++;
                }
            }
        }catch (final FileNotFoundException e){
            System.out.println("File not found");
        }
        System.out.println("Properties Added Successfully!");
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
        } else if (mapWHomeRoadCircle.get(currentPlayer.getPosition()) instanceof UndergroundStation) {
            System.out.println("You landed at an Underground station!");
            transferPlayerFromStationToStation(findPositionsOfUndergroundStations(mapWHomeRoadCircle),currentPlayer,mapWHomeRoadCircle);
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
        }
        System.out.println("CycleSquares added successfully");
        return mapWHomeRoadAndProperty;
    }
    /**
     * Adds the underground stations to the board.
     * @param mapWHomeRoadAndProperty
     * @param amountOfUnderGroundStations
     * @return The new updated board map containing the underground stations.
     */
    public HashMap<Integer, SpaceSquare> addUnderGroundStation(final HashMap<Integer,SpaceSquare> mapWHomeRoadAndProperty, final int amountOfUnderGroundStations) {
        if(amountOfUnderGroundStations <=1){
            System.out.println("The board won't contain any underground stations (need a minimum of 2)");
            return mapWHomeRoadAndProperty;
        }else {
            int max = mapWHomeRoadAndProperty.size();
            int min = 2;
            Random rand = new Random();
            int counter = 0;
            while (counter < amountOfUnderGroundStations) {
                int underGroundStation = rand.nextInt(max - min + 1) + min;
                if (mapWHomeRoadAndProperty.get(underGroundStation) instanceof Road) {
                    mapWHomeRoadAndProperty.put(underGroundStation, new UndergroundStation(underGroundStation));
                    counter++;
                }
            }
            System.out.println("UndergroundStations added successfully");
            return mapWHomeRoadAndProperty;
        }
    }
    /**
     * Finds all the positions of the Underground stations and returns it.
     * @param mapWHomeRoadAndProperty The HashMap of the positions of the Underground stations.
     * @return A hashmap containing the Underground station's position.
     */
    public HashMap<SpaceSquare, Integer> findPositionsOfUndergroundStations(final HashMap<Integer,SpaceSquare> mapWHomeRoadAndProperty){
        HashMap<SpaceSquare, Integer> positionsOfStations = new HashMap<>();
        int count = 1;
        for(SpaceSquare spaceSquare : mapWHomeRoadAndProperty.values()){
            if(spaceSquare instanceof UndergroundStation){
                positionsOfStations.put(spaceSquare,spaceSquare.getPositionOfSquare());
                count++;
            }
        }
        return positionsOfStations;
    }
    /**
     * This function is used to transfer the player from one station to the chosen other.
     * I used this code snippet:- https://www.baeldung.com/java-hashmap-random-key-value-entry#:~:text=Getting%20a%20Random%20Entry,to%20retrieve%20the%20corresponding%20value.
     * (Specifically the 3.1 using array) in this function.
     * @param positionsOfStations
     * @param currentPlayer
     * @param mapWHomeRoadAndProperty
     */
    public void transferPlayerFromStationToStation(final HashMap<SpaceSquare, Integer> positionsOfStations,final Player currentPlayer,final HashMap<Integer,SpaceSquare> mapWHomeRoadAndProperty) {
        SpaceSquare currentStation = mapWHomeRoadAndProperty.get(currentPlayer.getPosition());
        SpaceSquare[] listOfstations = positionsOfStations.keySet().toArray(new SpaceSquare[0]);
        Random rand = new Random();
        SpaceSquare destination;
        do {
            destination = listOfstations[rand.nextInt(listOfstations.length)];
        }
        while (destination.equals(currentStation));
        currentPlayer.setPosition(positionsOfStations.get(destination));
        System.out.println("You have been transfered to the station at position "+currentPlayer.getPosition());
        if((positionsOfStations.get(destination)-positionsOfStations.get(currentStation)) < 0){
            float passingHomeMoney = 200.00F + currentPlayer.calculate1PercentOfRentalPrice();
            System.out.println("You have passed home while taking the underground and recieve $"+passingHomeMoney);
        }
    }
}
