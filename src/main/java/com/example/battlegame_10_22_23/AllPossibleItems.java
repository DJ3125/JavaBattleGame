package com.example.battlegame_10_22_23;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AllPossibleItems {
    public AllPossibleItems(AllImmediateHealthChanges allImmediateHealthChanges, AllPossibleEffects allPossibleEffects){
        this.HEALTH_KIT = new ItemReference("Health Kit", 20.99, null, new ArrayList<>());
        this.HEALTH_KIT.getImmediateHealthChangesAssociated().add(allImmediateHealthChanges.getMAJOR_HEAL());
        this.VENOM_SYRINGE = new ItemReference("Venom Syringe", 44.99, new ArrayList<>(), null);
        this.VENOM_SYRINGE.getEffectsAssociated().add(allPossibleEffects.getMINOR_BLEED());
    }

    public ArrayList<ItemReference> getAllItemReferences(){
        ArrayList<ItemReference> allItemReferences = new ArrayList<>();
        for(Field i : AllPossibleItems.class.getDeclaredFields()){
            try{if(i.get(this) instanceof ItemReference){allItemReferences.add((ItemReference) (i.get(this)));}}
            catch(IllegalAccessException error){error.printStackTrace();}
        }
        return allItemReferences;
    }

    public ItemReference getHEALTH_KIT() {return this.HEALTH_KIT;}
    public ItemReference getVENOM_SYRINGE() {return this.VENOM_SYRINGE;}

    private final ItemReference HEALTH_KIT, VENOM_SYRINGE;
}
