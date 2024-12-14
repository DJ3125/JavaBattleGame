package com.example.battlegame_10_22_23;

public class Actions {
    /*
    Get methods. Pretty self-explanatory
     */
    public int getCurrentCoolDown(){return this.currentCoolDown;}
    public boolean isRecentlySelected() {return this.recentlySelected;}
    public ActionsReference getActionsReference() {return this.actionsReference;}

    /*
        Constructor: Takes in an integer to represent the maximum cool-down, a String to represent the name, and two ArrayLists to represent the effects associated and the immediate health changes associated
         */
    public Actions(ActionsReference actionsReference){
        this.currentCoolDown = 0;
        this.recentlySelected = false;
        this.actionsReference = actionsReference;
    }

    //Set methods. The first two change the value of the cool-down. The last one denotes if this action was recently selected or not
    public void decrementCoolDown(){if(this.currentCoolDown > 0 && !this.recentlySelected){this.currentCoolDown--;}} //If the action has a cool-down of greater than 0 and if it wasn't recently selected, the cool-down is decremented
    public void resetCoolDown(){this.currentCoolDown = this.actionsReference.getMaxCoolDown();}
    public void setCooldownToZero(){this.currentCoolDown = 0;}
    public void setRecentlySelected(boolean recentlySelected) {this.recentlySelected = recentlySelected;}

    //Private attributes
    private int currentCoolDown;
    private boolean recentlySelected;
    private final ActionsReference actionsReference;
}
