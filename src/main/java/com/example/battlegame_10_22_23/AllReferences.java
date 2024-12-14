package com.example.battlegame_10_22_23;

public class AllReferences {

    public AllReferences(){
        this.ALL_IMMEDIATE_HEALTH_CHANGES = new AllImmediateHealthChanges();
        this.ALL_POSSIBLE_EFFECTS = new AllPossibleEffects();
        this.ALL_POSSIBLE_ACTIONS = new AllPossibleActions(this.ALL_POSSIBLE_EFFECTS, this.ALL_IMMEDIATE_HEALTH_CHANGES);
        this.ALL_POSSIBLE_DINOSAURS = new AllPossibleDinosaurs(this.ALL_POSSIBLE_ACTIONS);
        this.ALL_POSSIBLE_TEAMMATES = new AllPossibleTeammates(this.ALL_POSSIBLE_ACTIONS);
        this.ALL_POSSIBLE_ITEMS = new AllPossibleItems(this.ALL_IMMEDIATE_HEALTH_CHANGES, this.ALL_POSSIBLE_EFFECTS);
    }

    public AllImmediateHealthChanges getALL_IMMEDIATE_HEALTH_CHANGES() {return this.ALL_IMMEDIATE_HEALTH_CHANGES;}
    public AllPossibleActions getALL_POSSIBLE_ACTIONS() {return this.ALL_POSSIBLE_ACTIONS;}
    public AllPossibleDinosaurs getALL_POSSIBLE_DINOSAURS() {return this.ALL_POSSIBLE_DINOSAURS;}
    public AllPossibleEffects getALL_POSSIBLE_EFFECTS() {return this.ALL_POSSIBLE_EFFECTS;}
    public AllPossibleItems getALL_POSSIBLE_ITEMS() {return this.ALL_POSSIBLE_ITEMS;}
    public AllPossibleTeammates getALL_POSSIBLE_TEAMMATES() {return this.ALL_POSSIBLE_TEAMMATES;}

    private final AllImmediateHealthChanges ALL_IMMEDIATE_HEALTH_CHANGES;
    private final AllPossibleActions ALL_POSSIBLE_ACTIONS;
    private final AllPossibleTeammates ALL_POSSIBLE_TEAMMATES;
    private final AllPossibleItems ALL_POSSIBLE_ITEMS;
    private final AllPossibleEffects ALL_POSSIBLE_EFFECTS;
    private final AllPossibleDinosaurs ALL_POSSIBLE_DINOSAURS;
}
