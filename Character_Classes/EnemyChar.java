//EnemyChar subclass
package Character_Classes;

import Dungeon_Classes.*;

/**
 * Represents a hostile NPC entity within the dungeon grid.
 * Handles enemy specific behaviors including turn intervals, detection ranges, 
 * random movement avoiding heat tiles, and dropping gold upon defeat.
 * 
 * @author Katigbak and Porciuncula
 * @version 1.0
 */
public class EnemyChar extends GameCharacter {
    //attributes
    /** The amount of gold dropped when this enemy is defeated. */
    protected int goldDrop;
    /** The turn interval required before this enemy can take an action. */
    protected int turnsPerMove;
    /** The Manhattan distance radius within which the enemy can detect the player. */
    protected float detectionRange;
    protected boolean diagonal;

    //constructor
    /**
     * Constructs an enemy character with defined combat stats, drop values, and grid position.
     *
     * @param name the designation of the enemy
     * @param health the initial health points
     * @param attack the base damage power
     * @param goldDrop the amount of gold dropped upon death
     * @param turnsPerMove how often the enemy moves relative to player turns
     * @param detectionRange the sight radius for spotting the player
     * @param x the starting X grid coordinate
     * @param y the starting Y grid coordinate
     */
    public EnemyChar(String name, float health, float attack, int goldDrop, int turnsPerMove, float detectionRange, boolean diagonal, int x, int y) {
        super(name, health, attack, x, y);
        this.goldDrop = goldDrop;
        this.turnsPerMove = turnsPerMove;
        this.detectionRange = detectionRange;
        this.diagonal = diagonal;
        this.floor = null;
    }

    //getters/setters
    /**
     * Calculates or retrieves the amount of currency awarded when this entity is defeated.
     * 
     * @return the gold drop amount
     */
    public int getGoldDrop() {
        return this.goldDrop;
    }

    /**
     * Updates the currency reward value assigned to this entity.
     * 
     * @param goldDrop the new gold drop amount
     */
    public void setGoldDrop(int goldDrop) {
        this.goldDrop = goldDrop;
    }

    /**
     * Gets the movement delay factor determining how frequently this entity acts relative to game turns.
     * 
     * @return the number of turns required per move
     */
    public int getTurnsPerMove() {
        return this.turnsPerMove;
    }

    /**
     * Modifies the movement frequency rate for this entity.
     * 
     * @param turnsPerMove the new turn interval layout
     */
    public void setTurnsPerMove(int turnsPerMove) {
        this.turnsPerMove = turnsPerMove;
    }

    /**
     * Retrieves the distance threshold within which this entity can spot or track the player.
     * 
     * @return the player detection radius
     */
    public float getDetectionRange() {
        return this.detectionRange;
    }

    /**
     * Updates the vision boundaries or awareness radius for tracking targets.
     * 
     * @param detectionRange the new player detection radius
     */
    public void setDetectionRange(float detectionRange) {
        this.detectionRange = detectionRange;
    }

    //additional methods

    /**
     * Spawns a gold treasure tile at the enemy's current grid position upon defeat.
     *
     * @param floor the current Floor map context where the tile is placed
     */
    public void dropGold(Floor f) {
        int x = this.x;
        int y = this.y;

        f.getMap()[x][y] = new DestructibleTile(
            this.x, this.y,
            'g', this.goldDrop,
            null, true);
    }

    protected double calcDistance(double x1, double y1, double x2, double y2) {
        double xPow, yPow, distance;

        xPow = Math.pow((x2-x1), 2);
        yPow = Math.pow((y2-y1), 2);
        distance = Math.sqrt(xPow + yPow);

        return distance;
    }

    /**
     * Calculates whether the player character is within the enemy's detection range 
     * using the distance formula.
     *
     * @param map the 2D grid matrix of the current floor
     * @param Yohane the playable character instance to track
     * @return true if the player is within range, false otherwise
     */
    public boolean detectPlayer(Tile[][] map, PlayableChar entity) {
        double distance;
        distance = calcDistance(this.x, this.y, entity.getX(), entity.getY());

        if (distance <= detectionRange) {
            return true;
        }

        return false;
    }
}