//DestructibleTile subclass
package Dungeon_Classes;
import Item_Classes.*;

/**
 * Represents a tile within the dungeon layout that can be broken or collected.
 * Manages specialized behaviors for dynamic map objects including spawning 
 * treasure rewards, dropping randomized currency amounts, and granting items.
 * 
 * @author Katigbak and Porciuncula
 * @version 1.0
 */
public class DestructibleTile extends Tile {
    //attributes
    /** Flag indicating if this tile holds hidden or uncollected treasure. */
    private boolean treasure; 
    /** The amount of gold dropped when this tile is destroyed. */
    private int goldDrop;
    /** The item asset dropped when this tile is destroyed. */ 
    private Item itemDrop; 

    //constructor
    /**
     * Constructs a destructible tile with direct assignments for drops and attributes.
     *
     * @param x the map column coordinate
     * @param y the map row coordinate
     * @param symbol the character representation of the tile
     * @param goldDrop the exact currency amount contained
     * @param itemDrop the item payload contained
     * @param treasure flag specifying if it is a chest configuration
     */
    public DestructibleTile(int x, int y, char symbol, int goldDrop, Item itemDrop, boolean treasure) {
        super(x, y, symbol);
        this.treasure = treasure;
        this.goldDrop = goldDrop;
        this.itemDrop = itemDrop;
        super.assignProperties();
    }

    //copy constructor
    /**
     * Constructs a destructible tile copy using attributes from an existing parent Tile instance.
     *
     * @param parentTile the source tile to copy properties from
     */
    public DestructibleTile(Tile parentTile) {
        super(parentTile.getX(), parentTile.getY(), parentTile.getSymbol());
        assignProperties();
    }

    //getters/setters
    /**
     * Checks if the tile contains treasure contents.
     *
     * @return true if the tile contains treasure, false otherwise
     */
    public boolean getTreasure() {
        return this.treasure;
    }

    /**
     * Updates the treasure status flag for this tile.
     *
     * @param treasure the new treasure status flag
     */
    public void setTreasure(boolean treasure) {
        this.treasure = treasure;
    }

    /**
     * Retrieves the currency amount dropped upon destruction.
     *
     * @return the currency amount dropped upon destruction
     */
    public int getGoldDrop() {
        return this.goldDrop;
    }

    /**
     * Updates the gold drop value for this tile.
     *
     * @param goldDrop the new gold drop value
     */
    public void setGoldDrop(int goldDrop) {
        this.goldDrop = goldDrop;
    }

    /**
     * Retrieves the item payload dropped upon destruction.
     *
     * @return the item payload dropped upon destruction
     */
    public Item getItemDrop() {
        return this.itemDrop;
    }

    /**
     * Updates the item drop configuration for this tile.
     *
     * @param itemDrop the new item drop configuration
     */
    public void setItemDrop(Item itemDrop) {
        this.itemDrop = itemDrop;
    }

    //additional methods
    /**
     * Randomizes the treasure tile's content state, transforming its layout 
     * representation into either a raw item drop ('I') or a gold drop ('g').
     */
    public void dropTreasure() {
        int rand = (int)(Math.random() * 2);

        if (rand == 0) { //randomized to item
            this.symbol = 'I';
        } else { //randomized to gold
            this.symbol = 'g';
        }

        super.assignProperties();
    }

    /**
     * Overrides state definitions to assign specialized parameters like randomized 
     * currency scaling or default consumable items based on the active symbol signature.
     */
    public void assignProperties() {
        super.assignProperties();

        switch(this.symbol) {
            case 'T': //treasure tiles
                this.treasure = true;
                this.goldDrop = (int)(Math.random() * 91 + 10); //random gold drop between 10 and 100
                this.itemDrop = new ConsumableItem("Noppo Bread", 0.5f);
                break;
            default:
                this.treasure = false;
                this.goldDrop = 0;
                this.itemDrop = null;
                break;
        }
    }
}