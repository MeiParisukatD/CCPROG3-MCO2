//GameCharacter class
package Character_Classes;
import Dungeon_Classes.*;

/**
 * Represents an active character entity in the Mirror World of Numazu.
 * Acts as the base class for Yohane, Lailaps, the Siren, and enemy Bats.
 * 
 * @author Katigbak and Porciuncula
 * @version 2.0
 */
public class GameCharacter {
    //attributes
    /** The name of the character or entity. */
    protected String name;
    /** The current hitpoints (HP). Reaching 0 triggers a defeat or Game Over. */
    protected float health;
    /** The attack power used to deal damage to opponents. */
    protected float attack;
    /** The X coordinate mapping the character's position on the 2D dungeon grid. */
    protected int x;
    /** The Y coordinate mapping the character's position on the 2D dungeon grid. */
    protected int y;
    /** The Floor instance that this character currently belongs to. */
    protected Floor floor;

    //constructor
    /**
     * Constructs a character with base stats and sets position to (0,0).
     * Optimized for PlayableChar.
     *
     * @param name the name of the character
     * @param health the initial health points
     * @param attack the base attack power
     */
    public GameCharacter(String name, float health, float attack) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.x = 0;
        this.y = 0;
        this.floor = null;
    }

    /**
     * Constructs a character at a specified grid position. Optimized for EnemyChar.
     *
     * @param name the name of the character
     * @param health the initial health points
     * @param attack the base attack power
     * @param x the starting X coordinate
     * @param y the starting Y coordinate
     */
    public GameCharacter(String name, float health, float attack, int x, int y) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.x = x;
        this.y = y;
        this.floor = null;
    }

    /**
     * Constructs a non-combatant character (e.g., Shopkeeper) at (0,0), with
     * health and attack defaulted to 0.
     * Optimized for NPChar.
     *
     * @param name the name of the character
     */
    public GameCharacter(String name) {
        this.name = name;
        this.health = this.attack = 0;
        this.x = 0;
        this.y = 0;
        this.floor = null;
    }

    //getters/setters
    /**
     * Retrieves the display name identifying the character entity.
     * 
     * @return the character's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Changes the character's display name to a new identity string.
     * 
     * @param name the new name string
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the character's remaining life or durability points.
     * 
     * @return the current health value
     */
    public float getHealth() {
        return this.health;
    }

    /**
     * Directly updates the character's current pool of health points.
     * 
     * @param health the new health value
     */
    public void setHealth(float health) {
        this.health = health;
    }

    /**
     * Retrieves the base offensive strength score used during combat interactions.
     * 
     * @return the current attack power
     */
    public float getAttack() {
        return this.attack;
    }

    /**
     * Modifies the offensive damage potential of the character.
     * 
     * @param attack the new attack value
     */
    public void setAttack(float attack) {
        this.attack = attack;
    }

    /**
     * Gets the current horizontal placement index on the world map grid.
     * 
     * @return the current X grid coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * Updates the character's horizontal placement index to a new map grid position.
     * 
     * @param x the new X coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the current vertical placement index on the world map grid.
     * 
     * @return the current Y grid coordinate
     */
    public int getY() {
        return this.y;
    }

    /**
     * Updates the character's vertical placement index to a new map grid position.
     * 
     * @param y the new Y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Assigns the Floor instance that this character belongs to.
     *
     * @param floor the Floor context to associate with this character
     */    
    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    /**
     * Retrieves the Floor instance that this character currently belongs to.
     *
     * @return the current Floor context
     */    
    public Floor getFloor() {
        return this.floor;
    }
    
    //additional methods
    /**
     * Checks if the character's health has reached 0 or less.
     *
     * @return true if health is less than or equal to 0, false otherwise
     */
    public boolean charDeath() {
        boolean isDead = false;

        if (this.health <= 0)
            isDead = true;

        return isDead;
    }

    /**
     * Deals damage to a target enemy based on this character's attack power.
     *
     * @param enemy the target GameCharacter to damage
     */
    public void dealDmg(GameCharacter enemy) {
        enemy.takeDmg(this);
    }

    /**
     * Deducts health from this character based on an attacking character's attack power.
     *
     * @param enemy the GameCharacter dealing the damage
     */
    public void takeDmg(GameCharacter enemy) {
        this.health -= enemy.getAttack();
    }

    /**
     * Deducts a fixed amount of health from this character, used for
     * non-combat sources such as trap or hazard damage.
     *
     * @param damage the amount of health to lose
     */ 
    public void takeDmg(float damage) {
        this.health -= damage;
    }

    /**
     * Calculates the adjacent tile coordinate based on an input direction.
     * Map controls correspond to: 0 (Up/W), 1 (Down/S), 2 (Left/A), 3 (Right/D),
     * and, when diagonal movement is enabled, 4 (upper left), 5 (upper right),
     * 6 (bottom left), and 7 (bottom right).
     *
     * @param direction integer value from 0 to 7 representing the movement direction
     * @return the destination Tile target
     */
    protected Tile nextTile(int direction) {
        int next_x, next_y;
        Tile next;

        next_x = this.x;
        next_y = this.y;

        switch (direction) {
            case 0: next_x--; break; //up
            case 1: next_x++; break; //down
            case 2: next_y--; break; //left
            case 3: next_y++; break; //right
            case 4: //upper left
                next_x--;
                next_y--;
                break;
            case 5: //upper right
                next_x--;
                next_y++;
                break;
            case 6: //bottom left
                next_x++;
                next_y--;
                break;
            case 7: //bottom right
                next_x++;
                next_y++;
                break;
        }

        next = floor.getMap()[next_x][next_y];
        return next;
    }

    /**
     * Retrieves the Tile located at the given grid coordinates on this
     * character's current floor.
     *
     * @param x the target X grid coordinate
     * @param y the target Y grid coordinate
     * @return the Tile found at the given coordinates
     */    
    protected Tile nextTile(int x, int y) {
        return this.floor.getMap()[x][y];
    }


    /**
     * Moves the character to the next tile if valid. 
     * If blocked by a breakable wall, this action handles the digging logic instead.
     *
     * @param direction movement direction index (0 to 7)
     * @param override  if the movement must be allowed regardless of normal validation rules
     */
    public void move(int direction, boolean override) {
        Tile next = this.nextTile(direction);

        if (floor.validateMove(next) || override) {
            this.x = next.getX();
            this.y = next.getY();
        }
    }
}