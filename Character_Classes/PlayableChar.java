//PlayableChar subclass
package Character_Classes;

import Item_Classes.*;
import Dungeon_Classes.*;
import java.util.ArrayList;

public class PlayableChar extends GameCharacter {
    //attributes
    private int goldOwned;
    private float maxHealth;
    private int turnCount;
    private ArrayList<Item> inventory;
    private Item curItem;
    private String causeOfDeath;

    //constructor
    public PlayableChar(String name, float health, float attack, String dialogue) {
        super(name, health, attack, dialogue);
        this.goldOwned = 0;
        this.turnCount = 0;
        this.maxHealth = health;
        this.inventory = new ArrayList<Item>();
        this.curItem = null;
        this.causeOfDeath = null;
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

    public void addItem(Item i) {
        this.inventory.add(i);
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

    public String getCauseOfDeath() {
        return this.causeOfDeath;
    }

    //additional methods
    public void incrementTurn() {
        this.turnCount++;
    }

    public void takeDmg(GameCharacter entity) {
        super.takeDmg(entity.getAttack());

        //if character dies from taking damage
        if (this.charDeath()) {
            this.causeOfDeath = entity.getName();
        }
    }

    public boolean prevItem() {
        if (inventory.size() <= 1) {
            return false;
        }

        int index = inventory.indexOf(curItem);

        index--;

        if (index < 0) {
            index = inventory.size() - 1;
        }

        curItem = inventory.get(index);

        return true;
    }

    public boolean nextItem() {
        if (inventory.size() <= 1) {
            return false;
        }

        int index = inventory.indexOf(curItem);

        index++;

        if (index >= inventory.size()) {
            index = 0;
        }

        curItem = inventory.get(index);

        return true;
    }

    public boolean useItem() {
        if (curItem == null) {
            return false;
        }

        boolean used = curItem.use(this);

        if (used) {
            inventory.remove(curItem);

            if (inventory.isEmpty()) {
                curItem = null;
            }
            else {
                curItem = inventory.get(0);
            }

            incrementTurn();
        }

        return used;
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
        if (amount <= 0) {
            return false;
        }

        float newHealth = this.health + amount;

        if (newHealth > maxHealth) {
            newHealth = maxHealth;
        }

        this.health = newHealth;

        return true;
    }

    public void dig(Tile tile, Floor floor) {
        floor.destroyTile(tile);
    }

    public void collect(DestructibleTile tile, Floor floor) {
        if (tile.getSymbol() == 'I') {
            this.inventory.add(tile.getItemDrop());
            
            //if this is the first item the character receives
            if (this.inventory.size() == 1) {
                this.curItem = this.inventory.get(0);
            }
        } else { //if the tile is a gold tile
            this.goldOwned += tile.getGoldDrop();
        }
        //destroy tile after collected
        floor.destroyTile(tile);
    }

    public void openTreasure(DestructibleTile tile) {
        tile.dropTreasure();
    }

    public void move(char direction, Floor floor) {
        int d = -1; //assigned sentinel value to accomodate compilation
        Tile next;

        //adjusting direction to numerical value
        switch (direction) {
            case 'w': d = 0; break;
            case 's': d = 1; break;
            case 'a': d = 2; break;
            case 'd': d = 3; break;
        }

        next = nextTile(d, floor);

        //check for enemy
        EnemyChar enemy = floor.findEnemy(next.getX(), next.getY());
        if (enemy != null) {
            this.dealDmg(enemy);
        } 
        //if next tile is not an entity
        else {
            super.move(d, floor);
            this.takeDmg(next.getDamage()); //receives damage from tile

            //if character dies from tile damage 
            if (this.charDeath()) {
                this.causeOfDeath = (next.getSymbol() == 'h') ? "Heat Tiles" : "Spike Walls";
            } 
            else {
                //if the destination tile is destructible, 
                if(next.isDestructible()) {
                    DestructibleTile dTile = (DestructibleTile) next;

                    //if next tile is a collectible
                    if (next.getSymbol() == 'I' || next.getSymbol() == 'g') {
                        this.collect(dTile, floor);
                    }
                    //if next tile is a treasure tile
                    else if (next.getSymbol() == 'T') {
                        this.openTreasure(dTile);
                    }
                    //if next tile can be dug
                    else {
                        this.dig(next, floor);
                    }
                }
            }
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