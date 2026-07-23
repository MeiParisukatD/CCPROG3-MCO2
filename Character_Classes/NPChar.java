//NPChar subclass
package Character_Classes;

/**
 * Represents a non-playable character (NPC) in the game world.
 * This class handles friendly townspeople or companions who can hold items 
 * and track whether they have been rescued or interacted with.
 * 
 * @author Katigbak and Porciuncula
 * @version 2.0
 */
public class NPChar extends GameCharacter {
    //attributes
    /** Flag tracking whether the NPC has been rescued or completed their objective. */
    private boolean saved;
    private int Times_Saved;

    //constructor
    /**
     * Constructs an NPC with a specific name, and a reward item.
     * Sets their default state as not yet saved.
     * @param name     the name of the NPC
     * @param item     the item associated with or gifted by the NPC
     */
    public NPChar(String name) {
        super(name);
        this.saved = false;
        this.Times_Saved = 0;
    }

    //getters/setters
    /**
     * Checks the rescue status of the NPC to see if they are out of danger.
     * 
     * @return true if the NPC has been saved, false otherwise
     */
    public boolean getSaved() {
        return this.saved;
    }

    /**
     * Updates the rescue status of the NPC.
     * 
     * @param saved the new status indicating whether the NPC is saved
     */
    public void isSaved(boolean saved) {
        this.saved = saved;
    }

    public int getTimesSaved() {
        return this.Times_Saved;
    }

    public void incrementTimesSaved() {
        this.Times_Saved++;
    }
}