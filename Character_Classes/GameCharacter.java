//GameCharacter class
package Character_Classes;
import Dungeon_Classes.*;

public class GameCharacter {
    //attributes
    protected String name;
    protected float health;
    protected float attack;
    protected String dialogue;
    protected int x, y; //coordinates

    //constructor
    public GameCharacter(String name, float health, float attack, String dialogue) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.x = 0;
        this.y = 0;
        this.dialogue = dialogue;
    }

    public GameCharacter(String name, float health, float attack, int x, int y) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.x = x;
        this.y = y;
        this.dialogue = null;
    }

    public GameCharacter(String name, String dialogue) {
        this.name = name;
        this.health = this.attack = 0;
        this.x = 0;
        this.y = 0;
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
    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
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

    public void dealDmg(GameCharacter enemy) {
        enemy.takeDmg(this.attack);
    }

    public void takeDmg(float damage) {
        this.health -= damage;
    }

    protected Tile nextTile(int direction, Floor floor) {
        int next_x, next_y;
        Tile next;

        next_x = this.x;
        next_y = this.y;

        switch (direction) {
            case 0: next_x--; break;
            case 1: next_x++; break;
            case 2: next_y--; break;
            case 3: next_y++; break;
        }

        next = floor.getMap()[next_x][next_y];
        return next;
    }

    public void move(int direction, Floor floor) {
        Tile next = this.nextTile(direction, floor);

        if (floor.validateMove(next)) {
            this.x = next.getX();
            this.y = next.getY();
        }
    }
}