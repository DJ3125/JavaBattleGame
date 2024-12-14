package com.example.battlegame_10_22_23;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TeammateInfoController {
    @FXML public Label titleLabel, statsLabel, immunitiesLabel;
    @FXML public ImageView entityImageView;
    @FXML public VBox actionsVBox, effectsVBox;

    public void updateInfoScene(BattleEntities selectedEntity){
        this.actionsVBox.setAlignment(Pos.CENTER);
        this.actionsVBox.setSpacing(10);
        this.effectsVBox.setAlignment(Pos.CENTER);
        this.effectsVBox.setSpacing(10);
        this.actionsVBox.getChildren().clear();
        this.effectsVBox.getChildren().clear();
        this.titleLabel.setText(selectedEntity.getName());
        try{this.entityImageView.setImage(new Image(new FileInputStream(selectedEntity.getPictureLink())));}
        catch(FileNotFoundException error){error.printStackTrace();}
        this.statsLabel.setText("Attack: " + selectedEntity.getBaseAttack() +" \nMax Health: " + selectedEntity.getBaseHealth() + "\nCurrent Health : " + selectedEntity.getCurrentHealth());
        String txt = "";
        if(selectedEntity.getImmunities() != null){for(Class<? extends EffectsReference> i: selectedEntity.getImmunities()){txt = txt.concat(i.toString().substring(i.toString().lastIndexOf('.') + 1) + ", ");}}
        else{txt = "There Are No Immunities";}
        this.immunitiesLabel.setText(txt);
        for(Actions i : selectedEntity.getBattleActions()){
            VBox action = new VBox(new Label(i.getActionsReference().getName()));
            ((action.getChildren().get(0))).setStyle("-fx-font-size: 10;");
            action.getChildren().addAll(new Label("Health Changes:"), new Label(i.getActionsReference().getHealthChangeDescription()));
            action.getChildren().addAll(new Label("Effects:"), new Label(i.getActionsReference().getEffectsDescription()));
            for(Node j : action.getChildren()){
                if(j.getClass() == Label.class){
                    ((Label)(j)).setWrapText(true);
                    ((Label)(j)).setAlignment(Pos.CENTER);
                }
            }
            actionsVBox.getChildren().add(action);
        }
        if(selectedEntity.getEffectsOnEntity() != null){for (Effects i : selectedEntity.getEffectsOnEntity()){this.effectsVBox.getChildren().add(new Label(i.getEffectsReference().getDescription()));}}
        for(Node i : this.effectsVBox.getChildren()){if(i.getClass() == Label.class){((Label)(i)).setWrapText(true);}}
    }
}
