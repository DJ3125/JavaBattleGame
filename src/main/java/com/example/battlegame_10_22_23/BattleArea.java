package com.example.battlegame_10_22_23;

import java.util.ArrayList;

public class BattleArea {
    //getMethods. Pretty self-explanatory
    public BattleEntities getBattleEntity(boolean myTeam, int index){
        if(myTeam){return this.playerTeam.get(index);}
        else{return this.opponentTeam.get(index);}
    }
    public ArrayList<BattleEntities> getPlayerTeam() {return this.playerTeam;}
    public ArrayList<BattleEntities> getOpponentTeam() {return this.opponentTeam;}
    public ArrayList<Items> getPlayerItems(){return this.playerItems;}

    public PlayerInformation getPlayerInformation() {return this.playerInformation;}

    public boolean isMyTurn() {return this.myTurn;}

    //Constructor. Takes in 2 ArrayLists consisting of BattleEntities.
    public BattleArea(ArrayList<BattleEntities> playerTeam, ArrayList<BattleEntities> opponentTeam, PlayerInformation playerInformation){
        this.playerTeam = playerTeam;
        this.opponentTeam = opponentTeam;
        this.myTurn = true;
        this.playerItems = playerInformation.getItemsOwned();
        this.playerInformation = playerInformation;
        for(BattleEntities i : this.playerTeam){
            i.setPerformedAction(false);
            i.setActionCooldownsToZero();
        }
        for(BattleEntities i : this.opponentTeam){
            i.setPerformedAction(false);
            i.setActionCooldownsToZero();
        }
    }

    /*
    Requires no explicit parameter
    Returns a value corresponding to the result of the battle, if there is one
    Purpose: To get the current status of the battle
     */
    public byte checkForEndOfBattle(){//-1 means stalemate, 0 means continuous battle, 1 means player won, 2 means opponent won
        boolean isPlayerTeamFinished = true;
        byte interval = 0;
        while(isPlayerTeamFinished && interval < playerTeam.size()){ //Sets the isPlayerTeamFinished to true, and if there's an entity that didn't go yet, the loop terminates and the boolean value is set to false
            if(playerTeam.get(interval).getCurrentHealth() > 0){isPlayerTeamFinished = false;}
            interval++;
        }
        interval = 0;
        boolean isOpponentTeamFinished = true;
        while(isOpponentTeamFinished && interval < opponentTeam.size()){ //sets the isOpponentTeamFinished to true, and if there's an entity in the team that didn't go yet, the loop terminates and the boolean value is sent to false
            if(opponentTeam.get(interval).getCurrentHealth() > 0){isOpponentTeamFinished = false;}
            interval++;
        }

        //Based on the values above, a result is returned. Look above for the results
        if(isOpponentTeamFinished != isPlayerTeamFinished){
            if(isOpponentTeamFinished){return 1;}
            else{return 2;}
        }else{
            if(isOpponentTeamFinished){return -1;}
            else{return 0;}
        }
    }

    /*
    Parameters: Requires an Action to determine what action is being performed. 3 Battle entities are also required, representing an attacker, and 2 others representing targets.
        2 targets are required because Health changes and effects can either affect the player's or opponent's team. If the effect/health change doesn't target the group, a specific
        target is required. As a result, there are 2 targets, each corresponding to the player's or opponent's team.

    Return: Has no return value
    Purpose: Performs the specific action, applying the effects associated with that action on to their targets, and also changes the health of the target(s)
     */
    public void performActions(BattleEntities attacker, Actions actionSelected, BattleEntities myTeamTarget, BattleEntities opponentTeamTarget){
        actionSelected.resetCoolDown(); //Resets the cool-down of the specified action, so it can't select that option next time
        actionSelected.setRecentlySelected(true); //Marks this action, so that it won't decrement when this turn ends
        this.applyEffectsAndHealthChanges(actionSelected.getActionsReference().getAssociatedEffects(), actionSelected.getActionsReference().getAssociatedHealthChanges(), myTeamTarget, opponentTeamTarget, attacker);
    }


    /*
    Parameters: Requires no explicit parameters
    Return: returns a true or false value depending on if it was the end of the turn or not.
    Purpose: Causes the effects to be applied onto the entity, and decrements the amount of turns remaining. If there are no more turns remaining on the effect, then the effect is removed
     */
    public boolean manageTurnSwitching(){
        ArrayList<BattleEntities> teamSelected = (myTurn) ? this.playerTeam : this.opponentTeam; //Gets the team selected based on if it's the players turn or not. If it's the players turn, it will update their team and then switch turns. Same with the opponent
        for (BattleEntities battleEntities : teamSelected){if (!battleEntities.isPerformedAction() && battleEntities.getCurrentHealth() > 0) {return false;}}//Checks if all the players have gone or not. If not, then it returns false, describing that there's still actions the players can make. If it doesn't return false, then the code below this segment is run
        for(BattleEntities i : teamSelected){//Loops through the team selected and updates their health and effects
            i.changeHealthDueToEffects();//Changes the health of the entity based on the effects attached on it
            for(Actions j : i.getBattleActions()){j.decrementCoolDown();}//Loops through all the actions, and decrements the cool-down for each action
            byte interval = 0;
            while(i.getEffectsOnEntity() != null && interval < i.getEffectsOnEntity().size()){//loops through the effects on the entity, and decrements each one because their turn has passed. After the decrement, if the amount of turns remaining on the effect is 0, then it removes that effect
                i.getEffectsOnEntity().get(interval).decrementTurnsRemaining();
                if(i.getEffectsOnEntity().get(interval).getTurnsRemaining() == 0){i.getEffectsOnEntity().remove(interval);}
                else{interval++;}
            }
        }
        this.myTurn = !this.myTurn;//Switches the turns
        for(BattleEntities i : (this.myTurn) ? this.playerTeam : this.opponentTeam){i.setPerformedAction(false);}
        return true; //Returns true to describe that this team is finished
    }

    /*
    Parameters: Requires an ArrayList of Effects and ImmediateHealthChanges to apply onto specific entities, and requires 2 entities to represent the target and the person performing the action
    Return:  Has no return value
    Purpose: Applies effects and health changes onto the specified entities
     */
    private void applyEffectsAndHealthChanges(ArrayList<EffectsReference> effectsToApply, ArrayList<ImmediateHealthChanges> healthChangesToApply, BattleEntities myTeamTarget, BattleEntities opponentTeamTarget, BattleEntities attacker){
        attacker.setPerformedAction(true); //Marks the specified entity as performed the action, so it can't go again
        if(healthChangesToApply != null){
            for(ImmediateHealthChanges i : healthChangesToApply){//Changes the health of the specified targets by a certain amount
                if(i.doesTargetGroup()){for(BattleEntities j : (this.myTurn == i.doesTargetOpponent()) ? this.opponentTeam : this.playerTeam){i.setChangeInHealth(j, attacker);}}
                else{i.setChangeInHealth(((i.doesTargetOpponent() == this.myTurn) ? opponentTeamTarget : myTeamTarget), attacker);} //If the Immediate Health Change does not target the opponent, it targets the entity that is performing the action
            }
        }
        if(effectsToApply != null){
            for(EffectsReference i : effectsToApply){//Applies effects onto the specified targets if they are not immune
                if(i.doesAffectGroup()){for(BattleEntities j : ((this.myTurn == i.doesTargetOpponent()) ? this.opponentTeam : this.playerTeam)){if(!j.isImmune(i)){j.getEffectsOnEntity().add(new Effects(i));}}}
                else if(!(((i.doesTargetOpponent() == this.myTurn) ? opponentTeamTarget : myTeamTarget).isImmune(i))){((i.doesTargetOpponent() == this.myTurn) ? opponentTeamTarget : myTeamTarget).getEffectsOnEntity().add(new Effects(i));} //If the effect does not target the opponent, it targets the person performing the action
            }
        }
    }

    /*
    Parameters: Requires An ItemReference to get the item that the player wants to use, and requires 3 Battle Entities to represent the entity performing the action and the 2 targets. For more info on why there's two targets, check the method above
    return: Returns a boolean value to represent if the item is used successfully
    Purpose: Uses the specified item and applies it ont the targets
     */
    public boolean useItem(ItemReference itemToUse, BattleEntities myTeamTarget, BattleEntities myOpponentTarget, BattleEntities itemUser){
        for(Items j : this.playerItems){
            if(j.getItemReference().equals(itemToUse)){
                j.changeQuantity(-1);
                this.applyEffectsAndHealthChanges(itemToUse.getEffectsAssociated(), itemToUse.getImmediateHealthChangesAssociated(), myTeamTarget, myOpponentTarget, itemUser);

                //Removes all the empty items
                this.playerInformation.removeEmptyItems();
                return true;
            }
        }
        return false;
    }

    public void clearAllEffects(){for(BattleEntities i : this.getPlayerTeam()){i.getEffectsOnEntity ().clear();}}//Removes all the effects on the player's team


    //Private values containing all the data of the battle
    private ArrayList<BattleEntities> playerTeam, opponentTeam;
    private ArrayList<Items> playerItems;
    private PlayerInformation playerInformation;
    private boolean myTurn;
}