//PlayableChar subclass
package Character_classes;
import java.util.ArrayList;

import Item_Classes.Item;

public class PlayableChar extends Character {
    //attributes
    private int goldOwned;
    private int curHealth;
    private int turnCount;
    private ArrayList<Item> inventory;

    //attributes with items
    private Item curItem;
    private boolean walkWater;
    private boolean heatImmune;
    private boolean spikeImmune;
    private boolean batDamageReduction;
    private boolean hasIceCream;

    //constructor
    public PlayableChar(String name, int health, int attack, String dialogue) {
        super(name, health, attack, dialogue);
        this.goldOwned = 0;
        this.turnCount = 0;
        this.curHealth = health;
        this.inventory = new ArrayList<Item>();
        this.curItem = null;
        this.walkWater = false;
        this.heatImmune = false;
        this.spikeImmune = false;
        this.batDamageReduction = false;
        this.hasIceCream = false;
    }

    //getters/setters
    public int getGoldOwned() {
        return this.goldOwned;
    }

    public void setGoldOwned(int goldOwned) {
        this.goldOwned = goldOwned;
    }

    public int getCurHealth() {
        return this.curHealth;
    }

    public void setCurHealth(int curHealth) {
        this.curHealth = curHealth;
    }

    public int getTurnCount() {
        return this.turnCount;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public ArrayList<Item> getInventory() {
        return this.inventory;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public Item getCurItem() {
        return this.curItem;
    }

    public void setCurItem(Item curItem) {
        this.curItem = curItem;
    }

    //additional methods
    public boolean switchItem(int index) {
        //TODO
    }

    public boolean useItem() {
        //TODO
    }

    public boolean buyItem(Item purchase) {
        //TODO
    }

    public boolean discardItem(int index) {
        //TODO
    }

    public boolean heal(float amount) {
        //TODO
    }

    public boolean canWalkWater() {
        return walkWater;
    }

    public void setWalkWater(boolean walkWater) {
        this.walkWater = walkWater;
    }

    public boolean isHeatImmune() {
        return heatImmune;
    }

    public void setHeatImmune(boolean heatImmune) {
        this.heatImmune = heatImmune;
    }

    public boolean isSpikeImmune() {
        return spikeImmune;
    }

    public void setSpikeImmune(boolean spikeImmune) {
        this.spikeImmune = spikeImmune;
    }

    public boolean hasBatDamageReduction() {
        return batDamageReduction;
    }

    public void setBatDamageReduction(boolean batDamageReduction) {
        this.batDamageReduction = batDamageReduction;
    }

    public boolean hasIceCream() {
        return hasIceCream;
    }

    public void setHasIceCream(boolean hasIceCream) {
        this.hasIceCream = hasIceCream;
    }
}