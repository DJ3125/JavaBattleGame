package com.example.battlegame_10_22_23;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SavingAndReading {

    public void saveData(PlayerInformation playerInformation, AllReferences allReferences){//Called when the program exits
        try{
            PrintWriter fileWriter = new PrintWriter("src/main/resources/SaveFiles/MoneyOwned.txt");
            fileWriter.print(playerInformation.getMoneyOwned());
            fileWriter.close();
            PrintWriter itemWriter = new PrintWriter("src/main/resources/SaveFiles/Items.txt");
            if(!playerInformation.getItemsOwned().isEmpty()){
                ArrayList<ItemReference> itemReferences = allReferences.getALL_POSSIBLE_ITEMS().getAllItemReferences();
                for(Items i : playerInformation.getItemsOwned()){
                    byte itemReferenceInterval = 0;
                    while(itemReferenceInterval < itemReferences.size() && !i.getItemReference().getName().equals(itemReferences.get(itemReferenceInterval).getName())){itemReferenceInterval++;}
                    itemWriter.println(itemReferenceInterval);
                    itemWriter.println(i.getQuantity());
                }
            }
            itemWriter.close();
            for(BattleEntities i : playerInformation.getTeamMembers()){
                PrintWriter playerWriter = new PrintWriter("src/main/resources/SaveFiles/Teammates/" + i.getName().replace(" ", "") + ".txt");
                playerWriter.print(i.getSavingInformation(allReferences));
                playerWriter.close();
            }
        }catch(IOException error){error.printStackTrace();}
    }

    public PlayerInformation readData(AllReferences allReferences) throws FileNotFoundException{
        Scanner readMoneyOwned = new Scanner(new File("src/main/resources/SaveFiles/MoneyOwned.txt"));
        PlayerInformation playerInformation = new PlayerInformation();
        playerInformation.setMoneyOwned(Double.parseDouble(readMoneyOwned.nextLine()));
        readMoneyOwned.close();
        Scanner getItemsOwned = new Scanner(new File("src/main/resources/SaveFiles/Items.txt"));
        ArrayList<ItemReference> allPossibleItems = allReferences.getALL_POSSIBLE_ITEMS().getAllItemReferences();
        while(getItemsOwned.hasNextLine()){
            ItemReference reference = allPossibleItems.get(Integer.parseInt(getItemsOwned.nextLine()));
            playerInformation.getItemsOwned().add(new Items(reference, Integer.parseInt(getItemsOwned.nextLine())));
        }
        getItemsOwned.close();
        for(File i : (new File("src/main/resources/SaveFiles/Teammates")).listFiles()){
            try{
                BattleEntities battleEntities = new BattleEntities();
                Scanner getBattleEntity = new Scanner(i);
                byte counter = 0;
                while(getBattleEntity.hasNextLine()){
                    battleEntities.setInformationFromSave(getBattleEntity.nextLine(), BattleEntities.class.getDeclaredFields()[counter], allReferences);
                    counter++;
                }
                playerInformation.getTeamMembers().add(battleEntities);
                getBattleEntity.close();
            }catch (FileNotFoundException error){error.printStackTrace();}
        }
        return playerInformation;
    }

}
