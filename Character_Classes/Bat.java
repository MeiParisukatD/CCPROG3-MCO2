package Character_Classes;

import Dungeon_Classes.*;

/**
 * Represents the standard hostile Bat enemy within the dungeon grid.
 * Bats attack the player when detected, and otherwise wander to a random
 * adjacent tile while avoiding heat tiles, other enemies, and occupied tiles.
 *
 * @author Katigbak and Porciuncula
 * @version 2.0
 */
public class Bat extends EnemyChar {
   
    /**
     * Constructs a Bat enemy with defined combat stats, drop value, and grid position.
     * Health is fixed at 1.0.
     *
     * @param attack         the base damage power
     * @param goldDrop       the amount of gold dropped upon death
     * @param turnsPerMove   how often the Bat moves relative to player turns
     * @param detectionRange the sight radius for spotting the player
     * @param diagonal       whether the Bat is allowed to move diagonally
     * @param x              the starting X grid coordinate
     * @param y              the starting Y grid coordinate
     */
    public Bat (float attack, int goldDrop, int turnsPerMove, float detectionRange, boolean diagonal, int x, int y) {
        super("Bat", 1.0f, attack, goldDrop, turnsPerMove, detectionRange, diagonal, x, y);
    }

    /**
     * Manages the enemy's turn execution. Checks turn intervals to either 
     * attack the player if detected, or pick a random direction that avoids 
     * heat ('h') tiles.
     *
     * @param entity the playable character acting as the turn and target reference
     */
    public void move(PlayableChar entity) {
        //determines if it is currently a turn for the enemy
        boolean move = entity.getTurnCount() % this.turnsPerMove == 0;

        if (move) {
            if (detectPlayer(this.floor.getMap(), entity)) {
                if (!entity.isAttackedThisTurn()) {
                    this.dealDmg(entity); //attack player if detected
                    entity.setAttackedThisTurn(true);
                }
            } else { //if player is not detected
                int direction, max;
                boolean taken;
                Tile next = null;

                //makes diagonal moves available if diagonal = true
                max = (diagonal) ? 8 : 4;

                //enemies are not mentioned to be able to move over heat tiles
                //this is exclusive to enemies, thus is checked uniquely in this method
                do {
                    direction = (int)(Math.random() * max);
                    next = nextTile(direction);
                    taken = this.floor.tileTaken(next.getX(), next.getY());
                } while (next.getSymbol() == 'h' || next.getSymbol() == 'E' || taken);

                boolean crossWater = (next.getSymbol() == 'w');
                super.move(direction, crossWater);  
            }
        }
    }
}
