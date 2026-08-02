//NPChar subclass
package Character_Classes;

/**
 * Represents a non-playable character (NPC) in the game world.
  * This class handles friendly townspeople or companions who can be rescued,
 * tracking whether and how many times they have been saved.
 * 
 * @author Katigbak and Porciuncula
 * @version 2.0
 */
public class NPChar extends GameCharacter {
    //attributes
    /** Flag tracking whether the NPC has been rescued or completed their objective. */
    private boolean saved;
    /** The running count of how many times this NPC has been saved. */
    private int Times_Saved;

    //constructor
    /**
     * Constructs an NPC with a specific name.
     * Sets their default state as not yet saved, with a saved count of 0.
     *
     * @param name the name of the NPC
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
    public boolean isSaved() {
        return this.saved;
    }

    /**
     * Updates the rescue status of the NPC.
     * 
     * @param saved the new status indicating whether the NPC is saved
     */
    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    /**
     * Retrieves the total number of times this NPC has been saved.
     *
     * @return the running count of times saved
     */    
    public int getTimesSaved() {
        return this.Times_Saved;
    }

    /**
     * Increments the total number of times this NPC has been saved by one.
     */    
    public void incrementTimesSaved() {
        this.Times_Saved++;
    }
}