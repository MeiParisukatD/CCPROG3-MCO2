package Item_Classes;

import Character_Classes.PlayableChar;

/**
 * Represents passive equipment or upgrades purchased from Hanamaru's store.
 * Passive items grant ongoing effects or abilities to the player character
 * without being consumed upon use.
 * 
 * @author Katigbak and Porciuncula
 * @version 2.0
 */
public class PassiveItem extends Item {
    /** The passive ability or effect granted by this item. */
    private String ability;

    /**
     * Constructs a PassiveItem with a specified name, gold cost, and special ability.
     *
     * @param name    the display name of the passive item
     * @param price   the cost in gold (GP) to purchase the item
     * @param ability the passive effect or immunity conferred by the item
     */
    public PassiveItem(String name, int price, String ability) {
        super(name, price);
        this.ability = ability;
    }

    /**
     * Retrieves the description of the passive ability granted by this item.
     *
     * @return the passive ability string
     */
    public String getAbility() {
        return this.ability;
    }

    /**
     * Activates the passive item by placing it into the player's inventory.
     *
     * @param Yohane the playable character receiving the passive item
     */
    public void activate(PlayableChar Yohane) {
        if (Yohane != null) {
            Yohane.addItem(this);
        }
    }

    /**
     * Deactivates the passive item by removing it from the player's inventory.
     *
     * @param Yohane the playable character losing the passive item
     */
    public void deactivate(PlayableChar Yohane) {
        if (Yohane != null && Yohane.getInventory().contains(this)) {
            int index = Yohane.getInventory().indexOf(this);
            Yohane.discardItem(index);
        }
    }

    /**
     * Overrides the item activation behavior. Passive items are non-consumable
     * persistent effects, so manually using them returns false.
     *
     * @param player the playable character attempting to use the item
     * @return always false since passive items cannot be manually consumed
     */
    @Override
    public boolean use(PlayableChar player) {
        return false; // Passives are constant, not consumed on use
    }
}