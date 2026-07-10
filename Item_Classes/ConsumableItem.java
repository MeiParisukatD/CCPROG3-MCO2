package Item_Classes;

import Character_Classes.PlayableChar;

public class ConsumableItem extends Item {
    private float healAmount;

    public ConsumableItem(String name, float healAmount) {
        super(name);
        this.healAmount = healAmount;
    }
    
    public ConsumableItem(String name, int price, float healAmount) {
        super(name, price);
        this.healAmount = healAmount;
    }

    public float getHealAmount() {
        return healAmount;
    }

    public void setHealAmount(float healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public boolean use(PlayableChar player) {
        return player.heal(healAmount);
    }
}
