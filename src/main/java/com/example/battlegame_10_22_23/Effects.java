package com.example.battlegame_10_22_23;

public class Effects{
    //Get methods, pretty self-explanatory
    public int getTurnsRemaining() {return this.turnsRemaining;}
    public EffectsReference getEffectsReference(){return this.effectsReference;}

    //Constructors
    public Effects(EffectsReference effectsReference){
        this.effectsReference = effectsReference;
        this.turnsRemaining = this.effectsReference.getInitialTurnsRemaining();
    }

    public Effects(EffectsReference effectsReference, int turnsRemaining){
        this.effectsReference = effectsReference;
        this.turnsRemaining = turnsRemaining;
    }

    //Private attributes
    public void decrementTurnsRemaining(){this.turnsRemaining--;}
    private int turnsRemaining;
    private EffectsReference effectsReference;
}
