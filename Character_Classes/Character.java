//Character class
package Character_Classes;

public class Character {
    //attributes
    protected String name;
    protected int health;
    protected int attack;
    protected String dialogue;

    //constructor
    public Character(String name, int health, int attack, String dialogue) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.dialogue = dialogue;
    }

    public Character(String name, int health, int attack) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.dialogue = null;
    }

    public Character(String name, String dialogue) {
        this.name = name;
        this.health = this.attack = 0;
        this.dialogue = dialogue;
    }

    //getters/setters
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttack() {
        return this.attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public String getDialogue() {
        return this.dialogue;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

    //additional methods
    public boolean evaluateTile(Tile tile) {
        //TODO
    }

    public boolean charDeath() {
        boolean isDead = false;

        if (this.health <= 0)
            isDead = true;

        return isDead;
    }

    public void moveTile(char direction, Tile[][] map) {
        //TODO
    }

    public void dealDmg(Character enemy) {
        //TODO
    }

    public int takeDmg(int damage) {
        //TODO
    }
}