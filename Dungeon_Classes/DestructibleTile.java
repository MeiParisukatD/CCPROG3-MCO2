//DestructibleTile subclass
package Dungeon_classes;
import Item_classes.*;

public class DestructibleTile extends Tile {
    //attributes
    private boolean treasure;
    private int goldDrop;
    private Item itemDrop;

    //constructor
    public DestructibleTile(int x, int y, char symbol, int goldDrop, Item itemDrop, boolean treasure) {
        super(x, y, symbol);
        this.treasure = treasure;
        this.goldDrop = goldDrop;
        this.itemDrop = itemDrop;
    }

    //copy constructor
    public DestructibleTile(Tile parentTile) {
        super(parentTile.getX(), parentTile.getY(), parentTile.getSymbol());
        assignProperties();
    }

    //getters/setters
    public boolean getTreasure() {
        return this.treasure;
    }

    public void setTreasure(boolean treasure) {
        this.treasure = treasure;
    }

    public int getGoldDrop() {
        return this.goldDrop;
    }

    public void setGoldDrop(int goldDrop) {
        this.goldDrop = goldDrop;
    }

    public Item getItemDrop() {
        return this.itemDrop;
    }

    public void setItemDrop(Item itemDrop) {
        this.itemDrop = itemDrop;
    }

    //additional methods
    public void destroyTile() {
        //TODO
    }

    public void assignProperties() {
        switch(this.symbol) {
            case 'T': //treasure tiles
                this.treasure = true;
                this.goldDrop = (int)(Math.random() * 91 + 10); //random gold drop between 10 and 100
                this.itemDrop = new Item("Noppo Bread"); //TODO: implement item drop
                break;
            default:
                this.treasure = false;
                this.goldDrop = 0;
                this.itemDrop = null;
                break;
        }
    }
}