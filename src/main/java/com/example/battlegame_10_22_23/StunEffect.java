package com.example.battlegame_10_22_23;

public class StunEffect extends EffectsReference{
    public StunEffect(boolean affectsGroup, String name){super(1, true, affectsGroup, name, true);}

    @Override
    public String getDescription() {return super.getEffectName() + ": Stun " + (super.doesAffectGroup() ? " an entire group" : "a single target") + " for 1 turn";}
}
