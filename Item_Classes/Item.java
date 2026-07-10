//temp item class
package Item_Classes;

import Character_Classes.PlayableChar;

public class Item {
    //attributes
    protected String name;
    protected int price;

    public Item(String name) {
        this.name = name;
        this.price = 0;
    }
    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public boolean use(PlayableChar player){
        //override
        return false; 
    }
}