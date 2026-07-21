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

    /** Shared reader utility scanning string tokens input stream values from standard system consoles. */
    public static Scanner s = new Scanner(System.in);
    private static boolean shopUnlocked = false;
    private static PlayableChar Yohane;

    /**
     * Private constructor to prevent instantiation of utility main runner class.
     */
    private Game() {
        // Utility class
    }
    
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

            System.out.println("\n************************************************");
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
        Yohane = new PlayableChar("Yohane", 3, 1, null);
        
        Floor[] floors = new Floor[1];
        floors[0] = new Floor(1);

        NPChar hanamaru = new NPChar("Hanamaru Kunikida", null, null);
        
        Dungeon dungeon = new Dungeon("Shougetsu Confectionary", 1, 1, floors, hanamaru);

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

            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("\nLailaps: Yohane! Where should we go now?\n");

            displayStats(Yohane);

            System.out.println();
            String avail = dungeon.getMember().getSaved() ? "X" : "1";
            System.out.println("[" + avail + "] Visit " + dungeon.getName());
            System.out.println("[I] Inventory");
            System.out.println("[Q] Quit");
            if (shopUnlocked) {
                System.out.println("[H] Hanamaru's Store");
            }
            System.out.print("\nChoice: ");

            try {
                choice = Character.toLowerCase(
                        s.nextLine().charAt(0));
            } catch (StringIndexOutOfBoundsException e) {
                choice = 'x';
            }

            switch (choice) {
                case '1':
                    if (!dungeon.isCompleted(Yohane)) {
                        runDungeon(Yohane, dungeon);
                    }
                    break;
                case 'i':
                    displayInventory(Yohane);
                    break;

                case 'h':
                    if (shopUnlocked) {
                        displayShop();
                    }
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

        System.out.println("\nViewing Inventory");

        displayStats(Yohane);

        System.out.println("\nItems:");

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

            if (dungeon.isCompleted(Yohane)) {
                displayDungeonClearScene(dungeon);
                shopUnlocked = true;
                break;
            }
        } while (!dungeon.gameOver(Yohane) && !dungeon.isCompleted(Yohane));

        //resets Yohane's health if dungeon ends in death
        //resets Yohane's health if dungeon ends in death
        if (dungeon.gameOver(Yohane)) {
            String RED, RESET;
            RED = "\u001B[38;5;196m";
            RESET = "\u001B[0m";

            System.out.println(RED + "You Died!" + RESET );
            System.out.println("Killed by " + RED + Yohane.getCauseOfDeath() + RESET);

            Yohane.setHealth(3);
            //regen floor
            dungeon.getFloors()[dungeon.getCurFloor()-1].generateFloor(dungeon.getFloors()[dungeon.getCurFloor()-1].getFile());
        }
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

        System.out.println("\nDungeon #" + dungeon.getDungeonNum() + ": " + dungeon.getName());
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
     * Outputs the dialogue for unlocking Hanamaru's shop after saving Hanamaru
     *
     * @param dungeon the dungeon cleared
     */
    public static void displayDungeonClearScene(Dungeon dungeon) {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("\n************************************************************");
        System.out.println("                      Dungeon Cleared!                      ");
        System.out.println("              "+dungeon.getName()+" Completed!            ");
        System.out.println("                 "+dungeon.getMember().getName()+" rescued!                 ");
        System.out.println("\nUnlocked: Hanamaru's Store Now Available!\n");
        System.out.println("************************************************************\n");
        System.out.println("Hanamaru: Yohane-chan, zura! You're here!");
        System.out.println("Yohane: Hanamaru! We have to get out of here quickly!");
        System.out.println("Hanamaru: Oh? I was wondering what this place was and why there \nare bats everywhere, zura!");
        System.out.println("Yohane: Seems like there's a Siren that wants to take your voices \nand is holding you in this dimension so that your \ncounterparts in the real world can't sing!");
        System.out.println("Hanamaru: Really? That sounds terrifying, zura. What have we \ngot to do?");
        System.out.println("Yohane: First, we have to get out of here, Zuramaru! I know the \nway out.");
        System.out.println("Hanamaru: Lead the way, zura!");
        System.out.println("\nPress Enter to return...");
        s.nextLine();
    }

    public static void displayShop() {
    Shop shop = new Shop();
    String input;
    String statusMessage = "";

    do {
        // Clear terminal screen
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // 1. Display shop items along with player's gold and last transaction message
        shop.displayItems(null, Yohane, statusMessage);

        // 2. Read user choice
        input = s.nextLine().trim();

        // Check if user wants to return/exit shop
        if (input.equalsIgnoreCase("r") || input.equalsIgnoreCase("0")) {
            break;
        }

        try {
            int choice = Integer.parseInt(input);
            
            // 3. Process purchase
            boolean success = shop.sellItem(choice, Yohane);

            if (success) {
                statusMessage = "Successfully purchased item!";
            } else {
                statusMessage = "Purchase failed! Check your gold balance or duplicate items.";
            }

        } catch (NumberFormatException e) {
            statusMessage = "Invalid selection. Please enter a valid number or 'R'.";
        }

    } while (true);
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

    /**
     * Renders a placeholder block notification layout alerting that attribute inspections 
     * are still undergoing engineering developments.
     */
    public static void displayStatus(){
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("\nStatus not implemented yet\n");

        System.out.println("\nPress Enter to return...");
        s.nextLine();
    }
}
