package com.example.battlegame_10_22_23;

public class HealingHealthChanges extends ImmediateHealthChanges{

    public HealingHealthChanges(double healthChange, boolean targetsGroup, boolean isAMultiplier){
        super(healthChange, false, targetsGroup, isAMultiplier, true);
    }

    @Override public void setChangeInHealth(BattleEntities target, BattleEntities attacker) {
        if(super.isAMultiplier()){target.addHealth((int)(super.getHealthChange() * target.getBaseHealth()));}
        else{target.addHealth((int)(super.getHealthChange()));}
    }

    @Override public String getDescription() {return "Heal " + (-super.getHealthChange()) + ((super.isAMultiplier()) ? "x Base Health" : "HP" + " for " + (super.doesTargetGroup() ? "each teammate" : "for the target"));}
}
