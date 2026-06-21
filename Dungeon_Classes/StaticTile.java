//StaticTile subclass
package Dungeon_classes;

public class StaticTile extends Tile {
    //attributes
    private char type;

    //constructor
    public StaticTile(int x, int y, char symbol, boolean passable, boolean dealsDmg, int damage, char type) {
        super(x, y, symbol, passable, dealsDmg, damage);
        this.type = type;
    }

    //getters/setters
    public char getType() {
        return this.type;
    }

    public void setType(char type) {
        this.type = type;
    }
}