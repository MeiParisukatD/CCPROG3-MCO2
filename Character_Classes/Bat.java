package Character_Classes;

import Dungeon_Classes.*;

public class Bat extends EnemyChar {
    public Bat (float attack, int goldDrop, int turnsPerMove, float detectionRange, boolean diagonal, int x, int y) {
        super("Bat", 1.0f, attack, goldDrop, turnsPerMove, detectionRange, diagonal, x, y);
    }

    /**
     * Manages the enemy's turn execution. Checks turn intervals to either 
     * attack the player if detected, or pick a random direction that avoids 
     * heat ('h') tiles.
     *
     * @param floor the current Floor context
     * @param entity the playable character acting as the turn and target reference
     */
    public void move(PlayableChar entity) {
        //determines if it is currently a turn for the enemy
        boolean move = entity.getTurnCount() % this.turnsPerMove == 0;

        if (move) {
            if (detectPlayer(this.floor.getMap(), entity)) {
                this.dealDmg(entity); //attack player if detected
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
                super.move(direction, crossWater);   // was: super.move(direction, false);
            }
        }
    }
}
