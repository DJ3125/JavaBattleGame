package com.example.battlegame_10_22_23;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InformationArea {

    public InformationArea(){
        try{
            this.infoStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teammate-info.fxml"));
            this.infoStage.setScene(new Scene((fxmlLoader.load())));
            this.infoStageController = fxmlLoader.getController();
            this.infoStage.hide();
        }catch(IOException error){error.printStackTrace();}
    }

    public Stage getInfoStage() {return this.infoStage;}
    public TeammateInfoController getInfoStageController() {return this.infoStageController;}

    private Stage infoStage;
    private TeammateInfoController infoStageController;
}
