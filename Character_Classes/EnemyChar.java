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
    public EnemyChar(String name, int health, int attack, int goldDrop, int turnsPerMove, int detectionRange) {
        super(name, health, attack, (Tile) null);
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

    public void moveTile(Floor floor, PlayableChar Yohane) {
        if (Yohane.getTurnCount() % turnsPerMove != 0) {
            return;
        }

        if (detectPlayer(floor.getMap(), Yohane)) {
            dealDmg(Yohane);
        }
        else {
            randomMove(floor);
        }
    }

    public void randomMove(Floor floor) {

        Random rand = new Random();

        int direction;
        int x, y;
        Tile next;

        direction = rand.nextInt(4);

        x = getTile().getX();
        y = getTile().getY();

        switch (direction) {
            case 0:
                x--;
                break;

            case 1:
                x++;
                break;

            case 2:
                y--;
                break;

            case 3:
                y++;
                break;
        }

        next = floor.getMap()[x][y];

        if (next.getSymbol() == '.') {
            floor.moveCharacter(getTile(), next, this);
        }
    }

}