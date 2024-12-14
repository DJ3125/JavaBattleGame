package com.example.battlegame_10_22_23;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    @Override public void start(Stage stage){
        GAME_STAGE = stage;
        GAME_STAGE.setResizable(false);
        GAME_STAGE.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, (windowCloseEvent) ->{
            if(HelloApplication.BATTLE_SCENE.getController() != null) {
                windowCloseEvent.consume();
                Stage abortStage = new Stage();
                VBox root = new VBox(new VBox(new Label("If You Close, it will count as a loss. Will You Continue?")));
                Button abortButton = new Button("Abort");
                Button continueButton = new Button("Do Not Abort");
                abortButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
                    try {
                        BattleArea battleArea = (BattleArea) (BattleSceneController.class.getMethod("getSceneInfo").invoke(HelloApplication.BATTLE_SCENE.getController()));
                        PlayerInformation playerInformation = (PlayerInformation) (BattleArea.class.getMethod("getPlayerInformation").invoke(battleArea));
                        for (Items items : playerInformation.getItemsOwned()) {
                            items.changeQuantity(-((int) (1 + (items.getQuantity() - 1) * Math.random())));
                        }
                        playerInformation.removeEmptyItems();
                        battleArea.clearAllEffects();
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException error) {error.printStackTrace();}
                    abortStage.close();
                    GAME_STAGE.close();
                });
                continueButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> abortStage.close());
                root.getChildren().addAll(abortButton, continueButton);
                Scene scene = new Scene(root);
                abortStage.setScene(scene);
                abortStage.setResizable(false);
                abortStage.setAlwaysOnTop(true);
                abortStage.setTitle("Abort Battle?");
                abortStage.show();
            }
        });
        if((new File("src/main/resources/SaveFiles/MoneyOwned.txt")).exists()){
            AllReferences allReferences = new AllReferences();
            HelloApplication.setStage(HelloApplication.BASE_CAMP_SCENE);
            try{
                ((BaseCampSceneController)(HelloApplication.BASE_CAMP_SCENE.getController())).initializeBaseCampScene((new SavingAndReading()).readData(allReferences), allReferences);
            }catch(FileNotFoundException error){error.printStackTrace();}
        }else{
            System.out.println("hi");
            HelloApplication.setStage(HelloApplication.HIRE_PLAYERS_SCENE);
            ((HirePlayerSceneController)(HelloApplication.HIRE_PLAYERS_SCENE.getController())).initializeHirePlayersScene();
        }
        GAME_STAGE.setTitle("Dylan's Dino Battle Bonanza!");
//        GAME_STAGE.getIcons().add(new Image(new FileInputStream()));
        GAME_STAGE.show();
    }

    public static void main(String[] args) {launch();}

    /*
    Parameters: requires an FXMLLoader to get the elements from to create a new scene and add it to the stage
    Return: Has no return value
    Purpose: Sets the scene
     */
    public static void setStage(FXMLLoader fxmlLoader){
        try{GAME_STAGE.setScene(new Scene(fxmlLoader.load()));}//Sets the scene to the specified scene
        catch(IOException error){error.printStackTrace();}
    }

    /*
    Parameters: Requires an FXMLLoader to specify which scene to not set to null
    Return: Has no return value
    Purpose: Removes all other scenes and controllers that are not used in an attempt to save memory
     */
    public static void removeUnnecessaryScenes(FXMLLoader fxmlLoader){
        for(FXMLLoader i : HelloApplication.getAllFXMLLoaders()){
            if(i != null && i != fxmlLoader){
                i.setRoot(null);
                i.setController(null);
            }
        }
    }

    private static ArrayList<FXMLLoader> getAllFXMLLoaders(){
        ArrayList<FXMLLoader> fxmlLoaders = new ArrayList<>();
        for(Field i : HelloApplication.class.getDeclaredFields()){
            if(i.getType() == FXMLLoader.class){
                try{fxmlLoaders.add((FXMLLoader)i.get(null));}
                catch(IllegalAccessException error){error.printStackTrace();}
            }
        }
        return fxmlLoaders;
    }


    static {//Initializes all the scenes
        BATTLE_SCENE = new FXMLLoader(HelloApplication.class.getResource("battle-scene.fxml"));
        BATTLE_RESULTS_SCENE = new FXMLLoader(HelloApplication.class.getResource("battle-results-scene.fxml"));
        BASE_CAMP_SCENE = new FXMLLoader(HelloApplication.class.getResource("base-camp-scene.fxml"));
        SHOP_SCENE = new FXMLLoader(HelloApplication.class.getResource("shop-scene.fxml"));
        MAP_SCENE = new FXMLLoader(HelloApplication.class.getResource("map-scene.fxml"));
        SELECT_PLAYERS_SCENE = new FXMLLoader(HelloApplication.class.getResource("select-players-scene.fxml"));
        TRAINING_SCENE = new FXMLLoader(HelloApplication.class.getResource("observe-team-scene.fxml"));
        HIRE_PLAYERS_SCENE = new FXMLLoader(HelloApplication.class.getResource("hire-players-scene.fxml"));
    }

    //Public attributes representing all the different scene and stages
    public static Stage GAME_STAGE;
    public static final FXMLLoader BATTLE_SCENE, SHOP_SCENE, BATTLE_RESULTS_SCENE, MAP_SCENE, BASE_CAMP_SCENE, SELECT_PLAYERS_SCENE, TRAINING_SCENE, HIRE_PLAYERS_SCENE;
}