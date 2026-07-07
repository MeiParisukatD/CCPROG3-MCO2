//PlayableChar subclass
package Character_classes;

import Item_classes.*;
import java.util.ArrayList;

import Dungeon_classes.Floor;
import Dungeon_classes.Tile;

public class PlayableChar extends GameCharacter {
    //attributes
    private int goldOwned;
    private float curHealth;
    private int turnCount;
    private ArrayList<Item> inventory;
    private Item curItem;

    //constructor
    public PlayableChar(String name, float health, float attack, String dialogue) {
        super(name, health, attack, dialogue);
        this.goldOwned = 0;
        this.turnCount = 0;
        this.curHealth = health;
        this.inventory = new ArrayList<Item>();
        this.curItem = null;
    }

    //getters/setters
    public int getGoldOwned() {
        return this.goldOwned;
    }

    public void setGoldOwned(int goldOwned) {
        this.goldOwned = goldOwned;
    }

    public float getCurHealth() {
        return this.curHealth;
    }

    public void setCurHealth(int curHealth) {
        this.curHealth = curHealth;
    }

    public int getTurnCount() {
        return this.turnCount;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public ArrayList<Item> getInventory() {
        return this.inventory;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public Item getCurItem() {
        return this.curItem;
    }

    public void setCurItem(Item curItem) {
        this.curItem = curItem;
    }

    //additional methods
    public boolean switchItem(int index) {
        //TODO
        return false;
    }

    public boolean useItem() {
        //TODO
         return false;
    }

    public boolean buyItem(Item purchase) {
        //TODO
         return false;
    }

    public boolean discardItem(int index) {
        //TODO
         return false;
    }

    public boolean heal(float amount) {
        //TODO
         return false;
    }

    @Override
    public void move(char direction, Floor floor) {
        int x = tile.getX();
        int y = tile.getY();

        switch (direction) {
            case 'w': x--; break;
            case 's': x++; break;
            case 'd': y++; break;
            case 'a': y--; break;
            default:
                System.out.println("[!] Invalid direction.");
                return;
        }

        Tile next = floor.getMap()[x][y];

        // Attack instead of moving if there's an enemy
        EnemyChar enemy = floor.findEnemy(x, y);

        if (enemy != null) {


            dealDmg(enemy);

            if (enemy.charDeath()) {
                setGoldOwned(getGoldOwned() + enemy.getGoldDrop());
                floor.removeEnemy(enemy);
            }

            return;
        }

        if (floor.validateMove(next)) {
            floor.moveCharacter(tile, next, this);
        }
    }

    @Override
    public void takeDmg(float damage) {
        curHealth -= damage;

        if (curHealth < 0)
            curHealth = 0;
    }
}