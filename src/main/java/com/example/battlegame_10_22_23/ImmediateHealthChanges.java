package com.example.battlegame_10_22_23;

public abstract class ImmediateHealthChanges {
    /*Get Methods. Pretty Self Explanatory*/
    public boolean doesTargetGroup() {return this.targetsGroup;}
    public boolean doesTargetOpponent() {return this.targetsOpponent;}

    public double getHealthChange() {return this.changeInHealth;}
    public boolean isAMultiplier() {return this.isAMultiplier;}
    public boolean isAttackIndependent() {return this.attackIndependent;}

    public abstract String getDescription();
    public abstract void setChangeInHealth(BattleEntities target, BattleEntities attacker); //Attack modifier is the proportion of attack remaining. For example, 0.85 represents that the attack is 85% of the value of the base attack
    
    //Constructor. Requires 4 boolean and one double, to specify the qualities (ex. who it targets, how to apply the health change) of the health change and it's magnitude
    public ImmediateHealthChanges(double changeInHealth, boolean targetsOpponent, boolean targetsGroup, boolean isAMultiplier, boolean attackIndependent){
        this.changeInHealth = changeInHealth;
        this.targetsGroup = targetsGroup;
        this.targetsOpponent = targetsOpponent;
        this.isAMultiplier = isAMultiplier;
        this.attackIndependent = attackIndependent;
    }

    //Private attributes
    private final double changeInHealth;
    private final boolean targetsOpponent, targetsGroup, isAMultiplier, attackIndependent;
}
