//GameCharacter class
package Character_classes;
import Dungeon_classes.*;

public class GameCharacter {
    //attributes
    protected String name;
    protected float health;
    protected float attack;
    protected Tile tile;
    protected String dialogue;

    //constructor
    public GameCharacter(String name, float health, float attack, String dialogue) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.tile = null;
        this.dialogue = dialogue;
    }

    public GameCharacter(String name, float health, float attack, Tile tile) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.tile = tile;
        this.dialogue = null;
    }

    public GameCharacter(String name, String dialogue) {
        this.name = name;
        this.health = this.attack = 0;
        this.tile = null;
        this.dialogue = dialogue;
    }

    //getters/setters

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getHealth() {
        return this.health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getAttack() {
        return this.attack;
    }

    public void setAttack(float attack) {
        this.attack = attack;
    }

    public Tile getTile() {
        return this.tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public String getDialogue() {
        return this.dialogue;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }    

    //additional methods
    public boolean charDeath() {
        boolean isDead = false;

        if (this.health <= 0)
            isDead = true;

        return isDead;
    }

    protected Tile nextTile(int direction, Floor floor) {
        int x, y;
        Tile next;

        x = this.tile.getX();
        y = this.tile.getY();

        switch (direction) {
            case 0: x--; break;
            case 1: x++; break;
            case 2: y--; break;
            case 3: y++; break;
        }

        next = floor.getMap()[x][y];
        return next;
    }

    public void move(int direction, Floor floor) {
        // int x, y;
        // Tile next;

        // x = this.tile.getX();
        // y = this.tile.getY();

        // switch (direction) {
        //     case 0: x--; break;
        //     case 1: x++; break;
        //     case 2: y--; break;
        //     case 3: y++; break;
        // }
        Tile next = nextTile(direction, floor);

        if (floor.validateMove(next)) {
            floor.moveCharacter(this.tile, next, this);
        }
    }

    public void dealDmg(GameCharacter enemy) {
        //TODO
    }

    public void takeDmg(float damage) {
        this.health -= damage;
    }
}