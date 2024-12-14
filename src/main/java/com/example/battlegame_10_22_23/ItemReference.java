package com.example.battlegame_10_22_23;

import java.util.ArrayList;

public class ItemReference {
    //Get methods, pretty self-explanatory
    public String getName(){return this.name;}
    public double getPrice(){return this.price;}
    public ArrayList<EffectsReference> getEffectsAssociated() {return this.effectsAssociated;}
    public ArrayList<ImmediateHealthChanges> getImmediateHealthChangesAssociated() {return this.immediateHealthChangesAssociated;}

    public boolean doesTargetSelf(){
        boolean hasPositiveRepercussions = false;
        byte interval = 0;
        if(this.effectsAssociated != null){
            while(!hasPositiveRepercussions && interval < this.getEffectsAssociated().size()){
                hasPositiveRepercussions = !this.getEffectsAssociated().get(interval).doesTargetOpponent();
                interval++;
            }
        }
        if(this.immediateHealthChangesAssociated != null){
            interval = 0;
            while(!hasPositiveRepercussions && interval < this.getImmediateHealthChangesAssociated().size()){
                hasPositiveRepercussions = !this.getImmediateHealthChangesAssociated().get(interval).doesTargetOpponent();
                interval++;
            }
        }
        return hasPositiveRepercussions;
    }

    //Set Methods, pretty self-explanatory
    public void setPrice(double price) {this.price = price;}

    public ItemReference(String name, double price, ArrayList<EffectsReference> effectsAssociated, ArrayList<ImmediateHealthChanges> immediateHealthChanges){
        this.name = name;
        this.price = price;
        this.effectsAssociated = effectsAssociated;
        this.immediateHealthChangesAssociated = immediateHealthChanges;
    }

    private final String name;
    private double price;
    private ArrayList<EffectsReference> effectsAssociated;
    private ArrayList<ImmediateHealthChanges> immediateHealthChangesAssociated;
}
