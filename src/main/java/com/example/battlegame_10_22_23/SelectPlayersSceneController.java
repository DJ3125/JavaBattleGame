package com.example.battlegame_10_22_23;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SelectPlayersSceneController {
    //FXML elements
    @FXML public Button backButton, battleButton, openInformationButton;
    @FXML public HBox playersHBox;

    public void initializeSelectionScene(PlayerInformation playerInformation, AllReferences allReferences){
        HelloApplication.removeUnnecessaryScenes(HelloApplication.SELECT_PLAYERS_SCENE);
        this.battleButton.setText("Battle! (0/" + this.MAX_PLAYERS + ")");
        this.selectedEntities = new ArrayList<>();
        this.allPlayerOptions = playerInformation.getTeamMembers();
        this.playerInformation = playerInformation;
        this.allReferences = allReferences;
        this.informationArea = new InformationArea();
        this.playersHBox.setAlignment(Pos.CENTER);
        this.playersHBox.setSpacing(30);
        this.updateScene();
    }

    public void updateScene(){
        this.playersHBox.getChildren().clear();
        this.openInformationButton.setDisable(this.selectedEntities.isEmpty());
        for(BattleEntities i : this.selectedEntities){//Creates a new VBox for every entity that is selected. If the amount of entities selected doesn't exceed the max capacity, then the player can add more
            VBox icons = new VBox(new Label(i.getName()));
            icons.setAlignment(Pos.CENTER);
            icons.addEventHandler(MouseEvent.MOUSE_ENTERED, (mouseEvent)-> this.informationArea.getInfoStageController().updateInfoScene(i));
            try{
                ImageView iconImage = new ImageView(new Image(new FileInputStream(i.getPictureLink())));
                iconImage.setPreserveRatio(true);
                iconImage.setFitHeight(90);
                icons.getChildren().add(iconImage);
            }catch(FileNotFoundException error){error.printStackTrace();}
            Button removeButton = new Button("Remove Entity");
            removeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent)->{ //When the remove button is clicked, then it loops through the entitiesSelected ArrayList, and removes the selected entity. Then it updates again
                byte interval = 0;
                boolean found = false;
                while(interval < this.selectedEntities.size() && !found){
                    if(this.selectedEntities.get(interval).equals(i)){
                        this.selectedEntities.remove(interval);
                        found = true;
                    }
                    interval++;
                }
                this.updateScene();
            });
            icons.getChildren().addAll(new Label("Attack: " + i.getBaseAttack()), new Label("Health: " + i.getCurrentHealth()), new Label("Current Health: " + i.getCurrentHealth()), removeButton);
            this.playersHBox.getChildren().add(icons);
        }
        if(this.selectedEntities.size() < this.MAX_PLAYERS){ //If the amount of entities selected doesn't exceed the max amount, there will be an option to add more entities
            VBox addMoreIconsVBox = new VBox(new Label("Add Another Entity"));
            addMoreIconsVBox.setAlignment(Pos.CENTER);
            addMoreIconsVBox.setSpacing(10);
            ChoiceBox<String> newEntityToAddChoiceBox = new ChoiceBox<>();
            ArrayList<BattleEntities> possibleOptions = new ArrayList<>();
            for(BattleEntities i : this.allPlayerOptions){
                boolean selected = false;
                byte interval = 0;
                while(interval < this.selectedEntities.size() && !selected){
                    selected = this.selectedEntities.get(interval).equals(i);
                    interval++;
                }
                if(!selected && i.getCurrentHealth() > 0){
                    newEntityToAddChoiceBox.getItems().add(i.getName());
                    possibleOptions.add(i);
                }
            }
            newEntityToAddChoiceBox.addEventHandler(ActionEvent.ACTION, (mouseEvent) ->{
                this.selectedEntities.add(possibleOptions.get(newEntityToAddChoiceBox.getSelectionModel().getSelectedIndex()));
                this.updateScene();
            });
            addMoreIconsVBox.getChildren().add(newEntityToAddChoiceBox);
            this.playersHBox.getChildren().add(addMoreIconsVBox);
        }
        this.battleButton.setDisable(this.selectedEntities.isEmpty());
        this.battleButton.setText("Battle! (" + this.selectedEntities.size() + "/" + this.MAX_PLAYERS + ")");
    }

    @FXML public void handleBattle(){
        HelloApplication.setStage(HelloApplication.MAP_SCENE);
        ((MapSceneController)(HelloApplication.MAP_SCENE.getController())).initializeMapScene(this.selectedEntities, this.playerInformation, this.allReferences);
        HelloApplication.removeUnnecessaryScenes(HelloApplication.MAP_SCENE);
    }

    @FXML public void handleReturnToBaseCamp(){
        HelloApplication.setStage(HelloApplication.BASE_CAMP_SCENE);
        ((BaseCampSceneController)(HelloApplication.BASE_CAMP_SCENE.getController())).initializeBaseCampScene(this.playerInformation, this.allReferences);
    }

    @FXML public void handleOpenInfo(){
        this.informationArea.getInfoStage().show();
        this.informationArea.getInfoStageController().updateInfoScene(selectedEntities.get(0));
    }

    private ArrayList<BattleEntities> allPlayerOptions, selectedEntities;
    private PlayerInformation playerInformation;
    private InformationArea informationArea;
    private AllReferences allReferences;
    private final int MAX_PLAYERS = 3;
}
