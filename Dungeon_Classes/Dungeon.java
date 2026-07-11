//Dungeon Class
package Dungeon_Classes;
import Character_Classes.*;

/**
 * Represents a multi-floor dungeon layout within the game world.
 * Manages the progressive tracking of floors, verification of completion criteria, 
 * floor transition increments, and end-of-game logic when a player character is defeated.
 * 
 * @author Katigbak and Porciuncula
 * @version 1.0
 */
public class Dungeon {
    //attributes
    /** The descriptive name of the dungeon. */
    private String name;
    /** The index or number designating this specific dungeon sequence. */
    private int dungeonNum;
    /** The total count of floor stages contained in this dungeon. */
    private int numFloors;
    /** The total count of floor stages contained in this dungeon. */
    private int curFloor;
    /** The array array structure managing the individual Floor environments. */
    private Floor[] floors;
    /** Flag tracking whether the final floor has been fully cleared. */
    private boolean completion;
    /** Member to be saved from dungeon. */
    private NPChar member;

    //constructor
    /**
     * Constructs a dungeon environment with predefined floors, resetting the current 
     * floor tracking index to its baseline starting stage.
     *
     * @param name the structural name of the dungeon
     * @param dungeonNum the identifying number of the dungeon
     * @param numFloors the ceiling number of floors inside this structure
     * @param floors the composite array of Floor items making up the dungeon layout
     */
    public Dungeon(String name, int dungeonNum, int numFloors, Floor[] floors, NPChar member) {
        this.name = name;
        this.dungeonNum = dungeonNum;
        this.numFloors = numFloors;
        this.curFloor = 1; //curFloor always starts at one
        this.floors = floors;
        this.completion = false;
        this.member = member;
    }

    //getters/setters
    /**
     * Retrieves the name string representation of the dungeon.
     *
     * @return the name string representation
     */
    public String getName() {
        return this.name;
    }

    /**
     * Updates the identity text name of the dungeon.
     *
     * @param name the updated identity text name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the sequential dungeon number index.
     *
     * @return the sequential dungeon number index
     */
    public int getDungeonNum() {
        return this.dungeonNum;
    }

    /**
     * Updates the identification index number of the dungeon.
     *
     * @param dungeonNum the new identification index number
     */
    public void setDungeonNum(int dungeonNum) {
        this.dungeonNum = dungeonNum;
    }

    /**
     * Retrieves the total capacity boundary count of floors.
     *
     * @return the total capacity boundary count of floors
     */
    public int getNumFloors() {
        return this.numFloors;
    }

    /**
     * Updates the upper count of contained floors.
     *
     * @param numFloors the new upper count of contained floors
     */
    public void setNumFloors(int numFloors) {
        this.numFloors = numFloors;
    }

    /**
     * Retrieves the active floor location index step.
     *
     * @return the active floor location index step
     */
    public int getCurFloor() {
        return this.curFloor;
    }

    /**
     * Updates the target step position index to track.
     *
     * @param curFloor the target step position index to track
     */
    public void setCurFloor(int curFloor) {
        this.curFloor = curFloor;
    }

    /**
     * Retrieves the array holding floor records.
     *
     * @return the array holding floor records
     */
    public Floor[] getFloors() {
        return this.floors;
    }

    /**
     * Updates the replacement array structure of floor nodes.
     *
     * @param floors the replacement array structure of floor nodes
     */
    public void setFloors(Floor[] floors) {
        this.floors = floors;
    }

     /**
     * Retrieves the associated member.
     *
     * @return NPChar instance
     */
    public NPChar getMember() {
        return this.member;
    }


    //additional methods
    /**
     * Assigns a series of floor structures matching a specific map index catalogue.
     *
     * @param catalogue the input array mapping available layout references
     */
    public void assignFloors(Floor[] catalogue) {
        //TODO: WILL IMPLEMENT IN MCO2
    }

    /**
     * Evaluates dungeon completion by checking the termination status of 
     * the final floor within the array stack.
     *
     * @param entity the playable character instance to evaluate
     * @return true if the final map floor conditions are cleared, false otherwise
     */
    public boolean isCompleted(PlayableChar entity) {
        this.completion = this.floors[numFloors-1].completeFloor(entity) && this.curFloor == numFloors;
        String YELLOW, RESET;
        YELLOW = "\u001B[38;5;227m";
        RESET = "\u001B[0m";

        if (this.completion) {
            System.out.println(YELLOW + this.name + " completed!" + RESET);
            this.member.isSaved(true);
        }
        return this.completion;
    }

    /**
     * Advances the tracking index for the player's active floor map level, 
     * capped at the defined upper bound number of maximum floors.
     */
    public void incrementCurFloor() {
        if (this.curFloor != this.numFloors) {
            this.curFloor++;
        }
    }

    /**
     * Monitors the user's survival state. If death is detected, it logs 
     * formatted console readouts indicating the asset culprit responsible.
     *
     * @param entity the user-controlled character instance to monitor
     * @return true if the user character has fallen in combat or hazards, false otherwise
     */
    public boolean gameOver(PlayableChar entity) {
        boolean gameOver = entity.charDeath();
        return gameOver;
    }
}
