package com.example.battlegame_10_22_23;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class AllPossibleTeammates {

    public AllPossibleTeammates(AllPossibleActions allPossibleActions){
        this.ROBERT_MULDOON = new BattleEntities(49, 60, new ArrayList<>(Arrays.asList(allPossibleActions.getSTRIKE(), allPossibleActions.getBLEEDING_STRIKE(), allPossibleActions.getINVISIBILITY_STRIKE())), "Robert Muldoon", "src/main/resources/Pictures/Muldoon.jpg", new ArrayList<>(Arrays.asList(AttackChangeEffect.class, BleedEffect.class)));
        this.ALAN_GRANT = new BattleEntities(25, 75, new ArrayList<>(Arrays.asList(allPossibleActions.getSTRIKE(), allPossibleActions.getBLEEDING_IMPACT())), "Alan Grant", "src/main/resources/Pictures/Grant.jpg", new ArrayList<>(Arrays.asList(AttackChangeEffect.class)));
        this.IAN_MALCOLM = new BattleEntities(30, 80, new ArrayList<>(Arrays.asList(allPossibleActions.getSTRIKE(), allPossibleActions.getEMPOWERING_STRIKE())), "Ian Malcolm", "src/main/resources/Pictures/Malcolm.jpg");
        this.JOHN_HAMMOND = new BattleEntities(15, 100, new ArrayList<>(Arrays.asList(allPossibleActions.getSTRIKE(), allPossibleActions.getWEAKENING_STRIKE())), "John Hammond", "src/main/resources/Pictures/Hammond.jpg");
        this.ELLIE_SATTLER = new BattleEntities(40, 60, new ArrayList<>(Arrays.asList(allPossibleActions.getSTRIKE(), allPossibleActions.getINVISIBILITY_STRIKE())), "Ellie Sattler", "src/main/resources/Pictures/Sattler.jpg");
    }

    public ArrayList<BattleEntities> getAllPossibleEmployees(){
        ArrayList<BattleEntities> allEmployees = new ArrayList<>();
        for(Field i : AllPossibleTeammates.class.getDeclaredFields()){
            try{if(i.get(this) instanceof BattleEntities){allEmployees.add((BattleEntities) (i.get(this)));}}
            catch(IllegalAccessException error){error.printStackTrace();}
        }
        return allEmployees;
    }

    public BattleEntities getALAN_GRANT() {return this.ALAN_GRANT;}
    public BattleEntities getELLIE_SATTLER() {return this.ELLIE_SATTLER;}
    public BattleEntities getIAN_MALCOLM() {return this.IAN_MALCOLM;}
    public BattleEntities getJOHN_HAMMOND() {return this.JOHN_HAMMOND;}
    public BattleEntities getROBERT_MULDOON() {return this.ROBERT_MULDOON;}

    private final BattleEntities ROBERT_MULDOON, ALAN_GRANT, IAN_MALCOLM, JOHN_HAMMOND, ELLIE_SATTLER;
}
