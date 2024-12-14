package com.example.battlegame_10_22_23;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MapSceneController {
    @FXML public AnchorPane mapAnchorPane;
    @FXML public Button returnToBaseCampButton;

    public void initializeMapScene(ArrayList<BattleEntities> selectedTeam, PlayerInformation playerInformation, AllReferences allReferences){
        HelloApplication.removeUnnecessaryScenes(HelloApplication.MAP_SCENE);
        this.playerInformation = playerInformation;
        this.allReferences = allReferences;
        try{mapAnchorPane.setBackground(new Background(new BackgroundImage(new Image(new FileInputStream("src/main/resources/Pictures/Map.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));}
        catch(FileNotFoundException error){error.printStackTrace();}
        int totalPower = 0;
        for(BattleEntities j : selectedTeam){totalPower += j.getPower();}
        int amountOfDinos = this.allReferences.getALL_POSSIBLE_DINOSAURS().getAllDinos().size();
        Point2D[] dinosaurLocations = new Point2D[amountOfDinos];
        for(byte i = 0; i < amountOfDinos; i++){
            Point2D dinoLocation;
            boolean inCloseProximity;
            do{
                dinoLocation = new Point2D((int)(this.mapAnchorPane.getWidth()/3 + this.mapAnchorPane.getWidth()/4 * Math.cos(Math.random() * Math.PI)), (int)(this.mapAnchorPane.getHeight()/2 + this.mapAnchorPane.getHeight()/5*Math.cos(Math.random() * Math.PI)));
                inCloseProximity = false;
                for(Point2D j : dinosaurLocations){if(j != null && dinoLocation.distance(j) < 50){inCloseProximity = true;}}
            }while(inCloseProximity);
            dinosaurLocations[i] = dinoLocation;
            byte weightedSide = 3;
            int[] powerIntervals = new int[7];
            for(byte j = 0; j< powerIntervals.length; j++){powerIntervals[j] = 200 *(j + 1);}
            while(powerIntervals[weightedSide] <= totalPower){weightedSide--;}
            weightedSide++;
            int intervalSides = amountOfDinos;
            boolean sideFound = false;
            double chance = Math.random();
            double weightedSideProbability = 0.8;
            double unweightedSideProbability = (1 - weightedSideProbability)/(amountOfDinos - 1);
            while(!sideFound){
                sideFound = chance >= 1 - ((amountOfDinos - intervalSides) * unweightedSideProbability + ((intervalSides <= weightedSide) ? weightedSideProbability : 0) + ((intervalSides == weightedSide) ? 0 : unweightedSideProbability));
                intervalSides--;
            }
            BattleEntities dinoRandomlySelected = this.allReferences.getALL_POSSIBLE_DINOSAURS().getAllDinos().get(intervalSides);
            try{
                ImageView dinoImage = new ImageView(new Image(new FileInputStream(dinoRandomlySelected.getPictureLink())));
                dinoImage.setLayoutX(dinosaurLocations[i].getX());
                dinoImage.setLayoutY(dinosaurLocations[i].getY());
                dinoImage.setPreserveRatio(true);
                dinoImage.setFitHeight(50);
                byte amountOfDinosOnTeam = (byte)(Math.random() * 3 + 1);
                dinoImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent)->{
                    ArrayList<BattleEntities> dinosaurTeam = new ArrayList<>();
                    //doesn't equal the final boss
                    if(!dinoRandomlySelected.equals(this.allReferences.getALL_POSSIBLE_DINOSAURS().getINDOMINUS_REX())){for(byte j = 0; j < amountOfDinosOnTeam; j++){dinosaurTeam.add(dinoRandomlySelected.getClone());}}
                    else{dinosaurTeam.add(dinoRandomlySelected);}
                    HelloApplication.setStage(HelloApplication.BATTLE_SCENE);
                    ((BattleSceneController)(HelloApplication.BATTLE_SCENE.getController())).initializeBattleScene(selectedTeam, dinosaurTeam, this.playerInformation, this.allReferences);
                });
                byte index = i;
                dinoImage.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, (mouseEvent)->{
                    byte interval = 0;
                    while(interval < this.mapAnchorPane.getChildren().size()){
                        if(this.mapAnchorPane.getChildren().get(interval).getClass() == Label.class){this.mapAnchorPane.getChildren().remove(interval);}
                        else{interval++;}
                    }
                    Label infoLabel = new Label(dinoRandomlySelected.getName() + "\nAttack:" + dinoRandomlySelected.getBaseAttack() + "\nHealth:" + dinoRandomlySelected.getBaseHealth() + "\nQuantity: " + amountOfDinosOnTeam);
                    infoLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), new CornerRadii(0), new Insets(-10))));
                    infoLabel.setLayoutX(dinosaurLocations[index].getX() + 60);
                    infoLabel.setLayoutY(dinosaurLocations[index].getY());
                    this.mapAnchorPane.getChildren().add(infoLabel);
                });
                dinoImage.addEventHandler(MouseEvent.MOUSE_ENTERED, (mouseEvent)->{
                });
                this.mapAnchorPane.getChildren().add(dinoImage);
            }
            catch (FileNotFoundException error){error.printStackTrace();}
        }

    }

    @FXML public void handleGoBackToBaseCamp(){
        HelloApplication.setStage(HelloApplication.BASE_CAMP_SCENE);
        ((BaseCampSceneController)(HelloApplication.BASE_CAMP_SCENE.getController())).initializeBaseCampScene(this.playerInformation, this.allReferences);
    }

    private PlayerInformation playerInformation;
    private AllReferences allReferences;
}
