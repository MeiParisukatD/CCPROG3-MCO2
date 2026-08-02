//temp item class
package Item_Classes;

import Character_Classes.*;

/**
 * Represents the base blueprint for all inventory items within the game.
 * Defines shared properties such as item naming conventions, default pricing structures,
 * and standard interaction execution mechanisms meant to be customized by sub-classes.
 *
 * @author Katigbak and Porciuncula
 * @version 2.0
 */
public class Item {
    //attributes
    /** The descriptive name identifying the item. */
    protected String name;
    /** The basic gold value or purchase cost assigned to the item. */
    protected int price;
    /** The NPC that must be rescued before this item is unlocked for purchase, or null if unconditional. */
    protected NPChar condition;
    /** Flag tracking whether this item is currently available for purchase in the shop. */
    protected boolean available;

    /**
     * Constructs a baseline item with a name, defaulting its currency price tracking to zero.
     *
     * @param name the unique text name to identify the item
     * @param condition the NPC that must be rescued to unlock this item, or null if unconditional
     */
    public Item(String name, NPChar condition) {
        this.name = name;
        this.price = 0;
        this.condition = condition;
        this.available = true;
    }

    /**
     * Constructs an item setting up both its specific name designation and default store price.
     *
     * @param name  the unique text name to identify the item
     * @param price the monetary cost or evaluation of the item in gold points
     * @param condition the NPC that must be rescued to unlock this item, or null if unconditional
     */
    public Item(String name, int price, NPChar condition) {
        this.name = name;
        this.price = price;
        this.condition = condition;
        this.available = true;
    }

    /**
     * Retrieves the name identification string of the item.
     *
     * @return the name identification string
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the associated monetary gold price of the item.
     *
     * @return the associated monetary gold price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Checks whether this item is currently available for purchase in the shop.
     *
     * @return true if the item is available, false otherwise
     */
    public boolean isAvailable() {
        return this.available;
    }

    /**
     * Updates whether this item is currently available for purchase in the shop.
     *
     * @param status the new availability status
     */
    public void setAvailable(boolean status) {
        this.available = status;
    }

    /**
     * Triggers the inherent game mechanic behavior tied to item consumption or equipment.
     * This baseline implementation returns false and is intended to be overridden by specialized subclasses.
     *
     * @param player the target user character instance interacting with the item
     * @return true if the item was successfully utilized, false if processing was aborted or unsupported
     */
    public boolean use(PlayableChar player){
        //override
        return false;
    }

    /**
     * Checks whether this item's unlock condition has been satisfied, i.e. whether
     * it has no rescue requirement or its required NPC has already been saved.
     *
     * @return true if the item is unlocked, false otherwise
     */
    public boolean isUnlocked() {
        return condition == null || condition.isSaved();
    }
}