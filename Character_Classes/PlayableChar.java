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
 * @version 2.0
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
    /** The collection of special abilities currently unlocked by the character. */    
    private ArrayList<String> abilities;
    /** The item currently selected or equipped from the inventory layout. */
    private Item curItem;
    /** The source of damage or hazard that caused the character to lose all health. */
    private String causeOfDeath;
    /** Flag tracking whether the character was just damaged, used for map display purposes. */
    private boolean justDamaged;
    /** Flag tracking whether the character has already been attacked during the current turn. */
    private boolean attackedThisTurn;

    //constructor
    /**
     * Constructs a playable character with defined statistics and resets 
     * status variables to their starting configurations.
     *
     * @param name     the identifier name of the playable character
     * @param health   the starting health and ceiling threshold capacity
     * @param attack   the base attack damage capacity
     */
    public PlayableChar(String name, float health, float attack) {
        super(name, health, attack);
        this.goldOwned = 0;
        this.turnCount = 0;
        this.maxHealth = health;
        this.inventory = new ArrayList<Item>();
        this.abilities = new ArrayList<String>();
        this.curItem = null;
        this.causeOfDeath = null;
        this.justDamaged = false;
        this.attackedThisTurn = false;
    }

    //getters/setters
    /**
     * Returns the total amount of gold owned by this character.
     *
     * @return the total amount of gold owned
     */
    public int getGoldOwned() {
        return this.goldOwned;
    }

    /**
     * Sets the total amount of gold owned by this character.
     *
     * @param goldOwned the updated gold value to assign
     */
    public void setGoldOwned(int goldOwned) {
        this.goldOwned = goldOwned;
    }

    /**
     * Returns the upper limit ceiling of this character's health.
     *
     * @return the upper limit ceiling of character health
     */
    public float getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * Sets the upper limit ceiling of this character's health.
     *
     * @param maxHealth the new upper threshold for health
     */
    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * Returns the running record tracking total moves executed by this character.
     *
     * @return the running record tracking total moves executed
     */
    public int getTurnCount() {
        return this.turnCount;
    }

    /**
     * Sets the running record tracking total moves executed by this character.
     *
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
     * Unlocks a new special ability for the character.
     *
     * @param ability the identifier string of the ability to add
     */    
    public void addAbility(String ability) {
        this.abilities.add(ability);
    }

    /**
     * Checks whether the character currently has a given special ability unlocked.
     *
     * @param ability the identifier string of the ability to check
     * @return true if the character has the ability, false otherwise
     */    
    public boolean hasAbility(String ability) {
        return this.abilities.contains(ability);
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

    /**
     * Checks whether the character was just damaged, for map display purposes.
     *
     * @return true if the character was just damaged, false otherwise
     */    
    public boolean isJustDamaged() {
        return this.justDamaged;
    }

    /**
     * Updates the flag tracking whether the character was just damaged.
     *
     * @param status the new just-damaged status
     */    
    public void setJustDamaged(boolean status) {
        this.justDamaged = status;
    }
    

    /**
     * Checks whether the character has already been attacked during the current turn.
     *
     * @return true if the character has been attacked this turn, false otherwise
     */    
    public boolean isAttackedThisTurn() {
        return this.attackedThisTurn;
    }

    /**
     * Updates the flag tracking whether the character has been attacked during the current turn.
     *
     * @param status the new attacked-this-turn status
     */
    public void setAttackedThisTurn(boolean status) {
        this.attackedThisTurn = status;
    }

    //additional methods
    /**
     * Increments the total running turn engine index step count by one.
     */
    public void incrementTurn() {
        this.turnCount++;
    }

    /**
     * Re-points curItem after a held item has been removed from the inventory,
     * so a stale reference to the removed object can't be mistaken for a still-held item.
     * Falls back to a remaining copy of the same item if one exists, otherwise the
     * first item in the inventory, otherwise null if the inventory is now empty.
     *
     * @param removedItem the item reference that was just removed from the inventory
     */
    private void reindexCurItem(Item removedItem) {
        if (this.inventory.isEmpty()) {
            curItem = null;
        } //if copies of the item still exist, reference that copy
        else if (this.inventory.indexOf(removedItem) != -1) {
            int index = this.inventory.indexOf(removedItem);
            curItem = inventory.get(index);
        } else { //if no copies exist, switch to next item
            curItem = inventory.get(0);
        }
    }

    /**
     * Overrides standard damage reception to apply defensive subtraction and logging 
     * tracking enemy names if the strike results in a character defeat.
     *
     * @param entity the attacking entity dealing the blow
     */
    @Override
    public void takeDmg(GameCharacter entity) {
        float attack;
        //if character has bat tamer and attacking entity is a bat
        if (entity instanceof Bat && this.abilities.contains("Bat Damage Reduction")) {
            attack = 0.5f;
        } else { //else, entity damage acts normally
            attack = entity.getAttack();
        }

        this.health -= attack; //reduces health
        this.justDamaged = true; //for map display purposes

        //if character dies from taking damage
        if (this.charDeath()) {
            this.causeOfDeath = entity.getName();
        }

        //if character dies but has choco mint ice cream
        if (health <= 0 &&
            curItem != null &&
            curItem.getName().equals("Choco-Mint Ice Cream")) {

            setHealth(getMaxHealth());
            inventory.remove(curItem);

            //re-point curItem so the removed Ice Cream can't trigger the save again
            reindexCurItem(curItem);

            System.out.println("Choco-Mint Ice Cream saved Yohane!");
        }
    }

    /**
     * Iterates backward through the inventory layout to equip the previous item node.
     *
     * @return true if selection changed successfully, false if structure is too small
     */
    public boolean prevItem() {
        //if inventory size is <= 1, there is no previous item to switch to
        if (inventory.size() <= 1) {
            return false;
        }

        //get index of previous item
        int index = inventory.indexOf(curItem);
        int startIndex = index;
        do { //if previous item is duplicate, skip
            index--;
            if (index < 0) {
                index = inventory.size() - 1; // point to last item
            }

            // If we've come full circle, no different item exists
            if (index == startIndex) {
                return false;
            }
        } while (inventory.get(index).getName().equalsIgnoreCase(curItem.getName()));

        //get item pointed to
        curItem = inventory.get(index);
        return true;
    }

    /**
     * Iterates forward through the inventory layout to equip the next item node.
     *
     * @return true if selection changed successfully, false if structure is too small
     */
    public boolean nextItem() {
        //if inventory size is <= 1, there is no next item to switch to
        if (inventory.size() <= 1) {
            return false;
        }

        //get index of next item
        int index = inventory.indexOf(curItem);
        int startIndex = index;
        do { //if next item is duplicate, skip
            index++;
            if (index >= inventory.size()) {
                index = 0; // point to first item
            }

            // If we've come full circle, no different item exists
            if (index == startIndex) {
                return false;
            }
        } while (inventory.get(index).getName().equalsIgnoreCase(curItem.getName()));

        //get item pointed to
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
            this.inventory.remove(curItem);
            reindexCurItem(curItem);
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
        //if lacking funds or purchase is null
        if (purchase == null || this.goldOwned < purchase.getPrice()) {
            return false;
        }

        //reduce item price from goldOwned
        this.goldOwned -= purchase.getPrice();

        //uses item depending on item type/class
        if (purchase instanceof UpgradeItem) {
            ((UpgradeItem) purchase).applyUpgrade(this);
        } else if (purchase instanceof PassiveItem) {
            ((PassiveItem) purchase).activate(this);
        } else if (purchase instanceof ConsumableItem) {
            this.addItem(purchase);
            if (this.curItem == null) {
                this.curItem = purchase;
            }
        }

        return true;
    }

    /**
     * Applies restorative values to the active health variable pool without 
     * surpassing the defined maximum capability limit.
     *
     * @param amount the numerical health value to add
     * @return true if recovery processes successfully, false if value is invalid
     */
    public boolean heal(float amount) {
        //if amount to be healed is <= 0, abandon action
        if (amount <= 0) {
            return false;
        }

        //add amount to health
        float newHealth = this.health + amount;
        //if over max health, set to max health
        if (newHealth > maxHealth) {
            newHealth = maxHealth;
        }
        //set new health
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
     * @param direction the character movement input key ('w', 'a', 's', or 'd')
     * @param cX the character's X coordinate prior to the move (reserved, currently unused)
     * @param cY the character's Y coordinate prior to the move (reserved, currently unused)
     */
    public void move(char direction, int cX, int cY) {
        int d = -1; //assigned sentinel value to accomodate compilation
        Tile next;

        //adjusting direction to numerical value
        switch (direction) {
            case 'w': d = 0; break;
            case 's': d = 1; break;
            case 'a': d = 2; break;
            case 'd': d = 3; break;
        }

        next = nextTile(d);

        //check for enemy
        EnemyChar enemy = floor.findEnemy(next.getX(), next.getY());
        if (enemy != null && enemy.getX() == next.getX() && enemy.getY() == next.getY()) {
            this.dealDmg(enemy);
        }
        //if next tile is not an entity or playable character
        else {
            boolean waterWalk = this.abilities.contains("Water & Heat Immunity") && next.getSymbol() == 'w';
            super.move(d, waterWalk);

            if (next.getSymbol() == 'x' && !this.abilities.contains("Spike Immunity")) {
                this.takeDmg(next.getDamage());
                if (this.charDeath()) {
                    this.causeOfDeath = "Spike Walls";
                }
            }

            if (next.getSymbol() == 'h' && !this.abilities.contains("Water & Heat Immunity")){
                this.takeDmg(next.getDamage());
                this.justDamaged = true;
                if (this.charDeath()) {
                    this.causeOfDeath = "Heat Tiles";
                }
            }

            //if character hasnt died yet from tile damage
            if (!this.charDeath()) {
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
     * Checks whether the character is currently standing on a heat tile and,
     * if so and the character has not moved since the previous check, applies
     * heat damage unless the character has heat immunity.
     *
     * @param prevX the character's X coordinate at the previous check
     * @param prevY the character's Y coordinate at the previous check
     */
    public void checkHeatDamage(int prevX, int prevY) {
        if (this.floor == null) return;
        if (this.x == prevX && this.y == prevY) {
            Tile current = this.floor.getMap()[this.x][this.y];
            if (current.getSymbol() == 'h' && !this.abilities.contains("Water & Heat Immunity")) {
                this.takeDmg(current.getDamage());
                this.justDamaged = true;
                if (this.charDeath()) {
                    this.causeOfDeath = "Heat Tiles";
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
        
        switch (this.name) { //key depends on character name
            case "Yohane": key = 'Y'; break;
            case "Lailaps": key = 'L'; break;
            case "Bat": key = 'b'; break;
            case "Siren": key = 'S'; break;
            default: key = ' '; break;
        }
        //scan map for key symbol
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