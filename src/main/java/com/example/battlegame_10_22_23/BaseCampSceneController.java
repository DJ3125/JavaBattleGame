package com.example.battlegame_10_22_23;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class BaseCampSceneController {
    @FXML public Button shopButton, battleButton, observeTeammatesButton;

    public void initializeBaseCampScene(PlayerInformation playerInformation, AllReferences allReferences){
        HelloApplication.removeUnnecessaryScenes(HelloApplication.BASE_CAMP_SCENE);
        this.playerInformation = playerInformation;
        this.allReferences = allReferences;
        (new SavingAndReading()).saveData(playerInformation, allReferences);
    }
    @FXML public void handleGoToShop(){
        HelloApplication.setStage(HelloApplication.SHOP_SCENE);
        ((ShopSceneController)(HelloApplication.SHOP_SCENE.getController())).initializeShopScene(this.playerInformation, this.allReferences);
    }
    @FXML public void handleBattleDinosaurs(){
        HelloApplication.setStage(HelloApplication.SELECT_PLAYERS_SCENE);
        ((SelectPlayersSceneController)(HelloApplication.SELECT_PLAYERS_SCENE.getController())).initializeSelectionScene(this.playerInformation, this.allReferences);
    }
    @FXML public void handleObserveTeam(){
        HelloApplication.setStage(HelloApplication.TRAINING_SCENE);
        ((ObserveTeamSceneController)(HelloApplication.TRAINING_SCENE.getController())).initializeObservingTeamScene(this.playerInformation, this.allReferences);
    }
    private PlayerInformation playerInformation;
    private AllReferences allReferences;
}
