package Item_Classes;

import Character_Classes.PlayableChar;

public class ConsumableItem extends Item {
    private float healAmount;

    public ConsumableItem(int id, String name, int price, float healAmount, boolean isAvailable) {
        super(id, name, price, isAvailable);
        this.healAmount = healAmount;
    }

    @Override
    public void use(PlayableChar Yohane) {

        if (quantity <= 0)
            return;

        Yohane.heal(healAmount);
        quantity--;
    }
}
