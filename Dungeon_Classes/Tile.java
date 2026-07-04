//Tile class
package Dungeon_classes;

public class Tile {
    //attribute
    protected int x;
    protected int y;
    protected char symbol;
    protected boolean passable;
    protected boolean destructible;
    protected int damage;

    //constructors
    public Tile(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.passable = true;
        this.destructible = true;
        this.damage = 0;
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

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
    

    //additional methods
    public String assignColor() {
        //TODO
        return "";
    }

    public String assignProperties() {
        //TODO
        return "";
    }
}
