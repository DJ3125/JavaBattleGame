package com.example.battlegame_10_22_23;

public class Items {
    //Get methods, pretty self-explanatory
    public int getQuantity(){return this.quantity;}
    public ItemReference getItemReference() {return this.itemReference;}

    //Set Methods, pretty self-explanatory
    public void setQuantity(int newQuantity){this.quantity = newQuantity;}
    public double changeQuantity(int amountChange){//Adds a certain amount of items. It may return the price depending on if its specified or not
        this.quantity += amountChange;
        return this.itemReference.getPrice() * amountChange;
    }

    //Constructors
    public Items(ItemReference itemReference, int quantity){
        this.itemReference = itemReference;
        this.quantity = quantity;
    }

    //private attributes
    private int quantity;
    private ItemReference itemReference;
}
