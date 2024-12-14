package com.example.battlegame_10_22_23;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class HirePlayerSceneController {
    @FXML public Label moneyRemainingLabel;
    @FXML public VBox availableTeammatesVBox;
    @FXML public Button returnToBaseCampButton, informationButton;

    public void initializeHirePlayersScene(PlayerInformation playerInformation, AllReferences allReferences){
        HelloApplication.removeUnnecessaryScenes(HelloApplication.HIRE_PLAYERS_SCENE);
        this.playerInformation = playerInformation;
        this.returnToBaseCampButton.setDisable(false);
        this.returnToBaseCampButton.setVisible(true);
        this.allReferences = allReferences;
        this.informationArea = new InformationArea();
        this.availableTeammatesVBox.setSpacing(20);
        this.availableTeammatesVBox.setStyle("-fx-border-style: solid;");
        this.availableTeammatesVBox.setStyle("-fx-border-width: 2;");
        this.availableTeammatesVBox.setStyle("-fx-border-color: black;");
        (new SavingAndReading()).saveData(playerInformation, this.allReferences);
        this.updateScene();
    }

    public void initializeHirePlayersScene(){
        HelloApplication.removeUnnecessaryScenes(HelloApplication.HIRE_PLAYERS_SCENE);
        this.playerInformation = null;
        this.allReferences = new AllReferences();
        this.informationArea = new InformationArea();
        this.availableTeammatesVBox.setSpacing(20);
        this.availableTeammatesVBox.setStyle("-fx-border-style: solid;");
        this.availableTeammatesVBox.setStyle("-fx-border-width: 2;");
        this.availableTeammatesVBox.setStyle("-fx-border-color: black;");
        this.updateScene();
    }

    private ArrayList<BattleEntities> getValidEntries(){
        ArrayList<BattleEntities> validEntities = new ArrayList<>();
        for(BattleEntities i : this.allReferences.getALL_POSSIBLE_TEAMMATES().getAllPossibleEmployees()){
            if(this.playerInformation == null){validEntities.add(i);}
            else{
                byte interval = 0;
                while(interval < this.playerInformation.getTeamMembers().size() && !this.playerInformation.getTeamMembers().get(interval).getName().equals(i.getName())){interval++;}
                if(interval == this.playerInformation.getTeamMembers().size()){validEntities.add(i);}
            }
        }
        return validEntities;
    }

    private void updateScene(){
        if(this.playerInformation != null){this.moneyRemainingLabel.setText("You Have $" + playerInformation.getMoneyOwned() + " remaining.");}
        this.availableTeammatesVBox.getChildren().clear();
        ArrayList<BattleEntities> validEntities = this.getValidEntries();
        this.informationButton.setDisable(validEntities.isEmpty());
        byte interval = 0;
        while(interval < validEntities.size()){
            HBox teammatesHBox = new HBox();
            teammatesHBox.setSpacing(10);
            teammatesHBox.setAlignment(Pos.CENTER);
            while(interval < validEntities.size() && teammatesHBox.getChildren().size() < 4){
                BattleEntities selectedEntity = validEntities.get(interval);
                ImageView teammateImageView = null;
                try{
                    teammateImageView = new ImageView(new Image(new FileInputStream(selectedEntity.getPictureLink())));
                    teammateImageView.setPreserveRatio(true);
                    teammateImageView.setFitHeight(90);
                }catch(FileNotFoundException error){error.printStackTrace();}
                Button buyButton = new Button("Hire" + ((this.playerInformation != null) ? ". Costs $" + selectedEntity.getPower() * 2 : ""));
                buyButton.setDisable(this.playerInformation != null && 2 * selectedEntity.getPower() > this.playerInformation.getMoneyOwned());
                buyButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent)->{
                    if(this.playerInformation == null){
                        this.playerInformation = new PlayerInformation(1000, selectedEntity);
                        this.returnToBaseCamp();
                    }else{
                        this.playerInformation.getTeamMembers().add(selectedEntity);
                        this.playerInformation.decreaseMoneyOwned(selectedEntity.getPower() * 2);
                        this.updateScene();
                        (new SavingAndReading()).saveData(playerInformation, this.allReferences);
                    }
                });
                VBox playerVBox = new VBox(new Label(selectedEntity.getName()));
                playerVBox.setSpacing(10);
                playerVBox.setAlignment(Pos.CENTER);
                BattleEntities mouseOnEntity = validEntities.get(interval);
                playerVBox.addEventHandler(MouseEvent.MOUSE_ENTERED, (mouseEvent) -> this.informationArea.getInfoStageController().updateInfoScene(mouseOnEntity));
                playerVBox.getChildren().addAll(teammateImageView, buyButton);
                teammatesHBox.getChildren().add(playerVBox);
                interval++;
            }
            this.availableTeammatesVBox.getChildren().add(teammatesHBox);
        }
        if(validEntities.isEmpty()){
            this.availableTeammatesVBox.getChildren().add(new Label("There Are No More Teammates To Hire"));
        }
    }

    private void returnToBaseCamp(){
        this.informationArea.getInfoStage().close();
        HelloApplication.setStage(HelloApplication.BASE_CAMP_SCENE);
        ((BaseCampSceneController)(HelloApplication.BASE_CAMP_SCENE.getController())).initializeBaseCampScene(this.playerInformation, this.allReferences);
    }

    @FXML public void handleReturnToBaseCamp(){this.returnToBaseCamp();}

    @FXML public void handleOpenInformation(){
        this.informationArea.getInfoStage().show();
        this.informationArea.getInfoStageController().updateInfoScene(this.getValidEntries().get(0));
    }

    private PlayerInformation playerInformation;
    private AllReferences allReferences;
    private InformationArea informationArea;
}
