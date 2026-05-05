package com.cm6123.monopoly.game;

import com.cm6123.monopoly.app.UserInputManager;

import java.util.HashMap;

public class GameManager {
    /**
     * Has a hashmap of the player objects, the key in the hashmap will be equal to the playerNumber.
     */
    private HashMap<Integer, Player> players = new HashMap<>();
    /**
     * Holds the current player's object.
     */
    private Player playerTurn;
    /**
     * Gets the total of the both dice values for the current player's turn.
     */
    private int currentDiceTotal;
    /**
     * Holds the number of turns which the games play until it stops.
     */
    private final int numOfTurnsToEnd = 10;
    /**
     * Adds player objects to the Hashmap and returns it.
     * @param wantedNumOfPlayers Gives in the wanted number of players.
     * @return The Hashmap with the list of players.
     */
    public HashMap<Integer, Player> createPlayers(final int wantedNumOfPlayers) {
        for (int i = 0; i < wantedNumOfPlayers; i++) {
            players.put(i+1, new Player(i+1));
        }
        return players;
    }
    /**
     * This is a setter method for the tests. Create players will be used when the actual game runs.
     * @param playershash A hashmap of testing players.
     */
    public void setPlayers(final HashMap<Integer, Player> playershash) {
        this.players = playershash;
    }
    /**
     * Gives access to the hashmap containing players.
     * @return The hashmap containing the player objects.
     */
    public HashMap<Integer, Player> getPlayers() {
        return players;
    }
    /**
     * The method for cycling through the players for their turn.
     * @param gameTurns gives in the number of turns the game has to run for.
     * @param gameBoard Passes in the game board object.
     * @param boardMap Passes in the game bourd.
     */
    public void playerTurnSystem(final int gameTurns,final Board gameBoard,final HashMap<Integer, SpaceSquare> boardMap) {
        int count = 1;
        System.out.println("=============ROUND 1=============");
        while(count <= gameTurns){
            for(Player currentPlayer : players.values()) {
                System.out.println("The current player is Player " + currentPlayer.getPlayerNumber());
                if(!currentPlayer.isBankrupt()) {
                    if(currentPlayer.isOnACycleSquare()){
                        processesForPlayerOnACycleSquare(gameBoard, currentPlayer);
                    }else {
                        usualPlayerTurnProcesses(gameBoard, boardMap, currentPlayer);
                    }
                }
            }
            count++;
            System.out.println("=============ROUND "+count+"=============");
        }
    }

    private static void processesForPlayerOnACycleSquare(final Board gameBoard,final Player currentPlayer) {
        int cycleNum = currentPlayer.rollDiceForCycleRace();
        if(cycleNum>3){
            System.out.println("You got "+cycleNum+" and so will not be out of the cycle square.");
        }else{
            System.out.println("You rolled "+cycleNum+" and so are out of the cycle square!");
            gameBoard.setNewPlayerPosition(currentPlayer, cycleNum);
        }
    }

    private void usualPlayerTurnProcesses(final Board gameBoard,final HashMap<Integer, SpaceSquare> boardMap,final Player currentPlayer) {
        currentDiceTotal = currentPlayer.rollDice();
        gameBoard.setNewPlayerPosition(currentPlayer, currentDiceTotal);
        gameBoard.landedOn(boardMap, currentPlayer);
    }

    /**
     * Takes in the hashmap of the player objects and goes through each object to find the highest
     * balane and displayes the winner.
     * @param hashMapOfPlayers Takes in the Hashmap of player objects.
     * @return The object of the player who won.
     */
    public Player determineWinner(final HashMap<Integer, Player> hashMapOfPlayers) {
        Player winner = null;

        for(Player player : hashMapOfPlayers.values()) {
            if(winner == null || player.getPlayerBalance() > winner.getPlayerBalance()) {
                winner = player;
            }
        }
        System.out.println("The winner is Player " + winner.getPlayerNumber()+" with $" + winner.getPlayerBalance());
        return winner;
    }
    /**
     * Creates the board object.
     * @param sizeOfBoard Passes in the userDesired size of the board.
     */
    public void boardCreation(final int sizeOfBoard){
        Board board = new Board(sizeOfBoard);
    }
    /**
     * The main method.
     * @param args
     */
    public static void main(final String[] args) {
        GameManager gameManager = new GameManager();
        UserInputManager userInputManager = new UserInputManager();
        Board board = new Board(userInputManager.returncontextValidatedUserDesiredBoardSize());
        HashMap<Integer, SpaceSquare> boardMap = board.initiateBoard();
        boardMap = board.addPropertiesTOBoard(boardMap,board.calculateTheAmountOfProperties());
        boardMap = board.addCycleSquare(boardMap,board.calculateAmountOfCycleSquares());
        HashMap<Integer,Player> players = gameManager.createPlayers(userInputManager.returnContextValidatedUserPlayerCountNumber());
        gameManager.playerTurnSystem(gameManager.numOfTurnsToEnd, board, boardMap);
        gameManager.determineWinner(gameManager.players);
    }
}
