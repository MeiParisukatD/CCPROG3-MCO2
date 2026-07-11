//PlayableChar subclass
package Character_Classes;

import Item_Classes.*;
import Dungeon_Classes.*;
import java.util.ArrayList;

/**
 * Represents a user-controlled character within the game.
 * Tracks character statistics including inventory management, currency tracking, 
 * step counts, item usage, tile interactions, and dynamic combat outcomes.
 * 
 * @author Katigbak and Porciuncula
 * @version 1.0
 */
public class PlayableChar extends GameCharacter {
    //attributes
    /** The amount of gold currency accumulated by the character. */
    private int goldOwned;
    /** The maximum health points capacity this character can possess. */
    private float maxHealth;
    /** The running log of turns or actions executed by the character. */
    private int turnCount;
    /** The storage bag of items currently held by the character. */
    private ArrayList<Item> inventory;
    /** The item currently selected or equipped from the inventory layout. */
    private Item curItem;
    /** The source of damage or hazard that caused the character to lose all health. */
    private String causeOfDeath;

    //constructor
    /**
     * Constructs a playable character with defined statistics and resets 
     * status variables to their starting configurations.
     *
     * @param name     the identifier name of the playable character
     * @param health   the starting health and ceiling threshold capacity
     * @param attack   the base attack damage capacity
     * @param dialogue the interaction line text
     */
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
    /**
     * @return the total amount of gold owned
     */
    public int getGoldOwned() {
        return this.goldOwned;
    }

    /**
     * @param goldOwned the updated gold value to assign
     */
    public void setGoldOwned(int goldOwned) {
        this.goldOwned = goldOwned;
    }

    /**
     * @return the upper limit ceiling of character health
     */
    public float getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * @param maxHealth the new upper threshold for health
     */
    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * @return the running record tracking total moves executed
     */
    public int getTurnCount() {
        return this.turnCount;
    }

    /**
     * @param turnCount the custom integer turn step index
     */
    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    /**
     * Appends an item to the character's active inventory array.
     *
     * @param i the Item asset to insert
     */
    public void addItem(Item i) {
        this.inventory.add(i);
    }

    /**
     * Retrieves the character's entire inventory.
     * 
     * @return the full list structure tracking held items
     */
    public ArrayList<Item> getInventory() {
        return this.inventory;
    }

    /**
     * Gets the item that is currently selected or focused for the character's next action.
     * 
     * @return the item currently targeted for upcoming action steps
     */
    public Item getCurItem() {
        return this.curItem;
    }

    /**
     * Updates the currently selected item to a new target item.
     * 
     * @param curItem the item node to swap selection toward
     */
    public void setCurItem(Item curItem) {
        this.curItem = curItem;
    }

    /**
     * Retrieves the explanation or name of the entity/hazard that defeated the character.
     *
     * @return the identifier string explaining what defeated the character
     */
    public String getCauseOfDeath() {
        return this.causeOfDeath;
    }

    //additional methods
    /**
     * Increments the total running turn engine index step count by one.
     */
    public void incrementTurn() {
        this.turnCount++;
    }

    /**
     * Overrides standard damage reception to apply defensive subtraction and logging 
     * tracking enemy names if the strike results in a character defeat.
     *
     * @param entity the attacking entity dealing the blow
     */
    public void takeDmg(GameCharacter entity) {
        super.takeDmg(entity.getAttack());

        //if character dies from taking damage
        if (this.charDeath()) {
            this.causeOfDeath = entity.getName();
        }
    }

    /**
     * Iterates backward through the inventory layout to equip the previous item node.
     *
     * @return true if selection changed successfully, false if structure is too small
     */
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

    /**
     * Iterates forward through the inventory layout to equip the next item node.
     *
     * @return true if selection changed successfully, false if structure is too small
     */
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

    /**
     * Triggers the active function of the selected item, handles its depletion removal, 
     * re-indexes standard item assignments, and steps the engine turn metrics.
     *
     * @return true if item was successfully validated and used, false otherwise
     */
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

    /**
     * Facilitates purchasing an item asset if gold amount allow.
     *
     * @param purchase the Item targeted for purchase
     * @return true if bought successfully, false otherwise
     */
    public boolean buyItem(Item purchase) {
        //TODO
         return false;
    }

    /**
     * Discards an item asset out of the active list using an explicit collection index.
     *
     * @param index the coordinate index of the target object node
     * @return true if dropped successfully, false otherwise
     */
    public boolean discardItem(int index) {
        //TODO
         return false;
    }

    /**
     * Applies restorative values to the active health variable pool without 
     * surpassing the defined maximum capability limit.
     *
     * @param amount the numerical health value to add
     * @return true if recovery processes successfully, false if value is invalid
     */
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

    /**
     * Executes wall destruction processing against blocked target map coordinates.
     *
     * @param tile  the blocking target tile coordinate component
     * @param floor the current Floor context to mutate
     */
    public void dig(Tile tile, Floor floor) {
        floor.destroyTile(tile);
    }

    /**
     * Extracts items or currency value arrays directly out of destructible tile types, 
     * updating internal inventory and purging map structures afterwards.
     *
     * @param tile  the target source collectible tile
     * @param floor the active Floor map context modification reference
     */
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

    /**
     * Unlocks a treasure tile to distribute environmental asset drops.
     *
     * @param tile the target treasure-chest element layout
     */
    public void openTreasure(DestructibleTile tile) {
        tile.dropTreasure();
    }

    /**
     * Implements step direction handling ('w','a','s','d'), combat conflict discovery, 
     * hazard step environmental damage logging, and destructible path routing interactions.
     *
     * @param direction the character character layout input indicating path target
     * @param floor the target Floor system execution reference
     */
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
        if (enemy != null && enemy.getX() == next.getX() && enemy.getY() == next.getY()) {
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

    /**
     * Scans the 2D layout grid structure looking for explicitly matching unique character keys 
     * to accurately synchronize tracking coordinates during initialization steps.
     *
     * @param map the target tile layout matrix structure to process
     */
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
                    this.x = map[i][j].getX();
                    this.y = map[i][j].getY();
                    return;
                }
            }
        }
        // not found: leave at 0,0
        this.x = 0;
        this.y = 0;
    }
}
