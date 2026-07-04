//Playable file
import Character_classes.*;
import Dungeon_classes.*;
import Item_classes.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        // PlayableChar Yohane, Lailaps;
        // initializeGame();

        //test
        PlayableChar Yohane = new PlayableChar("Yohane", 3, 1, null);
        boolean firstMove = true;

        Floor[] floors = new Floor[3];
        floors[0] = new Floor(1);
        floors[1] = new Floor(2);
        floors[2] = new Floor(3);
        
        Dungeon dungeon = new Dungeon("Test Dungeon", 1, 3, floors);
        Scanner s = new Scanner(System.in);
        char input;

        do {
            if (firstMove) {
                Yohane.findCharTile(dungeon.getFloors()[dungeon.getCurFloor()].getMap());
                firstMove = false;
            }

            Game.displayDungeonMenu(dungeon, Yohane);
            input = s.nextLine().charAt(0);
            input = Character.toLowerCase(input);

            if (input != 'x' && input != 'q') {
                Yohane.move(input, dungeon.getFloors()[dungeon.getCurFloor()]);
            }

            Yohane.setTurnCount(Yohane.getTurnCount() + 1);
        } while (input != 'q');
    }

    // public initializeGame() {
    //     Yohane = new PlayableChar("Yohane", 3, 1,  null);
    //     Lailaps = new PlayableChar("Lailaps", 1, 0,  "Yohane! Where should we go to now?");
    // }

    public static void displayMainMenu() {
        //TODO
    }

    public static void displayDungeonMenu(Dungeon dungeon, PlayableChar Yohane) {
        System.out.println("Dungeon #" + dungeon.getDungeonNum() + ": " + dungeon.getName());
        System.out.println("Floor " + dungeon.getCurFloor() + " of " + dungeon.getNumFloors());

        System.out.println();
        Game.displayStats(Yohane);

        System.out.println();
        dungeon.getFloors()[dungeon.getCurFloor()].displayMap();

        System.out.println();
        System.out.println("Turn Counter: " + Yohane.getTurnCount());
        System.out.print("Where to, Yohane? ");
    }

    public static void displayStats(PlayableChar Yohane) {
        System.out.print("HP: " + Yohane.getCurHealth() + "/" + Yohane.getHealth());
        System.out.println("\t\tTotal Gold: " + Yohane.getGoldOwned() + " GP");

        int quantity;
        String displayQuantity;

        try {
            quantity = Collections.frequency(Yohane.getInventory(), Yohane.getCurItem().getName());
            displayQuantity = (quantity > 0) ?  "(" + quantity + ")" : "";
            System.out.println("Item on Hand: " + Yohane.getCurItem().getName() + displayQuantity);
        } catch (NullPointerException e) {
            System.out.println("Item on Hand: N/A");
        }
    }
}