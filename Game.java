//Playable file
import Character_classes.*;
import Dungeon_classes.*;
import Item_classes.*;

import java.util.ArrayList;
import java.util.Iterator;
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
        int i;

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

    // public initializeGame() {
    //     Yohane = new PlayableChar("Yohane", 3, 1,  null);
    //     Lailaps = new PlayableChar("Lailaps", 1, 0,  "Yohane! Where should we go to now?");
    // }

    public static void displayMainMenu() {
        //TODO
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
}