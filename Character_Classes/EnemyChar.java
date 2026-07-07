//EnemyChar subclass
package Character_classes;

import Dungeon_classes.*;
import java.util.Random;

public class EnemyChar extends GameCharacter {
    //attributes
    private int goldDrop;
    private int turnsPerMove;
    private int detectionRange;

    //constructor
    public EnemyChar(String name, float health, float attack, int goldDrop, int turnsPerMove, int detectionRange, Tile tile) {
        super(name, health, attack, tile);
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

    //additional methods
    public boolean detectPlayer(Tile[][] map, PlayableChar Yohane) {
        int dx, dy;

        dx = this.getTile().getX() - Yohane.getTile().getX();
        dy = this.getTile().getY() - Yohane.getTile().getY();

        if (dx < 0) {
            dx = dx * -1;
        }

        if (dy < 0) {
            dy = dy * -1;
        }

        if (dx + dy == detectionRange) {
            return true;
        }

        return false;
    }

    public int dropGold() {
        return goldDrop;
    }

    public void move(Floor floor, PlayableChar Yohane) {
        boolean move = Yohane.getTurnCount() % turnsPerMove == 0;

        if (move) {
            if (detectPlayer(floor.getMap(), Yohane)) {
                dealDmg(Yohane);
            } else {
                randomMove(floor);
            }
        }
    }

    public void randomMove(Floor floor) {
        int direction;
        Tile next = null;

        //enemies are not mentioned to be able to move over heat tiles
        //this is exclusive to enemies, thus is checked uniquely in this method
        do {
            //generate random direction
            Random rand = new Random();
            direction = rand.nextInt(4);
            next = nextTile(direction, floor);
        } while (next.getSymbol() == 'h');

        super.move(direction, floor);
    }

}