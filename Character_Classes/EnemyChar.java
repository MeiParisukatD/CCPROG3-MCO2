//EnemyChar subclass
package Character_Classes;

import Dungeon_Classes.*;

public class EnemyChar extends GameCharacter {
    //attributes
    private int goldDrop;
    private int turnsPerMove;
    private int detectionRange;

    //constructor
    public EnemyChar(String name, float health, float attack, int goldDrop, int turnsPerMove, int detectionRange, int x, int y) {
        super(name, health, attack, x, y);
        this.goldDrop = goldDrop;
        this.turnsPerMove = turnsPerMove;
        this.detectionRange = detectionRange;
    }

    //getters/setters
    public int getGoldDrop() {
        return this.goldDrop;
    }

    public void setGoldDrop(int goldDrop) {
        this.goldDrop = goldDrop;
    }

    public int getTurnsPerMove() {
        return this.turnsPerMove;
    }

    public void setTurnsPerMove(int turnsPerMove) {
        this.turnsPerMove = turnsPerMove;
    }

    public int getDetectionRange() {
        return this.detectionRange;
    }

    public void setDetectionRange(int detectionRange) {
        this.detectionRange = detectionRange;
    }
// public DestructibleTile(int x, int y, char symbol, int goldDrop, Item itemDrop, boolean treasure)
    //additional methods
    public void dropGold(Floor floor) {
        int x = this.getX();
        int y = this.getY();

        floor.getMap()[x][y] = new DestructibleTile(
            this.getX(), 
            this.getY(),
            'g', this.goldDrop,
            null, true);
    }

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