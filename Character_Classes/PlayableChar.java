//PlayableChar subclass
package Character_classes;
import java.util.ArrayList;

public class PlayableChar extends Character {
    //attributes
    private int goldOwned;
    private int curHealth;
    private int turnCount;
    private ArrayList<Item> inventory;
    private Item curItem;

    //constructor
    public PlayableChar(String name, int health, int attack, String dialogue) {
        super(name, health, attack, dialogue);
        this.goldOwned = this.turnCount = 0;
        this.curHealth = health;
        this.inventory = new ArrayList<Item>();
        this.curItem = new Item();
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

    public void setInventory(Item[] inventory) {
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
}