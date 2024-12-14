package com.example.battlegame_10_22_23;

public class ModifyTeamArea {

    public PlayerInformation getPlayerInformation() {return this.playerInformation;}
    public ModifyTeamArea(PlayerInformation playerInformation, double attackMultiplier, double healthMultiplier){
        this.playerInformation = playerInformation;
        this.attackMultiplier = attackMultiplier;
        this.healthMultiplier = healthMultiplier;
    }
    public void increaseTeammateStats(BattleEntities teammate){
        teammate.incrementTrainingLevel();
        teammate.setBaseAttack((int)(teammate.getBaseAttack() * this.attackMultiplier));
        teammate.setBaseHealth((int)(teammate.getBaseAttack() * this.healthMultiplier));
        this.playerInformation.decreaseMoneyOwned(this.getUpgradeCost(teammate));
        teammate.setCurrentHealth(teammate.getBaseHealth());
    }
    public double getUpgradeCost(BattleEntities teammate){return ((int)(100*(teammate.getBaseAttack() * (this.attackMultiplier - 1) + teammate.getBaseHealth() * (this.healthMultiplier - 1) + teammate.getTrainingLevel() * 10)))/100.0;}
    public double getAttackMultiplier() {return this.attackMultiplier;}
    public double getHealthMultiplier() {return this.healthMultiplier;}

    public void buyActions(BattleEntities teammate, ActionsReference actionSelected, double cost){
        teammate.getBattleActions().add(new Actions(actionSelected));
        this.playerInformation.decreaseMoneyOwned(cost);
        teammate.setCurrentHealth(teammate.getBaseHealth());
    }

    private PlayerInformation playerInformation;
    private double attackMultiplier, healthMultiplier;
}
