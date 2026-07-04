package Item_Classes;

import Character_Classes.PlayableChar;

public class UpgradeItem extends Item {
    public UpgradeItem(int id, String name, int price,
                       boolean isAvailable) {

        super(id, name, price, isAvailable);
    }

    @Override
    public void use(PlayableChar Yohane) {

        switch(id) {

            case 3:
                Yohane.setSpikeImmune(true);
                break;

            case 4:
                Yohane.setBatDamageReduction(true);
                break;

            case 5:
                Yohane.setWalkWater(true);
                Yohane.setHeatImmune(true);
                break;

            case 6:
            case 7:
            case 8:
                Yohane.increaseMaxHealth(1);
                break;
        }

    }
}
