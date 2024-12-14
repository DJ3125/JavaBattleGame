package com.example.battlegame_10_22_23;

public class AttackHealthChange extends ImmediateHealthChanges{
    public AttackHealthChange(double healthChange, boolean targetsGroup){
        super(healthChange, true, targetsGroup, true, false);
    }

    @Override public void setChangeInHealth(BattleEntities target, BattleEntities attacker) {
        target.subtractHealth((int)(super.getHealthChange() * attacker.changeAttackDueToEffects() * target.getDefenseMultiplier()));
    }

    @Override public String getDescription() {
        return "Attack "  + super.getHealthChange() + "x Attack to " + ((super.doesTargetGroup()) ? " all enemies " : "one target");
    }
}
