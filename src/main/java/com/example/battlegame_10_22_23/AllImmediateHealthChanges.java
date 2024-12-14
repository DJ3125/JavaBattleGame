package com.example.battlegame_10_22_23;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AllImmediateHealthChanges {

    public AllImmediateHealthChanges(){
        this.MINOR_HEAL = new HealingHealthChanges(0.1, false, true);
        this.MAJOR_HEAL = new HealingHealthChanges(0.33, false, true);
        this.STRIKE = new AttackHealthChange(1, false);
        this.IMPACT = new AttackHealthChange(1.5, false);
        this.RAMPAGE = new AttackHealthChange(2, false);
    }

    public ArrayList<ImmediateHealthChanges> getAllPossibleHealthChanges(){
        ArrayList<ImmediateHealthChanges> allHealthChanges = new ArrayList<>();
        for(Field i : AllImmediateHealthChanges.class.getDeclaredFields()){
            try{if(i.get(this) instanceof ImmediateHealthChanges){allHealthChanges.add((ImmediateHealthChanges) (i.get(this)));}}
            catch(IllegalAccessException error){error.printStackTrace();}
        }
        return allHealthChanges;
    }

    public ImmediateHealthChanges getRAMPAGE() {return this.RAMPAGE;}
    public ImmediateHealthChanges getIMPACT() {return this.IMPACT;}
    public ImmediateHealthChanges getMAJOR_HEAL() {return MAJOR_HEAL;}
    public ImmediateHealthChanges getSTRIKE() {return this.STRIKE;}
    public ImmediateHealthChanges getMINOR_HEAL() {return this.MINOR_HEAL;}

    private ImmediateHealthChanges MINOR_HEAL, MAJOR_HEAL, STRIKE, IMPACT, RAMPAGE;
}
