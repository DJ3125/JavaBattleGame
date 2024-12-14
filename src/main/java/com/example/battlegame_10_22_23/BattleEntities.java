package com.example.battlegame_10_22_23;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class BattleEntities {
    /*Methods changing the health of the entity*/
    public void addHealth(int healthIncrease){
        this.currentHealth += healthIncrease;
        if(this.currentHealth > this.baseHealth){this.currentHealth = this.baseHealth;}
    }
    public void subtractHealth(int healthDecrease){
        this.currentHealth -= healthDecrease;
        if(this.currentHealth < 0){this.currentHealth = 0;}
    }
    public void changeHealthDueToEffects(){if(this.effectsOnEntity != null){for(Effects i : this.effectsOnEntity){if(!(i.getEffectsReference() instanceof HealthChanceChangeEffect)){this.subtractHealth((int)(this.baseHealth * (i.getEffectsReference().getHealthChange())));}}}}
    public double getDefenseMultiplier(){
        double healthChange = 1;
        if(this.effectsOnEntity != null){
            for(Effects i : this.effectsOnEntity){
                if(i.getEffectsReference() instanceof HealthChanceChangeEffect){
                    healthChange *= i.getEffectsReference().getHealthChange();
                }
            }
        }
        if(healthChange == 1){return 1.0;}
        else{return 1.0 - healthChange;}
    }
    public void setCurrentHealth(int newHealthValue){this.currentHealth = newHealthValue;}

    //Set methods
    public void setTrainingLevel(byte newLevel){this.trainingLevel = newLevel;}
    public void setName(String name) {this.name = name;}

    public void setPictureLink(String pictureLink) {this.pictureLink = pictureLink;}

    public void setBaseAttack(int newBaseAttack) {this.baseAttack = newBaseAttack;}
    public void setBaseHealth(int newBaseHealth){this.baseHealth = newBaseHealth;}
    public void setActionCooldownsToZero(){for(Actions i : this.getBattleActions()){i.setCooldownToZero();}}
    public void incrementTrainingLevel(){this.trainingLevel++;}
    public void setPerformedAction(boolean performedAction){this.performedAction = performedAction;}

    public void setInformationFromSave(String line, Field field, AllReferences allReferences){
        if(field.getType() != ArrayList.class && !field.getName().equals("performedAction")){
            try{
                if(field.getType() == String.class){field.set(this, line);}
                else if(field.getType() == int.class){field.set(this, Integer.parseInt(line));}
                else{
                    String className = field.getType().toString().substring(0, 1).toUpperCase().concat(field.getType().toString().substring(1)).trim();
                    field.set(this, Class.forName("java.lang.".concat(className)).getMethod("parse" + className, String.class).invoke(null, line));
                }
            }catch(IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException error){error.printStackTrace();}
        }else if(field.getType() == ArrayList.class){
            try{
                if(field.getName().equals("immunities")){
                    byte interval = 0;
                    ArrayList<BattleEntities> battleEntities = allReferences.getALL_POSSIBLE_TEAMMATES().getAllPossibleEmployees();
                    while(interval < battleEntities.size() && !battleEntities.get(interval).getName().equals(this.name)){interval++;}
                    field.set(this, battleEntities.get(interval).getImmunities());
                }else if(field.getName().equals("effectsOnEntity")){
                    ArrayList<EffectsReference> effects = allReferences.getALL_POSSIBLE_EFFECTS().getAllEffects();
                    ArrayList<Effects> effectsOnEntity = new ArrayList<>();
                    for(String i : line.split(";")){if(!i.isEmpty()){effectsOnEntity.add(new Effects(effects.get(Integer.parseInt(i.split(",")[0])), Integer.parseInt(i.split(",")[1])));}}
                    field.set(this, effectsOnEntity);
                }else if(field.getName().equals("battleActions")){
                    ArrayList<ActionsReference> actions = allReferences.getALL_POSSIBLE_ACTIONS().getAllActionReferences();
                    ArrayList<Actions> entityActions = new ArrayList<>();
                    for(String i : line.split(",")){if(!i.isEmpty()){entityActions.add(new Actions(actions.get(Integer.parseInt(i))));}}
                    field.set(this, entityActions);
                }
            }catch (IllegalAccessException error){error.printStackTrace();}
        }
    }

    /*Get methods. Pretty self-explanatory*/
    public int getBaseHealth(){return this.baseHealth;}
    public int changeAttackDueToEffects(){
        double newAttack = 1;
        for(Effects i : this.effectsOnEntity){newAttack *= (1 - i.getEffectsReference().getAttackChange());} //Combines all the effects together and gets the attack after all the effects are on it. Does a 1 - attackChange because if an effect reduces the attack by "x" amount, then we multiply by 1-x to get the result
        System.out.println(newAttack);
        return (int)(this.baseAttack * newAttack);
    }
    public int getCurrentHealth(){return this.currentHealth;}
    public int getBaseAttack(){return this.baseAttack;}
    public ArrayList<Class<? extends EffectsReference>> getImmunities(){return this.immunities;}
    public ArrayList<Effects> getEffectsOnEntity(){return this.effectsOnEntity;}
    public int getPower(){return this.baseAttack + this.baseHealth;}
    public ArrayList<Actions> getBattleActions(){return this.battleActions;}
    public boolean isPerformedAction() {return this.performedAction;}
    public String getName(){return this.name;}
    public String getPictureLink(){return this.pictureLink;}
    public String getSavingInformation(AllReferences allReferences){
        String txt = "";
        for(Field i : BattleEntities.class.getDeclaredFields()){
            if(i.getType() != ArrayList.class && !i.getName().equals("performedAction")){
                try{txt = txt.concat(i.get(this) + "\n");}
                catch(IllegalAccessException error){error.printStackTrace();}
            }else if(i.getType() == ArrayList.class){
                if(i.getName().equals("immunities") && this.immunities != null){for(Class<? extends EffectsReference> j : this.immunities){txt = txt.concat(j.toString().substring(6));}}
                else if(i.getName().equals("effectsOnEntity")){
                    ArrayList<EffectsReference> effectsReferences = allReferences.getALL_POSSIBLE_EFFECTS().getAllEffects();
                    if(this.effectsOnEntity != null){
                        for(Effects j : this.effectsOnEntity){
                            byte interval = 0;
                            while(interval < effectsReferences.size() && j.getEffectsReference() != effectsReferences.get(interval)){interval++;}
                            txt = txt.concat(interval + "," + j.getTurnsRemaining() + ";");
                        }
                    }
                }else if(i.getName().equals("battleActions")){
                    ArrayList<ActionsReference> actionsReferences = allReferences.getALL_POSSIBLE_ACTIONS().getAllActionReferences();
                    for(Actions j : this.getBattleActions()){
                        byte interval = 0;
                        while(interval < actionsReferences.size() && !j.getActionsReference().getName().equals(actionsReferences.get(interval).getName())){interval++;}
                        txt = txt.concat(interval + ",");
                    }
                }
                if(!i.getName().equals("battleActions")){txt = txt.concat("\n");}
            }
        }
        return txt;
    }

    public boolean isImmune(EffectsReference effectApplied){
        if(effectApplied.isNegativeEffect() && this.immunities != null){
            for(Class<? extends EffectsReference> i : this.immunities){
                Class<?> effectSuperClass = effectApplied.getClass();
                while(effectSuperClass != Object.class){
                    if(effectSuperClass == i){return true;}
                    effectSuperClass = effectSuperClass.getSuperclass();
                }
            }
        }
        return false;
    }

    public BattleEntities getClone(){
        BattleEntities clone = new BattleEntities();
        clone.setBaseAttack(this.getBaseAttack());
        clone.setBaseHealth(this.getBaseHealth());
        clone.setName(this.getName());
        clone.setPictureLink(this.getPictureLink());
        clone.setCurrentHealth(this.getCurrentHealth());
        if(this.immunities != null){
            clone.getImmunities().clear();
            for(Class<? extends EffectsReference> i : this.getImmunities()){clone.getImmunities().add(i);}
        }
        clone.battleActions = new ArrayList<>();
        clone.effectsOnEntity = new ArrayList<>();
        for(Actions i : this.battleActions){clone.getBattleActions().add(new Actions(i.getActionsReference()));}
        return clone;
    }

    public int getTrainingLevel() {return this.trainingLevel;}
    /*Constructors. Requires at the minimum the base health and attack, the actions, the name, and a string representing the picture*/
    public BattleEntities(int attack, int health, ArrayList<ActionsReference> actions, String name, String pictureLink){
        this.baseAttack = attack;
        this.baseHealth = health;
        this.currentHealth = this.baseHealth;
        this.battleActions = new ArrayList<>();
        for(ActionsReference i : actions){this.battleActions.add(new Actions(i));}
        this.performedAction = false;
        this.name = name;
        this.pictureLink = pictureLink;
        this.trainingLevel = 1;
        this.effectsOnEntity = new ArrayList<>();
    }

    /*Private attributes*/
    public BattleEntities(int attack, int health, ArrayList<ActionsReference> actions, String name, String pictureLink, ArrayList<Class<? extends EffectsReference>> immunities){
        this(attack, health, actions, name, pictureLink);
        this.immunities = immunities;
    }

    public BattleEntities(){
        this.name = "";
        this.pictureLink = "";
        this.baseHealth = 0;
        this.baseAttack = 0;
        this.currentHealth = 0;
        this.trainingLevel = 0;
        this.immunities = null;
        this.effectsOnEntity = null;
        this.battleActions = null;
    }

    private String name, pictureLink;
    private int baseHealth, baseAttack, currentHealth; //Represents the attack and health of the entity before effects are applied (aka at the beginning of the battle)
    private byte trainingLevel;
    private ArrayList<Class<? extends EffectsReference>> immunities; //Represents all the different negative effects that cannot be applied to the entity
    private ArrayList<Effects> effectsOnEntity; //Represents all the different effects (can be positive or negative) that are applied to the entity
    private ArrayList<Actions> battleActions;//Represents all the actions the entity can make
    private boolean performedAction;
}