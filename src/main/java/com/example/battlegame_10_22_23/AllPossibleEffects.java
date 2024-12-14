package com.example.battlegame_10_22_23;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AllPossibleEffects {

    public AllPossibleEffects(){
        this.MINOR_BLEED = new BleedEffect(4, false, "Minor Bleed", 0.2);
        this.MAJOR_BLEED = new BleedEffect(3, false, "Major Bleed", 0.34);
        this.QUICK_BLEED = new BleedEffect(2, false, "Quick Bleed", 0.51);

        this.CAMOUFLAGE = new HealthChanceChangeEffect(2, false, false, "Camouflage", false, 0.9, 0.5, 0.67);

        this.WEAKEN = new AttackChangeEffect(3, true, false, "Weaken", true, 0.37);
        this.ATTACK_INCREASE = new AttackChangeEffect(2, false, false, "Attack Increase", false, -0.2);

        this.STUN = new StunEffect(false, "Stun");
    }

    public EffectsReference getMINOR_BLEED() {return this.MINOR_BLEED;}
    public EffectsReference getCAMOUFLAGE() {return this.CAMOUFLAGE;}
    public EffectsReference getSTUN() {return this.STUN;}
    public EffectsReference getWEAKEN() {return this.WEAKEN;}
    public EffectsReference getATTACK_INCREASE() {return this.ATTACK_INCREASE;}
    public EffectsReference getMAJOR_BLEED() {return this.MAJOR_BLEED;}
    public EffectsReference getQUICK_BLEED() {return this.QUICK_BLEED;}

    public ArrayList<EffectsReference> getAllEffects(){
        ArrayList<EffectsReference> allEffects = new ArrayList<>();
        for(Field i : AllPossibleEffects.class.getDeclaredFields()){
            try{if(i.get(this) instanceof EffectsReference){allEffects.add((EffectsReference) (i.get(this)));}}
            catch(IllegalAccessException error){error.printStackTrace();}
        }
        return allEffects;
    }

    private final EffectsReference MINOR_BLEED, CAMOUFLAGE, STUN, ATTACK_INCREASE, MAJOR_BLEED, WEAKEN, QUICK_BLEED;
}
