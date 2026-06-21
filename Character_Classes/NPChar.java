//NPChar subclass
package Character_classes;


public class NPChar extends Character {
    //attributes
    private Item item;
    private boolean saved;

    //constructor
    public NPChar(String name, String dialogue, Item item) {
        super(name, dialogue);
        this.item = item;
        this.saved = false;
    }

    //getters/setters
    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public boolean getSaved() {
        return this.saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}