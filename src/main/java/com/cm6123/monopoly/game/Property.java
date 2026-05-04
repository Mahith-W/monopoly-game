package com.cm6123.monopoly.game;

public class Property extends SpaceSquare{
    /**
     * Holds the property name.
     */
    private final String propertyName;
    /**
     * Holds the purchase value of the property.
     */
    private final float propertyValue;
    /**
     * Holds the rental value for the property.
     */
    private final float rentalPrice;
    /**
     * Contains the player object of the player who owns the property.
     */
    private Player owner;
    /**
     * The constructor for the property class.
     *
     * @param positionOfTheSquare Gives in the position of the square on the board
     * @param valueOfProperty Passes in the value of the property. The buy price.
     * @param valueOfRentalPrice Passes in the rental price for other players who aren't the owner.
     * @param nameOfProperty Passes in the name of the property.
     */
    public Property(final int positionOfTheSquare, final Float valueOfProperty,final Float valueOfRentalPrice,final String nameOfProperty) {
        super(positionOfTheSquare, "Property");
        this.propertyValue = valueOfProperty;
        this.rentalPrice = valueOfRentalPrice;
        this.propertyName = nameOfProperty;
    }
    /**
     * Sets a player class as the property owner.
     * @param ownerPlayer
     */
    public void setOwner(final Player ownerPlayer) {
        this.owner = ownerPlayer;
    }
    /**
     * Returns the value of the current property object.
     * @return The Value of the property.
     */
    public float getValueOfProperty() {
        return this.propertyValue;
    }
    /**
     * Returns the rental price of this property object.
     * @return The value of the rental price.
     */
    public float getRentalPrice() {
        return this.rentalPrice;
    }
    /**
     * Returns the Player class object of the player who has bought the property.
     * @return The Player object of the player who has bought the property.
     */
    public Player returnOwner() {
        return this.owner;
    }
    /**
     * Carries out the transaction for the player buying this property object.
     * @param purchasingPlayer
     */
    public void playerBuysProperty(final Player purchasingPlayer) {
        if (this.owner == null){
            if (playerCanBuyProperty(purchasingPlayer.getPlayerBalance())) {
                float playerBalance = purchasingPlayer.getPlayerBalance();
                purchasingPlayer.subBalance(propertyValue);
                setOwner(purchasingPlayer);
                System.out.println("Player "+purchasingPlayer.getPlayerNumber()+" has bought " +propertyName);
                purchasingPlayer.addOwnedProperty(this);
            }else{
                System.out.println("Player "+purchasingPlayer.getPlayerNumber()+" don't have enough balance");
            }
        }else{
            System.out.println("Player "+owner.getPlayerNumber()+" owns this property");
        }
    }
    /**
     * Checks if the player has enough funds to buy the property.
     * @param playerBalance Passes in the player whose making the purchase's balance.
     * @return True if the player can buy and has sufficiant funds or False if they can't.
     */
    public boolean playerCanBuyProperty(final float playerBalance) {
        if (playerBalance>this.propertyValue) {
            return true;
        }
        return false;
    }
    /**
     * Returns the name of the property for this property object.
     * @return Returns the Property name for this property object.
     */
    public String getPropertyName() {
        return this.propertyName;
    }
    /**
     * Carries Out the rental Payment. Takes money away from the current player if they are not the owner and pays that money to the
     * owner.
     * @param currentPlayer Passes in the current player value.
     * @return True if the rental was successfully paid otherwise False which probably results in the player being marked
     * as bankrupt.
     */
    public boolean carryOutRental(final Player currentPlayer) {
        boolean paymentSuccessful = false;
        if(this.rentalPrice < currentPlayer.getPlayerBalance()){
            return playerPaidWithJustBalance(currentPlayer);
        } else if (this.rentalPrice > currentPlayer.getPlayerBalance() && !currentPlayer.getOwnedProperties().isEmpty()) {
            while(!currentPlayer.getOwnedProperties().isEmpty() && this.rentalPrice>currentPlayer.getPlayerBalance()){
                currentPlayer.addBalance(currentPlayer.getLowestValueProperty().getValue().getValueOfProperty()*0.5F);
            }
            if(this.rentalPrice < currentPlayer.getPlayerBalance()){
                return playerPaidUsingProperties(currentPlayer);
            }
        } else if (this.rentalPrice > currentPlayer.getPlayerBalance() && currentPlayer.getPlayerBalance() > 0) {
            return playerPaidAllBalance(currentPlayer);
        }
        System.out.println("Player "+currentPlayer.getPlayerNumber()+" is bankrupt and is out of the game!");
        currentPlayer.setAsBankrupt();
        return paymentSuccessful;
    }

    private boolean playerPaidWithJustBalance(final Player currentPlayer) {
        boolean paymentSuccessful;
        currentPlayer.subBalance(this.rentalPrice);
        this.owner.addBalance(this.rentalPrice);
        paymentSuccessful = true;
        System.out.println("Player "+ currentPlayer.getPlayerNumber()+" has paid the rent!");
        return paymentSuccessful;
    }

    private boolean playerPaidAllBalance(final Player currentPlayer) {
        boolean paymentSuccessful;
        this.owner.addBalance(currentPlayer.getPlayerBalance());
        currentPlayer.subBalance(currentPlayer.getPlayerBalance());
        paymentSuccessful = true;
        System.out.println("Player "+ currentPlayer.getPlayerNumber()+" has paid the rent by paying everything they have!");
        return paymentSuccessful;
    }

    private boolean playerPaidUsingProperties(final Player currentPlayer) {
        boolean paymentSuccessful;
        currentPlayer.subBalance(this.rentalPrice);
        this.owner.addBalance(this.rentalPrice);
        paymentSuccessful = true;
        currentPlayer.getLowestValueProperty().getValue().unOwnershipProperty();
        currentPlayer.getOwnedProperties().remove(currentPlayer.getLowestValueProperty().getKey());
        System.out.println("Player "+ currentPlayer.getPlayerNumber()+" has paid the rent by selling their property(s)!");
        return paymentSuccessful;
    }
    private void unOwnershipProperty(){
        this.owner =  null;
    }
}
