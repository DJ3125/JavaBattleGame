package com.example.battlegame_10_22_23;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class BattleSceneController{
    //FXML elements here

    @FXML public VBox playerTeamVBox, opponentTeamVBox;
    @FXML public HBox actionAreaHBox;
    @FXML public BarChart<String, Integer> playerTeamHealthBarGraph, opponentTeamHealthBarGraph;
    @FXML public Label actionsLabel, turnLabel;
    @FXML public Button infoButton;

    public BattleArea getSceneInfo() {return this.sceneInfo;}
    public void initializeBattleScene(ArrayList<BattleEntities> playerTeam, ArrayList<BattleEntities> opponentTeam, PlayerInformation playerInformation, AllReferences allReferences){
        //Initializes all the info and sets up the graphics when the scene loads
        HelloApplication.removeUnnecessaryScenes(HelloApplication.BATTLE_SCENE);
        this.sceneInfo = new BattleArea(playerTeam, opponentTeam, playerInformation);
        this.allReferences = allReferences;
        this.playerTeamVBox.setAlignment(Pos.CENTER);
        this.playerTeamVBox.setSpacing(20);
        this.opponentTeamVBox.setAlignment(Pos.CENTER);
        for(byte i = 1; i <= this.sceneInfo.getOpponentTeam().size(); i++){
            this.sceneInfo.getOpponentTeam().get(i - 1).setName(this.sceneInfo.getOpponentTeam().get(i - 1).getName() + " " + i);
        }
        this.opponentTeamVBox.setSpacing(20);
        this.actionAreaHBox.getChildren().clear();
        this.actionAreaHBox.setAlignment(Pos.CENTER);
        this.updateTeamDisplay(true);
        this.updateTeamDisplay(false);
        this.informationArea = new InformationArea();
    }

    /*
    Parameters: Requires a boolean value to determine which display is needed to be updated, and a boolean value to show whether the borders on the icons will be displayed or not
    Return: Has no return value
    Purpose: Updates the icons of the team selected, and also updates their health bars
     */
    private void updateTeamDisplay(boolean updatePlayerTeam){
        //Clears data from the health bars and from the icons VBox
        ((updatePlayerTeam) ? this.playerTeamVBox : this.opponentTeamVBox).getChildren().clear();
        ((updatePlayerTeam) ? this.playerTeamHealthBarGraph : this.opponentTeamHealthBarGraph).getData().clear();
        XYChart.Series<String, Integer> data = new XYChart.Series<>();
        for(BattleEntities i : ((updatePlayerTeam) ? sceneInfo.getPlayerTeam() : sceneInfo.getOpponentTeam())){
            data.getData().add(new XYChart.Data<>(i.getName(), i.getCurrentHealth())); //Adds data to the series for each entity on the specified team, which will eventually be added to the bar graph
            //Creates the icon for each entity on the specified team
            HBox playerInfo = new HBox();
            playerInfo.setAlignment(Pos.CENTER);
            playerInfo.getChildren().add(new Label(i.getName() + "\nHP:" + i.getCurrentHealth()));
            if(i.getCurrentHealth() > 0 && !i.isPerformedAction() && this.sceneInfo.isMyTurn()){ //If the entity hasn't gone yet, and if borders are wanted to be shown
                playerInfo.setStyle("-fx-border-style: solid;");
                playerInfo.setStyle("-fx-border-width: 2;");
                playerInfo.setStyle("-fx-border-color: blue;");
                if(updatePlayerTeam){playerInfo.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> this.handlePlayerIconClicked(i));}//If the entity hasn't gone yet, allow the player to click on it and display the actions
            }
            playerInfo.addEventHandler(MouseEvent.MOUSE_ENTERED, (mouseEvent)-> this.informationArea.getInfoStageController().updateInfoScene(i));
            try{ //Sets the image for the icon for each entity on the specified team.
                ImageView entityIcon = new ImageView(new Image(new FileInputStream(i.getPictureLink())));
                entityIcon.setPreserveRatio(true);
                entityIcon.setFitHeight(50);
                entityIcon.setOpacity((i.getCurrentHealth() <= 0) ? 0.5 : 1.0);//Causes the entity to be half transparent if the entity is defeated
                playerInfo.getChildren().add(entityIcon);
            } catch(FileNotFoundException error){error.printStackTrace();}
            ((updatePlayerTeam) ? this.playerTeamVBox : this.opponentTeamVBox).getChildren().add(playerInfo);//Adds the icon to the HBox containing the icons of that specific team
        }
        ((updatePlayerTeam) ? this.playerTeamHealthBarGraph : this.opponentTeamHealthBarGraph).getData().add(data); //Adds the data containing the health of the specified team
    }

    /*
    Parameters: Requires a battle entity to get the actions from
    Return: Has no return value
    Purpose: When an icon is clicked during the player's turn, their actions appear as buttons. When those buttons are clicked, the player has the option to choose their targets or their items, depending on what they pick
     */
    private void handlePlayerIconClicked (BattleEntities entity){
        this.actionsLabel.setText(entity.getName() + "'s Actions");
        this.updateTeamDisplay(true);
        this.updateTeamDisplay(false);
        this.actionAreaHBox.getChildren().clear();//Removes all the other children in the HBox
        for(Actions i : entity.getBattleActions()){//Creates a button for each action, sets an action for their to perform when clicked, and then adds that button to the HBox action area
            Button action = new Button(i.getActionsReference().getName());
            action.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> this.selectTargets(entity, i, null));
            actionAreaHBox.getChildren().add(action);
        }
        Button action = new Button(); //Does the same as before, but instead names it something else, and it makes the player select the item that they want to use
        action.setText("Use An Item");
        action.setDisable(this.sceneInfo.getPlayerInformation().getItemsOwned().isEmpty());
        action.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> this.selectItems(entity));
        actionAreaHBox.getChildren().add(action);
    }

    /*
    Parameters: Requires a battle entity to get who is using the item
    Return: has no return value
    Purpose: it makes the player select the item they want to use
     */
    private void selectItems(BattleEntities selectedEntity){
        this.actionAreaHBox.getChildren().clear();
        VBox container = new VBox(new Label("Select Your Item"));
        ListView<String> itemsListView = new ListView<>();
        for(Items i : this.sceneInfo.getPlayerItems()){itemsListView.getItems().add(i.getItemReference().getName() + " (" + i.getQuantity() + ")");}//Adds all the possible items to a ListView
        itemsListView.getSelectionModel().selectFirst();
        Button backButton = new Button("Back");
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> this.handlePlayerIconClicked(selectedEntity)); //When the back button is pressed, it displays all the possible actions again
        Button nextButton = new Button("Next");
        nextButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) ->{ //When the next button is hit, it asks what the targets are
            ItemReference selectedItem = this.sceneInfo.getPlayerItems().get(itemsListView.getSelectionModel().getSelectedIndex()).getItemReference();
            this.selectTargets(selectedEntity, null, selectedItem);
        });
        container.getChildren().add(itemsListView);
        container.getChildren().add(new HBox(backButton, nextButton));
        this.actionAreaHBox.getChildren().add(container);
    }

    private void selectTargets(BattleEntities selectedEntity, Actions selectedAction, ItemReference selectedItem){
        this.actionAreaHBox.getChildren().clear();
        VBox targetContainer = new VBox();
        targetContainer.setAlignment(Pos.CENTER);
        HBox targetListViewBox = new HBox();
        targetListViewBox.setSpacing(20);
        targetListViewBox.setAlignment(Pos.CENTER);
        ListView<String> myTeamListView = new ListView<>(), opponentTeamListView = new ListView<>(); //Creates new ListViews so it is easier to access
        myTeamListView.setPrefSize(50, 50);
        opponentTeamListView.setPrefSize(50, 50);
        for(byte i = 0; i < 2; i++){//Modifies the two list views from above, filling them up with valid targets (ex. targets that haven't 'expired') and then adding those list views to the action area
            VBox container = new VBox(new Label((i == 0) ? "Target On My Team" : "Target On Opponent's Team"));
            ListView<String> teamTargets = (i == 0) ? myTeamListView : opponentTeamListView;
            for(BattleEntities j : (i == 0) ? this.sceneInfo.getPlayerTeam() : this.sceneInfo.getOpponentTeam()){if(j.getCurrentHealth() > 0){teamTargets.getItems().add(j.getName());}}
            teamTargets.getSelectionModel().selectFirst();
            container.getChildren().add(teamTargets);
            targetListViewBox.getChildren().add(container);
        }
        targetContainer.getChildren().add(targetListViewBox);
        Button backButton = new Button("Back");//Creates the buttons and adds them to the containers
        Button submitButton = new Button("Submit");
        targetContainer.getChildren().add(new HBox(backButton, submitButton));
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> handlePlayerIconClicked(selectedEntity));//When the back button is hit, it displays all the possible actions again
        this.actionAreaHBox.getChildren().addAll(targetContainer);
        submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {//When the submit button is hit, it either performs an action or uses an item. Either way it counts as a turn.
            byte interval = 0;
            while(interval < this.sceneInfo.getPlayerTeam().size() && !this.sceneInfo.getPlayerTeam().get(interval).getName().equals(myTeamListView.getSelectionModel().getSelectedItem())){interval++;}
            BattleEntities myTeamTarget = this.sceneInfo.getPlayerTeam().get(interval);
            interval = 0;
            while(interval < this.sceneInfo.getOpponentTeam().size() && !this.sceneInfo.getOpponentTeam().get(interval).getName().equals(opponentTeamListView.getSelectionModel().getSelectedItem())){interval++;}
            BattleEntities opponentTeamTarget = this.sceneInfo.getOpponentTeam().get(interval);
            this.actionAreaHBox.getChildren().clear();
            String txt = "";
            if(selectedAction != null){//Displays what action was performed or what item was used, who used it, and on what targets
                this.sceneInfo.performActions(selectedEntity, selectedAction, myTeamTarget, opponentTeamTarget);
                txt = selectedEntity.getName() + " Performed " + selectedAction.getActionsReference().getName() + " On " + opponentTeamTarget.getName() + " and " + myTeamTarget.getName() + "!";
            }else if(selectedItem != null) {
                this.sceneInfo.useItem(selectedItem, myTeamTarget, opponentTeamTarget, selectedEntity);
                txt = selectedEntity.getName() + " used " + selectedItem.getName() + " on " + myTeamTarget.getName() + " and " +opponentTeamTarget.getName();
            }
            this.sceneInfo.manageTurnSwitching();
            this.updateTeamDisplay(true);
            this.updateTeamDisplay(false);
            Label descriptionLabel = new Label(txt);
            descriptionLabel.setWrapText(true);
            VBox infoVBox = new VBox(descriptionLabel);
            infoVBox.setAlignment(Pos.CENTER);
            infoVBox.setSpacing(10);
            this.actionAreaHBox.getChildren().add(infoVBox);
            byte battleResults = this.sceneInfo.checkForEndOfBattle(); //Gets the battle data from this moment in time
            Button continueButton = new Button("Continue: " + ((this.sceneInfo.isMyTurn()) ? "it's your turn!" : "Its the opponent's turn"));
            if(battleResults == -1){infoVBox.getChildren().add(new Label("Its A Stalemate"));}//Adds a label depending on the status of the battle
            else if(battleResults == 1){infoVBox.getChildren().add(new Label("You Won!"));}
            else if(battleResults == 2){infoVBox.getChildren().add(new Label("You Lost!"));}
            else{continueButton.setText("Continue");}
            infoVBox.getChildren().add(continueButton);
            continueButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent2) -> {this.checkForSwitching(battleResults);});
        });
    }

    private void checkForSwitching(int battleResults){
        this.actionAreaHBox.getChildren().clear();
        if(battleResults != 0){
            HelloApplication.setStage(HelloApplication.BATTLE_RESULTS_SCENE);//Goes to the results scene
            BattleResultsSceneController resultsSceneController = ((HelloApplication.BATTLE_RESULTS_SCENE.getController()));
            this.informationArea.getInfoStage().close();
            this.sceneInfo.clearAllEffects();
            if(battleResults == -1){resultsSceneController.initializeBattleResultsScene(this.sceneInfo.getPlayerInformation(), this.allReferences);} //depending on the result, info is passed into the next scene
            else if(battleResults == 1){
                int totalScore = 0;
                for(BattleEntities i : this.sceneInfo.getOpponentTeam()){totalScore += i.getPower();}
                resultsSceneController.initializeBattleResultsSceneWithMoney(this.sceneInfo.getPlayerInformation(), totalScore, this.allReferences);
            }else if(battleResults == 2){resultsSceneController.initializeBattleResultsSceneWithItems(this.sceneInfo.getPlayerInformation(), this.allReferences);}
        }else if(!this.sceneInfo.isMyTurn()) {
            this.updateTeamDisplay(false); //Switches to the opponents turn
            this.updateTeamDisplay(true);
            this.performOpponentTurn();
        }else{
            this.actionAreaHBox.getChildren().clear();//Still is the plays turn, and prompts them to select another action
            this.actionsLabel.setText("Select An Entity");
            this.updateTeamDisplay(true);
        }
    }

    /*
    Parameters: Requires no explicit parameters
    Return: Has no return value
    Purpose: performs the opponent's turn
    */
    private void performOpponentTurn(){
        this.turnLabel.setText("Opponent's Turn");
        this.actionsLabel.setText("Opponent's Turn");
        this.actionAreaHBox.getChildren().clear();
        boolean performedTurn = false;
        byte interval = 0;
        while(interval < this.sceneInfo.getOpponentTeam().size() && !performedTurn){
            if(!this.sceneInfo.getOpponentTeam().get(interval).isPerformedAction() && this.sceneInfo.getOpponentTeam().get(interval).getCurrentHealth() > 0){
                byte actionInterval = 0;
                while(!performedTurn && actionInterval < this.sceneInfo.getOpponentTeam().get(interval).getBattleActions().size()){
                    if(this.sceneInfo.getOpponentTeam().get(interval).getBattleActions().get(actionInterval).getCurrentCoolDown() == 0){
                        performedTurn = true;
                        System.out.println(true);
                        BattleEntities selectedOpponent = null, selectedTeammate = null;
                        for(BattleEntities j: this.sceneInfo.getOpponentTeam()){if(j.getCurrentHealth() > 0){selectedTeammate = j;}}
                        for(BattleEntities j: this.sceneInfo.getPlayerTeam()){if(j.getCurrentHealth() > 0){selectedOpponent = j;}}
                        assert selectedTeammate != null;
                        assert selectedOpponent != null;
                        this.sceneInfo.performActions(this.sceneInfo.getOpponentTeam().get(interval), this.sceneInfo.getOpponentTeam().get(interval).getBattleActions().get(actionInterval), selectedOpponent, selectedTeammate);
                        this.actionAreaHBox.getChildren().add(new VBox(new Label(this.sceneInfo.getOpponentTeam().get(interval).getName() + " Performed " + this.sceneInfo.getOpponentTeam().get(interval).getBattleActions().get(actionInterval).getActionsReference().getName() + " On " + selectedTeammate.getName() + selectedOpponent.getName())));
                    }
                    actionInterval++;
                }
            }
            interval++;
        }
        this.sceneInfo.manageTurnSwitching();
        this.updateTeamDisplay(false);
        this.updateTeamDisplay(true);
        Button continueButton = new Button("Continue: " + ((this.sceneInfo.isMyTurn()) ? "it's now your turn!" : "Its still the opponent's turn"));
        ((VBox)(this.actionAreaHBox.getChildren().get(0))).getChildren().add(continueButton);
        continueButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> this.checkForSwitching(this.sceneInfo.checkForEndOfBattle()));
    }

    @FXML public void handleAbortBattle(){
        Stage stage = new Stage();
        VBox root = new VBox(new VBox(new Label("If You flee, it will count as a loss. Will You Continue?")));
        Button abortButton = new Button("Abort");
        Button continueButton = new Button("Do Not Abort");
        abortButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent)->{
            HelloApplication.setStage(HelloApplication.BATTLE_RESULTS_SCENE);
            ((BattleResultsSceneController)((HelloApplication.BATTLE_RESULTS_SCENE.getController()))).initializeBattleResultsSceneWithItems(this.sceneInfo.getPlayerInformation(), this.allReferences);
            this.informationArea.getInfoStage().close();
            stage.close();
        });
        continueButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent)-> stage.close());
        root.getChildren().addAll(abortButton, continueButton);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Abort Battle?");
        stage.show();
    }

    @FXML public void handlePullUpInfo(){
        this.informationArea.getInfoStage().show();
        this.informationArea.getInfoStageController().updateInfoScene(this.sceneInfo.getPlayerTeam().get(0));
    }

    //Private object that contains all the data and methods required for running the battle
    private BattleArea sceneInfo;
    private InformationArea informationArea;
    private AllReferences allReferences;
}