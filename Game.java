//Playable file
import Character_classes.*;
import Dungeon_classes.*;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
    public static void main(String[] args) {
        // PlayableChar Yohane, Lailaps;
        // initializeGame();

        //test
        Floor floor = new Floor(1);
        floor.displayMap();
    }

    // public initializeGame() {
    //     Yohane = new PlayableChar("Yohane", 3, 1,  null);
    //     Lailaps = new PlayableChar("Lailaps", 1, 0,  "Yohane! Where should we go to now?");
    // }

    public void displayMainMenu() {
        //TODO
    }

    public void displayDungeonMenu(Dungeon dungeon) {
        System.out.println("Dungeon #" + dungeon.getDungeonNum + ": " + dungeon.getName);
        System.out.println("Floor " + dungeon.getCurFloor + " of " + dungeon.getNumFloors);

        System.out.println();
        displayStats();

        System.out.println();
        dungeon.getFloors[dungeon.getCurFloor].displayMap();

        System.out.println();
        System.out.println("Where to, Yohane? ");
    }

    public void displayFloorMenu() {
        //TODO
    }

    public void displayBattleMenu() {
        //TODO
    }

    public void displayStats(PlayableChar Yohane) {
        System.out.print("HP: " + Yohane.getCurHealth() + "/" + Yohane.getHealth());
        System.out.println("\t\tTotal Gold: " + Yohane.getGoldOwned() + " GP");

        int quantity = Collections.frequency(Yohane.getInventory(), Yohane.getCurItem().getName());
        String displayQuantity = (quantity > 0) ?  "(" + quantity + ")" : "";
        System.out.println("Item on Hand: " + (Yohane.getCurItem() != null ? Yohane.getCurItem().getName() + displayQuantity : "N/A"));
    }

    public void displayInventory(PlayableChar Yohane) {
        //TODO
    }
}