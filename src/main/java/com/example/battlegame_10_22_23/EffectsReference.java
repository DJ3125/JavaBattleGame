package com.example.battlegame_10_22_23;

public abstract class EffectsReference {
    //Get methods, pretty self-explanatory
    public int getInitialTurnsRemaining() {return this.initialTurnsRemaining ;}
    public boolean doesTargetOpponent() {return this.targetsOpponent;}
    public boolean doesAffectGroup() {return this.affectsGroup;}
    public double getAttackChange(){return this.attackAmount;}
    public double getHealthChange(){return this.healAmount;}
    public String getEffectName(){return this.effectName;}
    public boolean isNegativeEffect() {return this.negativeEffect;}

    public abstract String getDescription();


    //Constructors
    public EffectsReference(int turnsRemaining, boolean targetsOpponent, boolean groupEffect, String name, boolean negativeEffect){
        this.initialTurnsRemaining = turnsRemaining;
        this.affectsGroup = groupEffect;
        this.targetsOpponent = targetsOpponent;
        this.effectName = name;
        this.attackAmount = 0;
        this.healAmount = 0;
        this.negativeEffect = negativeEffect;
    }

    public EffectsReference(int turnsRemaining, boolean targetsOpponent, boolean groupEffect, String name, boolean negativeEffect, double attackAmount, double healAmount){
        this(turnsRemaining, targetsOpponent, groupEffect, name, negativeEffect);
        this.attackAmount = attackAmount;
        this.healAmount = healAmount;
    }


    //Private attributes
    private final int initialTurnsRemaining;
    private double attackAmount, healAmount;
    private final boolean targetsOpponent, affectsGroup, negativeEffect;
    private final String effectName;
}
