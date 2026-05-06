package com.cm6123.monopoly.app;


import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserInputManager {
    /**
     * Holds the user's desired number of players.
     */
    private int userDesiredNumOfPlayers;
    /**
     * Holds the Desired board size from the user.
     */
    private int userDesiredBoardSize;
    /**
     * Stores the scanner privately for the user inputs.
     */
    private Scanner scanner = new Scanner(System.in);
    /**
     * Gets the userinput for the number of players they got, it also uses exceptions to make
     * sure they enter an integer.
     * I watched a YouTube video on Exceptions and the video's code I have used as inspiration here (the link contains the time frame).
     * Link :- https://youtu.be/u1PROb-aRUI?t=258.
     * @return The number of players the user wants (unverified to context/brief so can sometimes
     * be higher than 10 or less than 2.
     */
    public int getUserPlayerCountNumber(){
        System.out.println("Please enter the number of players between 2 and 10: ");
        try{
            userDesiredNumOfPlayers = scanner.nextInt();
            return userDesiredNumOfPlayers;
        }catch(InputMismatchException IME){
            System.out.println("That is not an integer!");
            scanner.nextLine();
            return -1;
        }catch(NoSuchElementException NSEE){
            System.out.println("Please enter an integer between 2 and 10");
            scanner.nextLine();
            return -1;
        }
    }
    /**
     * This function is used to validate the user input for the context (we can only have 2-10 players).
     * @return the full context verified user player count input value.
     */
    public int returnContextValidatedUserPlayerCountNumber() {
        int verifyingUserPlayerCount;
        do {
            verifyingUserPlayerCount = getUserPlayerCountNumber();
        }
        while (verifyingUserPlayerCount < 2 || verifyingUserPlayerCount > 10 || verifyingUserPlayerCount == -1);
        return verifyingUserPlayerCount;
    }
    /**
     * Gets the size of the board desired from the user. Makes sure it is an integer before being passed
     * to the context verifying function.
     * @return Unverified Context integer for the size of the board taken from the user.
     */
    public int getUserDesiredBoardSize() {
        System.out.println("Please enter the desired board size integer between 10-50: ");
        try{
            userDesiredBoardSize = scanner.nextInt();
            return userDesiredBoardSize;
        }catch(InputMismatchException IME){
            System.out.println("That is not an integer!");
            scanner.nextLine();
            return -1;
        }catch(NoSuchElementException NSEE){
            System.out.println("Please enter an integer between 10-50");
            scanner.nextLine();
            return -1;
        }
    }
    /**
     * Gets the full context verified size for the board.
     * @return The size of the board that's full context verified.
     */
    public int returncontextValidatedUserDesiredBoardSize() {
        int verifyingUserDesiredBoardSize;
        do {
            verifyingUserDesiredBoardSize = getUserDesiredBoardSize();
        }
        while (verifyingUserDesiredBoardSize < 10 || verifyingUserDesiredBoardSize > 50 || verifyingUserDesiredBoardSize == -1);
        return verifyingUserDesiredBoardSize;
    }
    /**
     * Gets the choice from the player whether they want to buy the property or not.
     * @return True for yes the player wants to buy the property and False if the Player doesn't want to buy the property.
     */
    public boolean doesPlayerWantToBuyProperty() {
        System.out.println("Would you like to buy this property? (Yes/No)");
        String choice = scanner.nextLine();
        choice = choice.toLowerCase();
        boolean validChoice = false;
        do{
            if (choice.equals("yes")) {
                validChoice = true;
                return true;
            } else if (choice.equals("no")) {
                validChoice = true;
                return false;
            } else {
                scanner.nextLine();
                System.out.println("Please enter yes or no");
                choice = scanner.nextLine();
                return false;
            }
        }while(!validChoice);
    }
}
