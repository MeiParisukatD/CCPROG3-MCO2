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
    private int goldDrop;
    /** The turn interval required before this enemy can take an action. */
    private int turnsPerMove;
    /** The Manhattan distance radius within which the enemy can detect the player. */
    private int detectionRange;

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
    public EnemyChar(String name, float health, float attack, int goldDrop, int turnsPerMove, int detectionRange, int x, int y) {
        super(name, health, attack, x, y);
        this.goldDrop = goldDrop;
        this.turnsPerMove = turnsPerMove;
        this.detectionRange = detectionRange;
    }

    //getters/setters
    /**
     * @return the gold drop amount
     */
    public int getGoldDrop() {
        return this.goldDrop;
    }

    /**
     * @param goldDrop the new gold drop amount
     */
    public void setGoldDrop(int goldDrop) {
        this.goldDrop = goldDrop;
    }

    /**
     * @return the number of turns required per move
     */
    public int getTurnsPerMove() {
        return this.turnsPerMove;
    }

    /**
     * @param turnsPerMove the new turn interval layout
     */
    public void setTurnsPerMove(int turnsPerMove) {
        this.turnsPerMove = turnsPerMove;
    }

    /**
     * @return the player detection radius
     */
    public int getDetectionRange() {
        return this.detectionRange;
    }

    /**
     * @param detectionRange the new player detection radius
     */
    public void setDetectionRange(int detectionRange) {
        this.detectionRange = detectionRange;
    }

    //additional methods

    /**
     * Spawns a gold treasure tile at the enemy's current grid position upon defeat.
     *
     * @param floor the current Floor map context where the tile is placed
     */
    public void dropGold(Floor floor) {
        int x = this.getX();
        int y = this.getY();

        floor.getMap()[x][y] = new DestructibleTile(
            this.getX(), 
            this.getY(),
            'g', this.goldDrop,
            null, true);
    }

    /**
     * Calculates whether the player character is within the enemy's detection range 
     * using Manhattan distance calculation.
     *
     * @param map the 2D grid matrix of the current floor
     * @param Yohane the playable character instance to track
     * @return true if the player is within range, false otherwise
     */
    public boolean detectPlayer(Tile[][] map, PlayableChar Yohane) {
        int dx, dy;

        dx = this.getX() - Yohane.getX();
        dy = this.getY() - Yohane.getY();

        if (dx < 0) {
            dx = dx * -1;
        }

        if (dy < 0) {
            dy = dy * -1;
        }

        if (dx + dy <= detectionRange) {
            return true;
        }

        return false;
    }

    /**
     * Manages the enemy's turn execution. Checks turn intervals to either 
     * attack the player if detected, or pick a random direction that avoids 
     * heat ('h') tiles.
     *
     * @param floor the current Floor context
     * @param entity the playable character acting as the turn and target reference
     */
    public void move(Floor floor, PlayableChar entity) {
        //determines if it is currently a turn for the enemy
        boolean move = entity.getTurnCount() % turnsPerMove == 0;

        if (move) {
            if (detectPlayer(floor.getMap(), entity)) {
                this.dealDmg(entity); //attack Yohane if detected
            } else { //if Yohane is not detected
                int direction;
                Tile next = null;

                //enemies are not mentioned to be able to move over heat tiles
                //this is exclusive to enemies, thus is checked uniquely in this method
                do {
                    //generate random direction
                    direction = (int)(Math.random() * 4);
                    next = nextTile(direction, floor);
                } while (next.getSymbol() == 'h');

                super.move(direction, floor);
            }
        }
    }
}