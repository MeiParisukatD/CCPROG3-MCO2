//DestructibleTile subclass
package Dungeon_classes;

public class DestructibleTile extends Tile {
    //attributes
    private boolean treasure;
    private int goldDrop;
    private Item itemDrop;

    //constructor
    public DestructibleTile(int x, int y, char symbol, boolean passable, boolean dealsDmg, int damage, int goldDrop, Item itemDrop, boolean treasure) {
        super(x, y, symbol, passable, dealsDmg, damage);
        this.treasure = treasure;
        this.goldDrop = goldDrop;
        this.itemDrop = itemDrop;
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

    public Item dropItem() {
        //TODO
    }

    public int dropGold() {
        //TODO
    }
}