//PlayableChar subclass
package Character_classes;

import Item_classes.*;
import Dungeon_classes.*;
import java.util.ArrayList;

public class PlayableChar extends GameCharacter {
    //attributes
    private int goldOwned;
    private float maxHealth;
    private int turnCount;
    private ArrayList<Item> inventory;
    private Item curItem;

    //constructor
    public PlayableChar(String name, float health, float attack, String dialogue) {
        super(name, health, attack, dialogue);
        this.goldOwned = 0;
        this.turnCount = 0;
        this.maxHealth = health;
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

    public float getMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
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
    public void incrementTurn() {
        this.turnCount++;
    }

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

    public void dig(Tile tile, Floor floor) {
        floor.destroyTile(tile);
    }

    public void move(char direction, Floor floor) {
        int d = -1;
        Tile next;

        //adjusting direction to numerical value
        switch (direction) {
            case 'w': d = 0; break;
            case 's': d = 1; break;
            case 'a': d = 2; break;
            case 'd': d = 3; break;
        }

        next = nextTile(d, floor);
        super.move(d, floor);
        this.takeDmg(next.getDamage());

        //if the destination tile is destructible, dig the tile
        if(next instanceof DestructibleTile) {
            this.dig(next, floor);
        }
    }

    public void findCharTile(Tile[][] map) {
        int i, j;
        char key;

        switch (this.name) {
            case "Yohane": key = 'Y'; break;
            case "Lailaps": key = 'L'; break;
            case "Bat": key = 'b'; break;
            case "Siren": key = 'S'; break;
            default: key = ' '; break;
        }

        for (i = 0; i < map.length; i++) {
            for (j = 0; j < map[i].length; j++) {
                if (map[i][j].getSymbol() == key) {
                    this.tile = map[i][j];
                }
            }
        }
    }
}