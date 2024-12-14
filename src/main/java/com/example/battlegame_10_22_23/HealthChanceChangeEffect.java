package com.example.battlegame_10_22_23;

public class HealthChanceChangeEffect extends EffectsReference{

    @Override public double getHealthChange() {return ((probabilityOfReducingDamage > Math.random()) ? this.alternativeHealthChangeAmount : super.getHealthChange());}

    public HealthChanceChangeEffect(int turnsLasting, boolean targetsOpponent, boolean targetsGroup, String name, boolean negativeEffect, double normalHealthChangeAmount, double alternativeHealthChangeAmount, double probabilityOfReducingDamage){
        super(turnsLasting, targetsOpponent, targetsGroup, name, negativeEffect, 0, normalHealthChangeAmount);
        this.probabilityOfReducingDamage = probabilityOfReducingDamage;
        this.alternativeHealthChangeAmount = alternativeHealthChangeAmount;
    }

    @Override
    public String getDescription() {return super.getEffectName() + ": Have " + this.probabilityOfReducingDamage + " chance of reducing damage by " + super.getHealthChange() + ". Otherwise, decrease damage by " + this.alternativeHealthChangeAmount + ". Targets" + (super.doesAffectGroup() ? " a group" : " a single target") + "for " + super.getInitialTurnsRemaining() + "turns";}

    public double getAlternativeHealthChangeAmount() {return this.alternativeHealthChangeAmount;}
    public double getProbabilityOfReducingDamage() {return this.probabilityOfReducingDamage;}

    private final double probabilityOfReducingDamage, alternativeHealthChangeAmount;
}
