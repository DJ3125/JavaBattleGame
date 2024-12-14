package com.example.battlegame_10_22_23;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class AllPossibleDinosaurs {

    public AllPossibleDinosaurs(AllPossibleActions allPossibleActions){
        this.COMPY = new BattleEntities(10, 20, new ArrayList<>(Arrays.asList(allPossibleActions.getSTRIKE(), allPossibleActions.getINVISIBILITY_STRIKE())), "Compy", "src/main/resources/Pictures/Compy.jpg");
        this.MEGA_COMPY = new BattleEntities(30, 90, new ArrayList<>(Arrays.asList(allPossibleActions.getSTRIKE(), allPossibleActions.getINVISIBILITY_STRIKE())), "Mega Compy", "src/main/resources/Pictures/Compy.jpg");
        this.DILOPHOSAUR = new BattleEntities(35, 100, new ArrayList<>(Arrays.asList(allPossibleActions.getSTRIKE(), allPossibleActions.getSTUNNING_STRIKE())), "Dilophosaurus", "src/main/resources/Pictures/pixil-frame-0 (1) (3).png");
        this.MEGA_DILOPHOSAUR = new BattleEntities(50, 150, new ArrayList<>(Arrays.asList(allPossibleActions.getSTRIKE(), allPossibleActions.getSTUNNING_STRIKE())), "Mega Dilophosaur", "src/main/resources/Pictures/pixil-frame-0 (1) (3).png");
        this.RAPTOR = new BattleEntities(70, 200, new ArrayList<>(Arrays.asList(allPossibleActions.getSTRIKE(), allPossibleActions.getEMPOWERING_STRIKE())), "Raptor", "src/main/resources/Pictures/pixil-frame-0 (2) (4).png");
        this.MEGA_RAPTOR = new BattleEntities(90, 250, new ArrayList<>(Arrays.asList(allPossibleActions.getSTRIKE(), allPossibleActions.getEMPOWERING_STRIKE())), "Mega Raptor", "src/main/resources/Pictures/pixil-frame-0 (2) (4).png");
        this.INDOMINUS_REX = new BattleEntities(150, 600, new ArrayList<>(Arrays.asList(allPossibleActions.getSTRIKE(), allPossibleActions.getWEAKENING_STRIKE(), allPossibleActions.getEMPOWERING_STRIKE())), "Indominus Rex", "src/main/resources/Pictures/pixil-frame-0 (29).png");
    }

    public ArrayList<BattleEntities> getAllDinos(){
        ArrayList<BattleEntities> allDinos = new ArrayList<>();
        for(Field i : AllPossibleDinosaurs.class.getDeclaredFields()){
            try{if(i.get(this) instanceof BattleEntities){allDinos.add((BattleEntities)(i.get(this)));}}
            catch(IllegalAccessException error){error.printStackTrace();}
        }
        return allDinos;
    }

    public BattleEntities getCOMPY() {return this.COMPY;}
    public BattleEntities getDILOPHOSAUR() {return this.DILOPHOSAUR;}
    public BattleEntities getINDOMINUS_REX() {return this.INDOMINUS_REX;}
    public BattleEntities getMEGA_COMPY() {return this.MEGA_COMPY;}
    public BattleEntities getMEGA_DILOPHOSAUR() {return this.MEGA_DILOPHOSAUR;}
    public BattleEntities getMEGA_RAPTOR() {return this.MEGA_RAPTOR;}
    public BattleEntities getRAPTOR() {return this.RAPTOR;}

    private final BattleEntities COMPY, MEGA_COMPY, DILOPHOSAUR, MEGA_DILOPHOSAUR, RAPTOR, MEGA_RAPTOR, INDOMINUS_REX;
}
