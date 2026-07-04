package Item_Classes;

import Character_Classes.PlayableChar;

public class PassiveItem extends Item {

    public PassiveItem(int id, String name, int price, boolean isAvailable) {
        super(id, name, price, isAvailable);
    }

    @Override
    public void use(PlayableChar Yohane) {

        switch(id) {

            case 9:
                Yohane.setHasIceCream(true);
                break;
        }

    }
}
