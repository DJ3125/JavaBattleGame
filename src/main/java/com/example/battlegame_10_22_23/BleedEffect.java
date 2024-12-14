package com.example.battlegame_10_22_23;

public class BleedEffect extends EffectsReference{

    public BleedEffect(int turnsLasting, boolean targetsGroup, String name, double decreaseHealthAmount){
        super(turnsLasting, true, targetsGroup, name, true, 0, decreaseHealthAmount);
    }

    @Override public String getDescription() {return super.getEffectName() + ": Decrease health of target" + ((super.doesAffectGroup()) ? "'s group": "") + " by " + super.getHealthChange() + " of their health for "+ super.getInitialTurnsRemaining()  + " turns";}
}
