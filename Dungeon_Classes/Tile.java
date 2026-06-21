//Tile class
package Dungeon_classes;

public class Tile {
    //attribute
    protected int x;
    protected int y;
    protected char symbol;
    protected boolean passable;
    protected boolean dealsDmg;
    protected int damage;

    //constructors
    public Tile(int x, int y, char symbol, boolean passable, boolean dealsDmg, int damage) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.passable = passable;
        this.dealsDmg = dealsDmg;
        this.damage = damage;
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

    public boolean getPassable() {
        return this.passable;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    public boolean getDealsDmg() {
        return this.dealsDmg;
    }

    public void setDealsDmg(boolean dealsDmg) {
        this.dealsDmg = dealsDmg;
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
}
