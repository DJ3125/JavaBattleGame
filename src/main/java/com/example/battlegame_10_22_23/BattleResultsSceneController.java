package com.example.battlegame_10_22_23;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class BattleResultsSceneController {
    @FXML public Label resultsLabel, infoLabel;
    @FXML public Button returnButton;

    /*
    Parameters: Requires an ArrayList of Items to modify
    Return: Has no return statement
    Purpose: this is called when the player loses. They lose items when they lose the battle
     */
    public void initializeBattleResultsSceneWithItems(PlayerInformation playerInformation, AllReferences allReferences){//Called if the player loses
        HelloApplication.removeUnnecessaryScenes(HelloApplication.BATTLE_RESULTS_SCENE);
        this.playerInformation = playerInformation;
        this.resultsLabel.setText("You Lost!");
        if(!playerInformation.getItemsOwned().isEmpty()){
            String txt = "You Lost: \n";
            for (Items items : this.playerInformation.getItemsOwned()) {//Removes a random amount of each item if the player loses
                int amountLost = (int) (1 + (items.getQuantity() - 1) * Math.random());
                items.changeQuantity(-amountLost);
                txt = txt.concat(amountLost + " " + items.getItemReference().getName() + "s \n");
            }
            txt += "You Now Have: \n"; //Displays how many of each item is remaining
            for(Items i : this.playerInformation.getItemsOwned()){txt = txt.concat(i.getQuantity() + " " + i.getItemReference().getName());}
            this.infoLabel.setText(txt);
            this.infoLabel.setWrapText(true);
            this.playerInformation.removeEmptyItems();
        }else{this.infoLabel.setText("However, you did not have any items on you, so you do not lose anything.");}
        if(!this.checkForValidTeammates()){
            BattleEntities selectedEntity = this.playerInformation.getTeamMembers().get((int)(Math.random() * this.playerInformation.getTeamMembers().size()));
            selectedEntity.setCurrentHealth(selectedEntity.getBaseHealth());
            this.infoLabel.setText(this.infoLabel.getText() + "\nSince All Of Your Employees Have No Health, A Random Revive is being supplied to you on the house." + selectedEntity.getName() + " has been revived.");
        }
        this.allReferences = allReferences;
        (new SavingAndReading()).saveData(playerInformation, allReferences);
    }

    private boolean checkForValidTeammates(){
        for(BattleEntities i : this.playerInformation.getTeamMembers()){if(i.getCurrentHealth() > 0){return true;}}
        return false;
    }

    /*
    Parameters: Requires an integer describing how much money was won
    Return: Has no return value
    Purpose: is called when the user wins the battle, and as a result wins money.
     */
    public void initializeBattleResultsSceneWithMoney(PlayerInformation playerInformation, int moneyGained, AllReferences allReferences){
        HelloApplication.removeUnnecessaryScenes(HelloApplication.BATTLE_RESULTS_SCENE);
        this.resultsLabel.setText("You Won!");
        this.infoLabel.setText("You gained $" + moneyGained);
        this.playerInformation = playerInformation;
        playerInformation.increaseMoneyOwned(moneyGained);
        this.allReferences = allReferences;
        (new SavingAndReading()).saveData(playerInformation, this.allReferences);
    }

    /*
    Parameters: requires no explicit parameter
    Return: Has no return value
    Purpose: is called when there is a stalemate
     */
    public void initializeBattleResultsScene(PlayerInformation playerInformation, AllReferences allReferences){
        HelloApplication.removeUnnecessaryScenes(HelloApplication.BATTLE_RESULTS_SCENE);
        this.resultsLabel.setText("It was a stalemate. You do not gain or lose any items or money.");
        if(!this.checkForValidTeammates()){
            BattleEntities selectedEntity = this.playerInformation.getTeamMembers().get((int)(Math.random() * this.playerInformation.getTeamMembers().size()));
            selectedEntity.setCurrentHealth(selectedEntity.getBaseHealth());
            this.infoLabel.setText(this.infoLabel.getText() + "\nSince All Of Your Employees Have No Health, A Random Revive is being supplied to you on the house." + selectedEntity.getName() + " has been revived.");
        }
        infoLabel.setVisible(false);
        this.allReferences = allReferences;
        this.playerInformation = playerInformation;
    }

    @FXML public void handleReturnToBaseCamp(){
        HelloApplication.setStage(HelloApplication.BASE_CAMP_SCENE);
        ((BaseCampSceneController)(HelloApplication.BASE_CAMP_SCENE.getController())).initializeBaseCampScene(this.playerInformation, this.allReferences);
    }

    private PlayerInformation playerInformation;
    private AllReferences allReferences;
}
