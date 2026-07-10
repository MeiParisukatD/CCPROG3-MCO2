//Tile class
package Dungeon_Classes;

public class Tile {
    //attribute
    protected int x;
    protected int y;
    protected char symbol;
    protected boolean passable;
    protected boolean destructible;
    protected float damage;

    //constructors
    public Tile(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.assignProperties();
    }

    //copy constructor
    public Tile(Tile refTile) {
        this.x = refTile.getX();
        this.y = refTile.getY();
        this.symbol = refTile.getSymbol();
        this.assignProperties();
    }

    //getters/setters 

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

    public char getSymbol() {
        return this.symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
        this.assignProperties();
    }

    public boolean isPassable() {
        return this.passable;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    public boolean isDestructible() {
        return this.destructible;
    }

    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    //additional methods
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