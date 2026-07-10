package Item_Classes;

import Character_Classes.PlayableChar;

/**
 * Represents a restorative item type that can be consumed by a player character.
 * Provides functional behavior to restore a character's health points based on a 
 * predefined healing constant upon usage.
 * 
 * @author Katigbak and Porciuncula
 * @version 1.0
 */
public class ConsumableItem extends Item {
    /** The flat amount of health points restored to the character upon consumption. */
    private float healAmount;

    /**
     * Constructs a consumable item with a name and a restoration value, 
     * defaulting its monetary price handling to the parent class baseline.
     *
     * @param name the descriptive name of the item
     * @param healAmount the quantity of health points this item recovers
     */
    public ConsumableItem(String name, float healAmount) {
        super(name);
        this.healAmount = healAmount;
    }
    
    /**
     * Constructs a consumable item with a name, store purchase price, 
     * and a restorative health value.
     *
     * @param name the descriptive name of the item
     * @param price the gold buy or sell value of the item
     * @param healAmount the quantity of health points this item recovers
     */
    public ConsumableItem(String name, int price, float healAmount) {
        super(name, price);
        this.healAmount = healAmount;
    }

    /**
     * Retrieves the configured health restoration value of the consumable item.
     *
     * @return the configured health restoration value
     */
    public float getHealAmount() {
        return healAmount;
    }

    /**
     * Updates the health restoration value of the consumable item.
     *
     * @param healAmount the updated health restoration value to apply
     */
    public void setHealAmount(float healAmount) {
        this.healAmount = healAmount;
    }

    /**
     * Applies the item's curative property to the targeted player character.
     *
     * @param player the user-controlled character consuming the item
     * @return true if the healing routine executes successfully, false if processing fails
     */
    @Override
    public boolean use(PlayableChar player) {
        return player.heal(healAmount);
    }
}
