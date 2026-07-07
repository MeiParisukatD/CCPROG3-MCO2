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
        this.tile = tile;
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

    public void move(char direction, Floor floor) {
        int x, y;
        Tile next;

        x = tile.getX();
        y = tile.getY();

        switch (direction) {
            case 'w': x--; break;
            case 's': x++; break;
            case 'd': y++; break;
            case 'a': y--; break;
            default: 
                System.out.println("[!] Invalid direction.");
                break;
        }

        if (floor.validateMove(floor.getMap()[x][y])) {
            next = floor.getMap()[x][y];
            floor.moveCharacter(tile, next, this);
        }
    }

    public void findCharTile(Tile[][] map) {
        int i, j;
        char key;

        switch (name) {
            case "Yohane": key = 'Y'; break;
            case "Lailaps": key = 'L'; break;
            case "Bat": key = 'b'; break;
            case "Siren": key = 'S'; break;
            default: key = ' '; break;
        }

        for (i = 0; i < map.length; i++) {
            for (j = 0; j < map[i].length; j++) {
                if (map[i][j].getSymbol() == key) {
                    this.tile = map[i][j];
                }
            }
        }
    }

    public void dealDmg(GameCharacter enemy) {
        //TODO
    }

    public void takeDmg(float damage) {
        this.health -= damage;
    }
}