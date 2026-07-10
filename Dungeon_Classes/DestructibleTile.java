//DestructibleTile subclass
package Dungeon_Classes;
import Item_Classes.*;

public class DestructibleTile extends Tile {
    //attributes
    private boolean treasure; //does the item hold treasure
    private int goldDrop; //amount of gold dropped when destroyed
    private Item itemDrop; //item dropped when destroyed

    //constructor
    public DestructibleTile(int x, int y, char symbol, int goldDrop, Item itemDrop, boolean treasure) {
        super(x, y, symbol);
        this.treasure = treasure;
        this.goldDrop = goldDrop;
        this.itemDrop = itemDrop;
        super.assignProperties();
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
    public void dropTreasure() {
        int rand = (int)(Math.random() * 2);

        if (rand == 0) { //randomized to item
            this.symbol = 'I';
        } else { //randomized to gold
            this.symbol = 'g';
        }

        super.assignProperties();
    }

    public void assignProperties() {
        super.assignProperties();

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