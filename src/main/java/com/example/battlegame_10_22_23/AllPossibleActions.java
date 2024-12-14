package com.example.battlegame_10_22_23;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AllPossibleActions {
    
    public AllPossibleActions(AllPossibleEffects allPossibleEffects, AllImmediateHealthChanges allImmediateHealthChanges){
        this.STRIKE = new ActionsReference(0, "Strike", null, new ArrayList<>());
        this.STRIKE.getAssociatedHealthChanges().add(allImmediateHealthChanges.getSTRIKE());
        this.BLEEDING_STRIKE = new ActionsReference(3, "Bleeding Strike", new ArrayList<>(), new ArrayList<>());
        this.BLEEDING_STRIKE.getAssociatedHealthChanges().add(allImmediateHealthChanges.getSTRIKE());
        this.BLEEDING_STRIKE.getAssociatedEffects().add(allPossibleEffects.getMAJOR_BLEED());
        this.BLEEDING_IMPACT = new ActionsReference(4, "Bleeding Impact", new ArrayList<>(), new ArrayList<>());
        this.BLEEDING_IMPACT.getAssociatedHealthChanges().add(allImmediateHealthChanges.getIMPACT());
        this.BLEEDING_IMPACT.getAssociatedEffects().add(allPossibleEffects.getMINOR_BLEED());
        this.INVISIBILITY_STRIKE = new ActionsReference(2, "Invisibility Strike", new ArrayList<>(), new ArrayList<>());
        this.INVISIBILITY_STRIKE.getAssociatedEffects().add(allPossibleEffects.getCAMOUFLAGE());
        this.INVISIBILITY_STRIKE.getAssociatedHealthChanges().add(allImmediateHealthChanges.getSTRIKE());
        this.EMPOWERING_INVISIBILITY = new ActionsReference(3, "Empowering Invisibility", new ArrayList<>(), null);
        this.EMPOWERING_INVISIBILITY.getAssociatedEffects().add(allPossibleEffects.getCAMOUFLAGE());
        this.EMPOWERING_INVISIBILITY.getAssociatedEffects().add(allPossibleEffects.getATTACK_INCREASE());
        this.WEAKENING_STRIKE = new ActionsReference(1, "Weakening Strike", new ArrayList<>(), new ArrayList<>());
        this.WEAKENING_STRIKE.getAssociatedEffects().add(allPossibleEffects.getWEAKEN());
        this.WEAKENING_STRIKE.getAssociatedHealthChanges().add(allImmediateHealthChanges.getSTRIKE());
        this.EMPOWERING_STRIKE = new ActionsReference(1, "Empowering Strike", new ArrayList<>(), new ArrayList<>());
        this.EMPOWERING_STRIKE.getAssociatedHealthChanges().add(allImmediateHealthChanges.getSTRIKE());
        this.EMPOWERING_STRIKE.getAssociatedEffects().add(allPossibleEffects.getATTACK_INCREASE());
        this.STUNNING_STRIKE = new ActionsReference(3, "Stunning Strike", new ArrayList<>(), new ArrayList<>());
        this.STUNNING_STRIKE.getAssociatedEffects().add(allPossibleEffects.getSTUN());
        this.STUNNING_STRIKE.getAssociatedHealthChanges().add(allImmediateHealthChanges.getSTRIKE());
    }
    
    public ArrayList<ActionsReference> getAllActionReferences(){
        ArrayList<ActionsReference> allActions = new ArrayList<>();
        for(Field i : AllPossibleActions.class.getDeclaredFields()){
            try{if(i.get(this) instanceof ActionsReference){allActions.add((ActionsReference) (i.get(this)));}}
            catch(IllegalAccessException error){error.printStackTrace();}
        }
        return allActions;
    }

    public ActionsReference getSTRIKE() {return this.STRIKE;}
    public ActionsReference getBLEEDING_STRIKE() {return this.BLEEDING_STRIKE;}
    public ActionsReference getBLEEDING_IMPACT() {return this.BLEEDING_IMPACT;}
    public ActionsReference getEMPOWERING_INVISIBILITY() {return this.EMPOWERING_INVISIBILITY;}
    public ActionsReference getEMPOWERING_STRIKE() {return this.EMPOWERING_STRIKE;}
    public ActionsReference getINVISIBILITY_STRIKE() {return this.INVISIBILITY_STRIKE;}
    public ActionsReference getSTUNNING_STRIKE() {return this.STUNNING_STRIKE;}
    public ActionsReference getWEAKENING_STRIKE() {return this.WEAKENING_STRIKE;}

    private final ActionsReference STRIKE, BLEEDING_STRIKE, BLEEDING_IMPACT, INVISIBILITY_STRIKE, STUNNING_STRIKE, EMPOWERING_INVISIBILITY, WEAKENING_STRIKE, EMPOWERING_STRIKE;
}
