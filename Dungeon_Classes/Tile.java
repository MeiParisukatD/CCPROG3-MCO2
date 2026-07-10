//Tile class
package Dungeon_Classes;

/**
 * Represents a fundamental structural unit or coordinate node within the dungeon grid map.
 * Tracks individual positioning coordinates, symbol identifiers, passability metrics, 
 * breakable traits, and environmental hazard damage weights.
 * 
 * @author Katigbak and Porciuncula
 * @version 1.0
 */
public class Tile {
    //attribute
    /** The horizontal grid row index of the tile. */
    protected int x;
    /** The vertical grid column index of the tile. */
    protected int y;
    /** The single character indicator tracking the structural identity layout. */
    protected char symbol;
    /** Flag identifying whether an entity can freely step onto this coordinate node. */
    protected boolean passable;
    /** Flag identifying whether this map component can be broken down or collected. */
    protected boolean destructible;
    /** The value of environmental hazard damage dealt to a character upon entering. */
    protected float damage;

    //constructors
    /**
     * Constructs a tile element setting up specific grid values, map representation characters, 
     * and initializes its movement property configurations.
     *
     * @param x the horizontal row coordinate position
     * @param y the vertical column coordinate position
     * @param symbol the character symbol defining the node template type
     */
    public Tile(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.assignProperties();
    }

    //copy constructor
    /**
     * Constructs a new tile by copying attributes and values out of an existing reference node.
     *
     * @param refTile the source structural tile instance to duplicate
     */
    public Tile(Tile refTile) {
        this.x = refTile.getX();
        this.y = refTile.getY();
        this.symbol = refTile.getSymbol();
        this.assignProperties();
    }

    //getters/setters 

    /**
     * Retrieves the row position index of this tile.
     *
     * @return the row position index
     */
    public int getX() {
        return this.x;
    }

    /**
     * Updates the row position index of this tile.
     *
     * @param x the new row position index to apply
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Retrieves the column position index of this tile.
     *
     * @return the column position index
     */
    public int getY() {
        return this.y;
    }

    /**
     * Updates the column position index of this tile.
     *
     * @param y the new column position index to apply
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Retrieves the visual symbol character identifier of this tile.
     *
     * @return the visual symbol character identifier
     */
    public char getSymbol() {
        return this.symbol;
    }

    /**
     * Alters the visual symbol configuration and triggers a state property reassignment.
     *
     * @param symbol the replacement type character assignment
     */
    public void setSymbol(char symbol) {
        this.symbol = symbol;
        this.assignProperties();
    }

    /**
     * Checks if characters can move across this space.
     *
     * @return true if characters can move across this space, false otherwise
     */
    public boolean isPassable() {
        return this.passable;
    }

    /**
     * Updates the path verification flag state for this tile.
     *
     * @param passable the new path verification flag state
     */
    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    /**
     * Checks if the node can be broken or extracted.
     *
     * @return true if the node can be broken or extracted, false otherwise
     */
    public boolean isDestructible() {
        return this.destructible;
    }

    /**
     * Updates the modification constraint status fro this tile.
     * 
     * @param destructible the new modification constraint status
     */
    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }

    /**
     * Retrieves the value of environmental penalty damage associated with this element.
     *
     * @return the value of environmental penalty damage associated with this element
     */
    public float getDamage() {
        return this.damage;
    }

    /**
     * Updates the penalty scale mapping to this node.
     *
     * @param damage the updated penalty scale mapping to this node
     */
    public void setDamage(float damage) {
        this.damage = damage;
    }

    //additional methods
    /**
     * Evaluates the active character symbol signature to supply a matching ANSI text 
     * formatting escape color code for terminal output rendering.
     *
     * @return a string tracking the target terminal layout formatting color sequence
     */
    public String assignColor() {
        String color = "\u001B[0m"; //no effect

        switch(this.symbol) {
            case 'w': //water tiles
                color = "\u001B[38;5;81m"; //blue
                break;
            case 'h': //heat tiles
                color = "\u001B[38;5;214m"; //orange
                break;
            case 'T': //treasure tiles
                color = "\u001B[38;5;119m"; //green
                break;
            case 'E': //exit tiles
            case 'g':
                color = "\u001B[38;5;227m"; //yellow
                break;
            case 'I': //item tiles
                color = "\u001B[38;5;158m"; //turquoise
            default:
                break;
        }

        return color;
    }

    /**
     * Configures the path passability, modification criteria, and default floor hazard damage metrics 
     * by resolving structural parameters mapped to individual entity symbols.
     */
    public void assignProperties() {
        switch(this.symbol) {
            case '.': //passable tiles
                this.passable = true;
                this.destructible = false;
                this.damage = 0.0f;
                break;
            case '*': //dungeon borders
                this.passable = false;
                this.destructible = false;
                this.damage = 0.0f;
                break;
            case 'v': //wall tiles 
                this.passable = false;
                this.destructible = true;
                this.damage = 0.0f;
                break;
            case 'x': //spike tiles
                this.passable = false;
                this.destructible = true;
                this.damage = 0.5f;
                break;
            case 'w': //water tiles
                this.passable = false;
                this.destructible = false;
                this.damage = 0.0f;
                break;
            case 'h': //heat tiles
                this.passable = true;
                this.destructible = false;
                this.damage = 1.0f;
                break;
            case 'T': //treasure tiles
                this.passable = false;
                this.destructible = true;
                this.damage = 0.0f;
                break;
            case 'g': //gold tiles
                this.passable = true;
                this.destructible = true;
                this.damage = 0.0f;
                break;
            case 'I': //item tiles
                this.passable = true;
                this.destructible = true;
                this.damage = 0.0f;
                break;
            case 'E': //exit tiles
                this.passable = true;
                this.destructible = false;
                this.damage = 0.0f;
                break;
            case 'Y': //Yohane tile
                break;
            default:
                System.out.println("[!] Invalid tile symbol.");
                System.out.println("Row " + x + ": " + y);
                break;
        }
    }
}

// case 'L': //Lailaps tile
            //     color = "\u001B[38;5;153m"; //blue
            //     break;