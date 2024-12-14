package com.example.battlegame_10_22_23;

public class AttackChangeEffect extends EffectsReference{

    public AttackChangeEffect(int turnsLasting, boolean targetsOpponent, boolean targetsGroup, String name, boolean negativeEffect, double attackChange){
        super(turnsLasting, targetsOpponent, targetsGroup, name, negativeEffect, attackChange, 0);
    }

    @Override public String getDescription() {return super.getEffectName() + ": " + ((super.getAttackChange() > 0) ? " Reduce" : "Increase") + " Attack of target"+((super.doesAffectGroup()) ? "'s team": "") + "by " + Math.abs(super.getAttackChange()) + " for " + super.getInitialTurnsRemaining() + " turns";}
}
