//Playable file
import Character_Classes.*;
import Dungeon_Classes.*;
import Item_Classes.*;

import java.util.Iterator;
import java.util.Collections;
import java.util.Scanner;

/**
 * Serves as the main orchestrator for "Yohane The Parhelion! The Siren in the Mirror World!".
 * Controls state transitions across menus, initializes level layers, sequences entity combat or 
 * structural exploration loop tracking, and processes text-based keypad keystroke actions.
 * 
 * @author Katigbak and Porciuncula
 * @version 1.0
 */
public class Game {

    /**
     * Private constructor to prevent instantiation of utility main runner class.
     */
    private Game() {
        // Utility class
    }
    
    /** Shared reader utility scanning string tokens input stream values from standard system consoles. */
    public static Scanner s = new Scanner(System.in);
    
    /**
     * Entry programmatic hook establishing execution boundaries and forwarding flow controls 
     * straight into home menu layouts.
     *
     * @param args array parameters passed from external CLI executions
     */
    public static void main(String[] args) {
        displayMainMenu();
    }

    /**
     * Loops a terminal presentation displaying standard introduction sequences, 
     * forwarding triggers to configure game models, evaluate status trackers, or terminate.
     */
    public static void displayMainMenu() {
        char choice;

        do {
            System.out.print("\033[H\033[2J");
            System.out.flush();

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

    /**
     * Allocates memory for user references, maps sequence structural boundaries, 
     * and sets up the test exploration loop scenario.
     */
    public static void startGame(){
        PlayableChar Yohane = new PlayableChar("Yohane", 3, 1, null);
        
        Floor[] floors = new Floor[1];
        floors[0] = new Floor(1);
        // floors[1] = new Floor(2);
        // floors[2] = new Floor(3);
        
        Dungeon dungeon = new Dungeon("Test Dungeon", 1, 1, floors);

        displayGameMenu(Yohane, dungeon);
    }

    /**
     * Loops choices allowing the character to transition into physical environments, 
     * check collected storage structures, or yield activities.
     *
     * @param Yohane  the user character instance model tracking state parameters
     * @param dungeon the active complex mapping level configurations
     */
    public static void displayGameMenu(PlayableChar Yohane, Dungeon dungeon){
        char choice;

        do {
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
        } while (choice != 'q');
    }

    /**
     * Pauses procedural states to write out tabular listings capturing metadata naming 
     * parameters for all held item entities currently tracked inside storage.
     *
     * @param Yohane the user character reference containing target container collections
     */
    public static void displayInventory(PlayableChar Yohane){
        System.out.print("\033[H\033[2J");
        System.out.flush();

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

    /**
     * Manages step sequences, user input updates, turn increments, enemy actions, 
     * and victory state verifications inside an active floor scenario.
     *
     * @param Yohane  the player character traversing the dungeon environment
     * @param dungeon the dungeon container hosting the exploration map levels
     */
    public static void runDungeon(PlayableChar Yohane, Dungeon dungeon){
        boolean firstMove = true;
        char input;

        do {
            int index = dungeon.getCurFloor() - 1;
            Floor currentFloor = dungeon.getFloors()[index];

            //spawn Yohane on the first move
            if (firstMove) {
                int x, y;
                Yohane.findCharTile(currentFloor.getMap());
                //assigns coordinates to Yohane
                x = Yohane.getX();
                y = Yohane.getY();
                //sets underlying tile to passable tile
                currentFloor.getMap()[x][y] = new Tile(x, y, '.');
                firstMove = false;
            }

            Game.displayDungeonMenu(dungeon, index, Yohane);

            try { //in case user returns without input
                input = s.nextLine().charAt(0);
                input = Character.toLowerCase(input);
            } catch (StringIndexOutOfBoundsException e) {
                input = 'x';
            };

            Yohane.incrementTurn(); //all actions are counted as a 'turn'

            if ("wasd".contains(Character.toString(input))) {
                Yohane.move(input, currentFloor);
            } //if valid direction, moves Yohane
            else if (input == ' ') {
                Yohane.useItem();
            }
            else if (input == '[') {
                Yohane.prevItem();
            }
            else if (input == ']') {
                Yohane.nextItem();
            }

            //prompts action from enemy characters
            Iterator<EnemyChar> it = currentFloor.getEnemies().iterator();
            while (it.hasNext()){
                EnemyChar enemy = it.next();
                if (enemy.charDeath()) { //if enemy is dead
                    enemy.dropGold(currentFloor);
                    it.remove();
                } else { //if enemy is alive
                    enemy.move(currentFloor, Yohane);
                }
            }

            //check if floor is complete
            if (currentFloor.completeFloor(Yohane)) {
                dungeon.incrementCurFloor();
            }
        } while (!dungeon.gameOver(Yohane) && !dungeon.isCompleted(Yohane));
    }

    /**
     * Refreshes display lines tracking operational statistics, mapping metadata indexes, 
     * and delegates control to paint structural tiles.
     *
     * @param dungeon the map tracking hub database reference
     * @param index the calculated internal floor lookup pointer
     * @param Yohane the entity character to render contextually
     */
    public static void displayDungeonMenu(Dungeon dungeon, int index, PlayableChar Yohane) {
        System.out.print("\033[H\033[2J");
        System.out.flush();

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

    /**
     * Outputs text metrics formatting numerical properties like health status limits, 
     * gold storage indexes, and calculates holding item structural frequencies.
     *
     * @param Yohane the targeted player reference being inspected
     */
    public static void displayStats(PlayableChar Yohane) {
        System.out.print("\033[H\033[2J");
        System.out.flush();

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

    /**
     * Renders a placeholder block notification layout alerting that attribute inspections 
     * are still undergoing engineering developments.
     */
    public static void displayStatus(){
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("Status not implemented yet");

        System.out.println();
        System.out.println("Press Enter to return...");
        s.nextLine();
    }
}
