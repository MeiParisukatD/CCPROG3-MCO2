//Playable file
import Character_classes.*;
import Dungeon_classes.*;
import Item_classes.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.Scanner;

public class Game {

    public static Scanner s = new Scanner(System.in);
    public static void main(String[] args) {
        displayMainMenu();
    }

    public static void displayMainMenu() {
        char choice;

        do {

            System.out.println("************************************************");
            System.out.println("*             Yohane The Parhelion!            *");
            System.out.println("*        The Siren in the Mirror World!        *");
            System.out.println("************************************************");
            System.out.println("        [N]ew Game");
            System.out.println("        [S]tatus");
            System.out.println("        [Q]uit");
            System.out.print("\nYour choice: ");

            choice = Character.toLowerCase(
                    s.nextLine().charAt(0));

            switch(choice) {

                case 'n':
                    startGame();
                    break;

                case 's':
                    displayStatus();
                    break;
            }

        } while(choice != 'q');
    }

    public static void displayGameMenu(PlayableChar Yohane, Dungeon dungeon){
        char choice;

        do {

            System.out.println();
            System.out.println("Lailaps: Yohane! Where should we go now?");
            System.out.println();

            displayStats(Yohane);

            System.out.println();
            System.out.println("[1] Visit Test Dungeon");
            System.out.println("[I] Inventory");
            System.out.println("[Q] Quit");
            System.out.print("\nChoice: ");

            try {
                choice = Character.toLowerCase(
                        s.nextLine().charAt(0));
            } catch (StringIndexOutOfBoundsException e) {
                choice = 'x';
            }

            switch (choice) {

                case '1':
                    runDungeon(Yohane, dungeon);
                    break;

                case 'i':
                    displayInventory(Yohane);
                    break;
            }

        } while (choice != 'r');
    }

    public static void displayInventory(PlayableChar Yohane){
        System.out.println();
        System.out.println("Viewing Inventory");

        displayStats(Yohane);

        System.out.println();
        System.out.println("Items:");

        if (Yohane.getInventory().isEmpty()) {
            System.out.println("No items.");
        } else {
            for (Item item : Yohane.getInventory()) {
                System.out.println("- " + item.getName());
            }
        }

        System.out.println();
        System.out.println("Press Enter to return...");
        s.nextLine();
    }

    public static void startGame(){
        PlayableChar Yohane = new PlayableChar("Yohane", 3, 1, null);
        

        Floor[] floors = new Floor[3];
        floors[0] = new Floor(1);
        floors[1] = new Floor(2);
        floors[2] = new Floor(3);
        
        Dungeon dungeon = new Dungeon("Test Dungeon", 1, 3, floors);

        displayGameMenu(Yohane, dungeon);
    }

    public static void runDungeon(PlayableChar Yohane, Dungeon dungeon){
        boolean firstMove = true;
        char input;

        do {
            int index = dungeon.getCurFloor() - 1;
            Floor currentFloor = dungeon.getFloors()[index];

            //spawn Yohane
            if (firstMove) {
                int x, y;
                Yohane.findCharTile(currentFloor.getMap());

                x = Yohane.getTile().getX();
                y = Yohane.getTile().getY();

                currentFloor.getMap()[x][y] = new Tile(x, y, '.');
                firstMove = false;
            }

            Game.displayDungeonMenu(dungeon, index, Yohane);

            try {
                input = s.nextLine().charAt(0);
                input = Character.toLowerCase(input);
            } catch (StringIndexOutOfBoundsException e) {
                input = 'x';
            };

            Yohane.incrementTurn();

            if ("wasd".contains(Character.toString(input))) {
                Yohane.move(input, currentFloor);

                //prompts action from enemy characters
                Iterator<EnemyChar> it = currentFloor.getEnemies().iterator();
                while (it.hasNext()){
                    EnemyChar enemy = it.next();
                    if (enemy.charDeath()) {
                        enemy.dropGold(currentFloor);
                        it.remove();
                    } else {
                        enemy.move(currentFloor, Yohane);
                    }
                }
            }
        } while (input != 'q');
    }

    public static void displayDungeonMenu(Dungeon dungeon, int index, PlayableChar Yohane) {
        System.out.println("Dungeon #" + dungeon.getDungeonNum() + ": " + dungeon.getName());
        System.out.println("Floor " + dungeon.getCurFloor() + " of " + dungeon.getNumFloors());

        System.out.println();
        Game.displayStats(Yohane);

        System.out.println();
        dungeon.getFloors()[index].displayMap(Yohane);

        System.out.println();
        System.out.println("Turn Counter: " + Yohane.getTurnCount());
        System.out.print("Where to, Yohane? ");
    }

    public static void displayStats(PlayableChar Yohane) {
        System.out.print("HP: " + Yohane.getHealth() + "/" + Yohane.getMaxHealth());
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

    public static void displayStatus(){
        System.out.println("Status not implemented yet");
    }
}