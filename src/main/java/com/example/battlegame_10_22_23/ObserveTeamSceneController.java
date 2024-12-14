package com.example.battlegame_10_22_23;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ObserveTeamSceneController {
    @FXML public ComboBox<String> teammateOptionsComboBox;
    @FXML public Label statsLabel, newStatsLabel, upgradeInfoLabel;
    @FXML public ImageView teammateImageView;
    @FXML public Button buyUpgradeButton, returnToBaseCampButton, hireMembersButton, useItemButton, infoButton;
    @FXML public VBox buyingActionsVBox;
    @FXML public ListView<String> itemsListView;

    public void initializeObservingTeamScene(PlayerInformation playerInformation, AllReferences allReferences){
        HelloApplication.removeUnnecessaryScenes(HelloApplication.TRAINING_SCENE);
        this.modifyTeamArea = new ModifyTeamArea(playerInformation, 1.1, 1.1);
        for(BattleEntities i : this.modifyTeamArea.getPlayerInformation().getTeamMembers()){this.teammateOptionsComboBox.getItems().add(i.getName());}
        this.MAX_LIMIT = 10;
        this.allReferences = allReferences;
        this.informationArea = new InformationArea();
    }

    private void updateArea(){
        this.buyUpgradeButton.setDisable(false);
        this.infoButton.setDisable(false);
        this.useItemButton.setDisable(false);
        this.buyingActionsVBox.getChildren().clear();
        this.itemsListView.getItems().clear();
        for(Items i : this.modifyTeamArea.getPlayerInformation().getItemsOwned()){if(i.getItemReference().doesTargetSelf()){this.itemsListView.getItems().add(i.getItemReference().getName() + " (" + i.getQuantity() + ")");}}
        if(this.itemsListView.getItems().isEmpty()){
            this.itemsListView.getItems().add("No Items To Use");
            this.useItemButton.setDisable(true);
        }else{this.itemsListView.getSelectionModel().selectFirst();}
        this.itemsListView.getSelectionModel().selectFirst();
        this.selectedEntity = this.modifyTeamArea.getPlayerInformation().getTeamMembers().get(this.teammateOptionsComboBox.getSelectionModel().getSelectedIndex());
        try{this.teammateImageView.setImage(new Image(new FileInputStream(this.selectedEntity.getPictureLink())));}
        catch(FileNotFoundException error){error.printStackTrace();}
        this.statsLabel.setText("Attack: " + this.selectedEntity.getBaseAttack() +" \nMax Health: " + this.selectedEntity.getBaseHealth() + "\nCurrent Health : " + this.selectedEntity.getCurrentHealth() + "\nMoney Owned: $" + this.modifyTeamArea.getPlayerInformation().getMoneyOwned());
        this.buyUpgradeButton.setDisable(this.selectedEntity.getTrainingLevel() >= this.MAX_LIMIT || this.modifyTeamArea.getPlayerInformation().getMoneyOwned() < this.modifyTeamArea.getUpgradeCost(this.selectedEntity));
        if(this.selectedEntity.getTrainingLevel() >= this.MAX_LIMIT){this.upgradeInfoLabel.setText(this.selectedEntity.getName() + " is at level " + this.MAX_LIMIT + ". You can't upgrade this further.");}
        else{
            this.upgradeInfoLabel.setText(((this.modifyTeamArea.getPlayerInformation().getMoneyOwned() < this.modifyTeamArea.getUpgradeCost(this.selectedEntity)) ? "You Don't Have Enough Money" : "") + "Upgrade " + this.selectedEntity.getName() + " to level " + (this.selectedEntity.getTrainingLevel() + 1) + " for $" + this.modifyTeamArea.getUpgradeCost(this.selectedEntity));
            this.newStatsLabel.setText("Attack: " + this.selectedEntity.getBaseAttack() + " -> " + ((int)(this.selectedEntity.getBaseAttack() * this.modifyTeamArea.getAttackMultiplier())) + "\nHealth: " + this.selectedEntity.getBaseHealth() + " -> " + ((int)(this.selectedEntity.getBaseHealth() * this.modifyTeamArea.getHealthMultiplier())));
        }
        ArrayList<ActionsReference> allValidActions = new ArrayList<>();
        ArrayList<ActionsReference> allReferences = this.allReferences.getALL_POSSIBLE_ACTIONS().getAllActionReferences();
        for(ActionsReference i : allReferences){
            byte interval = 0;
            while(interval < selectedEntity.getBattleActions().size() && (!selectedEntity.getBattleActions().get(interval).getActionsReference().equals(i))){interval++;}
            if(interval == selectedEntity.getBattleActions().size()){allValidActions.add(i);}
        }
        if(allValidActions.isEmpty()){this.buyingActionsVBox.getChildren().add(new Label("No Actions To Buy"));}
        else{
            byte interval = 0;
            int cost = (allReferences.size() - allValidActions.size()) * 200;
            this.buyingActionsVBox.getChildren().add(new Label("These Actions Cost $" + cost + ". Select One To Buy It." + ((this.modifyTeamArea.getPlayerInformation().getMoneyOwned() < cost) ? "You Don't Have Enough Money For These Actions" : "")));
            while(interval < allValidActions.size()){
                HBox actionsHBox = new HBox();
                while(interval < allValidActions.size() && actionsHBox.getChildren().size() < 2){
                    ActionsReference actionSelected = allValidActions.get(interval);
                    Button actionButton = new Button(actionSelected.getName());
                    actionButton.setDisable(this.modifyTeamArea.getPlayerInformation().getMoneyOwned() < cost);
                    actionButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) ->{
                        this.modifyTeamArea.buyActions(selectedEntity, actionSelected, cost);
                        this.updateArea();
                    });
                    interval++;
                    actionsHBox.setSpacing(10);
                    actionsHBox.getChildren().add(actionButton);
                }
                this.buyingActionsVBox.getChildren().add(actionsHBox);
            }
        }
        (new SavingAndReading()).saveData(this.modifyTeamArea.getPlayerInformation(), this.allReferences);
    }
    @FXML public void handleChangeEntitySelected(){
        this.updateArea();
        this.informationArea.getInfoStageController().updateInfoScene(this.selectedEntity);
    }
    @FXML public void handleUpgradeStats(){
        this.modifyTeamArea.increaseTeammateStats(this.selectedEntity);
        this.updateArea();
    }
    @FXML public void handleReturnToBaseCamp(){
        this.informationArea.getInfoStage().close();
        HelloApplication.setStage(HelloApplication.BASE_CAMP_SCENE);
        ((BaseCampSceneController)(HelloApplication.BASE_CAMP_SCENE.getController())).initializeBaseCampScene(this.modifyTeamArea.getPlayerInformation(), this.allReferences);
    }
    @FXML public void handleHireMoreMembers(){
        this.informationArea.getInfoStage().close();
        HelloApplication.setStage(HelloApplication.HIRE_PLAYERS_SCENE);
        HelloApplication.removeUnnecessaryScenes(HelloApplication.HIRE_PLAYERS_SCENE);
        ((HirePlayerSceneController)(HelloApplication.HIRE_PLAYERS_SCENE.getController())).initializeHirePlayersScene(this.modifyTeamArea.getPlayerInformation(), this.allReferences);
    }
    @FXML public void handleUseItem(){
        ArrayList<Items> validItems = new ArrayList<>();
        byte amountOfLoops = 0;
        while(amountOfLoops < this.modifyTeamArea.getPlayerInformation().getItemsOwned().size()){
            if(this.modifyTeamArea.getPlayerInformation().getItemsOwned().get(amountOfLoops).getItemReference().doesTargetSelf()){validItems.add(this.modifyTeamArea.getPlayerInformation().getItemsOwned().get(amountOfLoops));}
            amountOfLoops++;
        }
        Items itemSelected = validItems.get(this.itemsListView.getSelectionModel().getSelectedIndex());
        itemSelected.changeQuantity(-1);
        this.modifyTeamArea.getPlayerInformation().removeEmptyItems();
        if(itemSelected.getItemReference().getEffectsAssociated() != null){for(EffectsReference i : itemSelected.getItemReference().getEffectsAssociated()){if(!i.doesTargetOpponent()){this.selectedEntity.getEffectsOnEntity().add(new Effects(i));}}}
        if(itemSelected.getItemReference().getImmediateHealthChangesAssociated() != null){for(ImmediateHealthChanges i : itemSelected.getItemReference().getImmediateHealthChangesAssociated()){if(!i.doesTargetOpponent()){i.setChangeInHealth(this.selectedEntity, this.selectedEntity);}}}
        this.updateArea();
    }

    @FXML public void handleOpenInformation(){
        this.informationArea.getInfoStage().show();
        this.informationArea.getInfoStageController().updateInfoScene(this.selectedEntity);
    }

    private int MAX_LIMIT;
    private ModifyTeamArea modifyTeamArea;
    private BattleEntities selectedEntity;
    private AllReferences allReferences;
    private InformationArea informationArea;
}
