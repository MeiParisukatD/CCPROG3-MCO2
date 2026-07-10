//NPChar subclass
package Character_Classes;
import Item_Classes.*;

/**
 * Represents a non-playable character (NPC) in the game world.
 * This class handles friendly townspeople or companions who can hold items 
 * and track whether they have been rescued or interacted with.
 * 
 * @author Katigbak and Porciuncula
 * @version 1.0
 */
public class NPChar extends GameCharacter {
    //attributes
    /** The item held or rewarded by this NPC. */
    private Item item;
    /** Flag tracking whether the NPC has been rescued or completed their objective. */
    private boolean saved;

    //constructor
    /**
     * Constructs an NPC with a specific name, dialogue text, and a reward item.
     * Sets their default state as not yet saved.
     *
     * @param name     the name of the NPC
     * @param dialogue the conversation text associated with this NPC
     * @param item     the item associated with or gifted by the NPC
     */
    public NPChar(String name, String dialogue, Item item) {
        super(name, dialogue);
        this.item = item;
        this.saved = false;
    }

    //getters/setters
    /**
     * @return the item held by this NPC
     */
    public Item getItem() {
        return this.item;
    }

    /**
     * @param item the new item to assign to this NPC
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * @return true if the NPC has been saved, false otherwise
     */
    public boolean getSaved() {
        return this.saved;
    }

    /**
     * @param saved the new status indicating whether the NPC is saved
     */
    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}