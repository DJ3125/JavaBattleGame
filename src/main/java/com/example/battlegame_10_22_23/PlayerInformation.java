package com.example.battlegame_10_22_23;

import java.util.ArrayList;

public class PlayerInformation {
    //set methods. Pretty self-explanatory
    public void setMoneyOwned(double newBalance){this.moneyOwned = newBalance;}
    public void increaseMoneyOwned(double amountIncrease){
        this.moneyOwned += amountIncrease;
        this.moneyOwned = ((int)(this.moneyOwned * 100))/100.0;
    }
    public void decreaseMoneyOwned(double amountDecrease){
        this.moneyOwned -= amountDecrease;
        this.moneyOwned = ((int)(this.moneyOwned * 100))/100.0;
    }

    /*
    Parameters: Requires an ItemReference and an integer describing what is being bought and how much of it is being bought
    Return: Returns a true or false value describing if the transaction was successful or not
    Purpose: Adds a certain amount of items to the inventory and subtracts the corresponding amount of money if the player has enough. If the player doesn't have enough then the transaction doesn't occur
     */
    public boolean buyItems(ItemReference itemToBuy, int quantityToBuy){
        for(Items i : this.itemsOwned){
            if(i.getItemReference().equals(itemToBuy)){
                if(i.getItemReference().getPrice() * quantityToBuy > this.moneyOwned){return false;}
                this.decreaseMoneyOwned(i.changeQuantity(quantityToBuy));
                return true;
            }
        }
        if(itemToBuy.getPrice() * quantityToBuy > this.moneyOwned){return false;}
        this.moneyOwned -= itemToBuy.getPrice() * quantityToBuy;
        this.moneyOwned = ((int)(this.moneyOwned * 100))/100.0;
        this.itemsOwned.add(new Items(itemToBuy, quantityToBuy));
        return true;
    }

    /*
    Parameters: requires an ItemReference and an integer describing what is being sold and how much to sell
    Return: Returns a boolean value describing if the transaction was successful or not
    Purpose: Removes a certain amount of items from the inventory if present and adds to corresponding amount of money associated with that item. If there isn't enough items to sell, then the transaction doesn't occur
     */
    public boolean sellItems(ItemReference itemToSell, int quantityToSell){
        for(Items i : this.itemsOwned){
            if(i.getItemReference().equals(itemToSell)){
                if(i.getQuantity() > quantityToSell){return false;}
                this.increaseMoneyOwned(i.changeQuantity(-quantityToSell));
                this.removeEmptyItems();
                return true;
            }
        }
        return false;
    }

    /*
    Parameters: Requires no explicit parameters
    Return: has no return value
    Purpose: Removes all Items that have a quantity of 0
     */
    public void removeEmptyItems(){
        byte interval = 0;
        while (interval < this.itemsOwned.size()){
            if(this.itemsOwned.get(interval).getQuantity() <= 0){this.itemsOwned.remove(interval);}
            else{interval++;}
        }
    }

    //Get methods
    public ArrayList<Items> getItemsOwned(){return this.itemsOwned;}
    public double getMoneyOwned(){return this.moneyOwned;}
    public ArrayList<BattleEntities> getTeamMembers(){return this.teamMembers;}

    //Constructors
    public PlayerInformation(double initialBalance, BattleEntities selectedStartingEntity){
        this.moneyOwned = initialBalance;
        this.itemsOwned = new ArrayList<>();
        this.teamMembers = new ArrayList<>();
        this.teamMembers.add(selectedStartingEntity);
    }
    public PlayerInformation(){
        this.moneyOwned = 0;
        this.itemsOwned = new ArrayList<>();
        this.teamMembers = new ArrayList<>();
    }

    //Private attributes
    private double moneyOwned;
    private ArrayList<Items> itemsOwned;
    private ArrayList<BattleEntities> teamMembers;
}