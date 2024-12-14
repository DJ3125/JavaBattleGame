package com.example.battlegame_10_22_23;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ShopSceneController {
    @FXML public Label moneyRemainingLabel;
    @FXML public HBox itemsHBox;
    @FXML public Button returnToBaseCampButton;

    public void initializeShopScene(PlayerInformation playerInformation, AllReferences allReferences){
        HelloApplication.removeUnnecessaryScenes(HelloApplication.SHOP_SCENE);
        this.allReferences =allReferences;
        this.playerInformation = playerInformation;
        this.updateBuyingScene();
        this.itemsHBox.setAlignment(Pos.CENTER);
        this.itemsHBox.setSpacing(20);
    }

    private void updateBuyingScene(){
        this.itemsHBox.getChildren().clear();
        this.moneyRemainingLabel.setText("Money Remaining: $" + this.playerInformation.getMoneyOwned());
        for(ItemReference j : this.allReferences.getALL_POSSIBLE_ITEMS().getAllItemReferences()){
            VBox itemInfo = new VBox();
            itemInfo.setAlignment(Pos.CENTER);
            int amountOfItem = 0;
            int interval = 0;
            while(interval < this.playerInformation.getItemsOwned().size() && amountOfItem == 0){
                if(this.playerInformation.getItemsOwned().get(interval).getItemReference().equals(j)){amountOfItem = this.playerInformation.getItemsOwned().get(interval).getQuantity();}
                interval++;
            }
            itemInfo.getChildren().addAll(new Label(j.getName()), new Label("\nYou Have " + amountOfItem + " of this item. \n It Costs $" + j.getPrice()));
            Button buyButton = new Button("Buy Item");
            buyButton.setDisable(this.playerInformation.getMoneyOwned() < j.getPrice());
            buyButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent)->{
                this.playerInformation.buyItems(j, 1);
                this.updateBuyingScene();
            });
            for(Node i : itemInfo.getChildren()){if(i.getClass() == Label.class){((Label)(i)).setAlignment(Pos.CENTER);}}
            itemInfo.getChildren().add(buyButton);
            this.itemsHBox.getChildren().add(itemInfo);
        }
    }
    @FXML public void handleReturnToBaseCamp(){
        HelloApplication.setStage(HelloApplication.BASE_CAMP_SCENE);
        ((BaseCampSceneController)(HelloApplication.BASE_CAMP_SCENE.getController())).initializeBaseCampScene(this.playerInformation, this.allReferences);
    }
    private PlayerInformation playerInformation;
    private AllReferences allReferences;
}
