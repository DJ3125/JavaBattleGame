package com.example.battlegame_10_22_23;

import java.util.ArrayList;

public class ActionsReference {
    /*
    Get methods. Pretty self-explanatory
     */
    public int getMaxCoolDown(){return this.maxCoolDown;}

    public String getName(){return this.name;}
    public ArrayList<EffectsReference> getAssociatedEffects() {return this.associatedEffects;}
    public ArrayList<ImmediateHealthChanges> getAssociatedHealthChanges() {return this.associatedHealthChanges;}
    public String getEffectsDescription(){
        String txt = "";
        if(this.associatedEffects != null){for(EffectsReference i : this.associatedEffects){txt = txt.concat(i.getDescription() + "\n");}}
        else{txt = "No Associated Effects";}
        return txt;
    }

    public String getHealthChangeDescription(){
        String txt = "";
        if(this.associatedHealthChanges != null){for(ImmediateHealthChanges i : this.associatedHealthChanges){txt = txt.concat(i.getDescription() + "\n");}}
        else{txt = "No Associated Health Changes";}
        return txt;
    }

    /*
    Constructor: Takes in an integer to represent the maximum cool-down, a String to represent the name, and two ArrayLists to represent the effects associated and the immediate health changes associated
     */
    public ActionsReference(int maxCoolDown, String actionName, ArrayList<EffectsReference> effectsAssociated, ArrayList<ImmediateHealthChanges> immediateHealthChanges){
        this.maxCoolDown = maxCoolDown;
        this.name = actionName;
        this.associatedEffects = effectsAssociated;
        this.associatedHealthChanges = immediateHealthChanges;
    }

    //Private attributes
    private final int maxCoolDown;
    private final String name;
    private final ArrayList<EffectsReference> associatedEffects;
    private final ArrayList<ImmediateHealthChanges> associatedHealthChanges;
}
