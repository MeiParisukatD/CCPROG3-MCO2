//PlayableChar subclass
package Character_classes;

import Item_classes.*;
import java.util.ArrayList;

public class PlayableChar extends GameCharacter {
    //attributes
    private int goldOwned;
    private float curHealth;
    private int turnCount;
    private ArrayList<Item> inventory;
    private Item curItem;

    //constructor
    public PlayableChar(String name, float health, float attack, String dialogue) {
        super(name, health, attack, dialogue);
        this.goldOwned = 0;
        this.turnCount = 0;
        this.curHealth = health;
        this.inventory = new ArrayList<Item>();
        this.curItem = null;
    }

    //getters/setters
    public int getGoldOwned() {
        return this.goldOwned;
    }

    public void setGoldOwned(int goldOwned) {
        this.goldOwned = goldOwned;
    }

    public float getCurHealth() {
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
        return false;
    }

    public boolean useItem() {
        //TODO
         return false;
    }

    public boolean buyItem(Item purchase) {
        //TODO
         return false;
    }

    public boolean discardItem(int index) {
        //TODO
         return false;
    }

    public boolean heal(float amount) {
        //TODO
         return false;
    }
}