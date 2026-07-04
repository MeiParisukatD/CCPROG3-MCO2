package Item_Classes

import Character_Classes.PlayableChar;

public class Item {
    protected int id;
    protected String name;
    protected int price;
    protected int quantity;
    protected boolean isAvailable;

    protected Item(int id, String name, int price, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = 0;
        this.isAvailable = isAvailable;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean getAvailability() {
        return isAvailable;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAvailability(boolean availability) {
        this.isAvailable = availability;
    }

    public void use(PlayableChar Yohane) {
        // overridden by subclasses
    }
}