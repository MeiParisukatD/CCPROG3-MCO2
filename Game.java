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
    private static boolean ongoingGame = false;
    private static boolean shopUnlocked = false;
    private static boolean bossUnlocked = false;
    private static boolean bossOngoing = false;
    private static boolean completed = false;
    private static boolean gameOver = false;
    private static int goldSpent = 0;
    private static int sirenDefeated = 0;
    private static int gameOvers = 0;

    // Central initializer
    private static Initialize init;

    // References pulled from initializer
    private static PlayableChar Yohane;
    private static PlayableChar Lailaps;
    private static Dungeon[] dungeons;
    private static Item[] items;
    private static NPChar[] npcs;

    /**
     * Private constructor to prevent instantiation of utility main runner class.
     */
    private Game() { /* Utility class */ }

    /**
     * Entry programmatic hook establishing execution boundaries and forwarding flow controls 
     * straight into home menu layouts.
     *
     * @param args array parameters passed from external CLI executions
     */
    public static void main(String[] args) {
        // Run initializer once
        initialize();

        // Start game loop
        displayMainMenu();
    }

    public static void initialize() {
        int gold = 0;
        ongoingGame = false;

        //if first playthrough, fresh initialization
        if (init == null) {
            init = new Initialize();
            npcs = init.getNPCs();
            items = init.getItems();
        } else {
            //indicative of new game+ or character death
            if (completed || gameOver) {
                gold = Yohane.getGoldOwned();
            } //preserve Yohane's previous gold

            //reuse existing idols and items
            Initialize newInit = new Initialize();
            newInit.setNPCs(npcs);
            newInit.setItems(items);
            init = newInit;
        }

        //refresh Yohane
        Yohane = init.getYohane();
        Yohane.setGoldOwned(gold);

        //clears all items except noppo bread and tears of a fallen angel if gameOver
        if (gameOver) {
            Iterator<Item> it = Yohane.getInventory().iterator();
            while (it.hasNext()){
                Item item = it.next();
                if (!(item.getName().equalsIgnoreCase("Noppo Bread") || 
                item.getName().equalsIgnoreCase("Tears of a fallen angel"))) {
                    it.remove();
                }
            }
        } else { //if normal new game or new game+, clears all items
            Yohane.getInventory().clear();
        }
        //in all cases, set all (shop) availability back to true
        for(Item item: items) {
            item.setAvailable(true);
        }

        //refresh Lailaps and dungeons
        Lailaps = init.getLailaps();
        dungeons = init.getDungeons();
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
            String plus = (completed) ? "+" : "";

            System.out.println("\n************************************************");
            System.out.println("*             Yohane The Parhelion!            *");
            System.out.println("*        The Siren in the Mirror World!        *");
            System.out.println("************************************************");
            if (ongoingGame) {
                System.out.println("        [C]ontinue");
            }
            System.out.println("        [N]ew Game" + plus);
            System.out.println("        [S]tatus");
            System.out.println("        [Q]uit");
            System.out.print("\nYour choice: ");

            choice = Character.toLowerCase(s.nextLine().charAt(0));

            switch(choice) {
                case 'c':
                    startGame();
                    break;
                case 'n':
                    ongoingGame = true;
                    initialize(); //re-initialize to start new game
                    startGame();
                    break;
                case 's':
                    displayStatus();
                    break;
            }

        } while(choice != 'q');
    }

    /**
     * Renders a placeholder block notification layout alerting that attribute inspections 
     * are still undergoing engineering developments.
     */
    public static void displayStatus(){
        System.out.print("\033[H\033[2J");
        System.out.flush();

        int i, target = 45; //target index to print x amount of times an idol was saved
        String[] text = {
            "Times Kanan was saved",
            "Times Hanamaru was saved",
            "Times Ruby was saved",
            "Times Dia was saved",
            "Times Chika was saved",
            "Times You was saved",
            "Times Riko was saved",
            "Times Mari was saved",
            "Time Siren was defeated",
            "No. of game overs",
            "Total gold spent"
        };

        System.out.println("\n************************************************************");
        System.out.println("                       Overall Status");
        System.out.println("************************************************************");
        //displays number of times idols have been saved
        for (i = 0; i < text.length-3; i++) {
            System.out.println(text[i] + " ".repeat(target-text[i].length()) + npcs[i].getTimesSaved() + " times");
        }
        
        System.out.println();
        //displays number of times siren has been defeated
        System.out.println(text[i] + " ".repeat(target-text[i].length()) + sirenDefeated + " times");
        System.out.println();
        i++; //display number of game overs
        System.out.println(text[i] + " ".repeat(target-text[i].length()) + gameOvers + " times");
        System.out.println();
        i++; //display total gold spent
        String YELLOW = "\u001B[38;5;227m";
        String RESET = "\u001B[0m";
        System.out.println(text[i] + " ".repeat(target-text[i].length()) + YELLOW + goldSpent + " gp" + RESET);
        System.out.println();

        System.out.println("\nPress Enter to return...");
        s.nextLine();
    }

    /**
     * Allocates memory for user references, maps sequence structural boundaries, 
     * and sets up the test exploration loop scenario.
     */
    public static void startGame(){
        ongoingGame = true;
        displayGameMenu();
    }

    /**
     * Loops choices allowing the character to transition into physical environments, 
     * check collected storage structures, or yield activities.
     */
    public static void displayGameMenu(){
        char choice;
        gameOver = false;

        do {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("\nLailaps: Yohane! Where should we go now?\n");

            displayStats();
            System.out.println();

            int i, size = dungeons.length;
            bossUnlocked = dungeons[0].isCompleted(Yohane) && dungeons[1].isCompleted(Yohane) && dungeons[2].isCompleted(Yohane);
            bossOngoing = false;

            //if all three dungeons have been completed, show boss
            if (bossUnlocked) {
                System.out.println("[1] Face the " + dungeons[size-1].getName());
            } 
            //if dungeons are incomplete, display dungeon menu choices
            else {
                for (i = 0; i < size-1; i++) {
                    String avail = dungeons[i].getMember().isSaved() ? "X" : Integer.toString(i+1);
                    System.out.println("[" + avail + "] Visit " + dungeons[i].getName());
                }
            }
            System.out.println("[I] Inventory");
            System.out.println("[Q] Quit");
            //if shop is unlocked, display shop option
            if (shopUnlocked) {
                System.out.println("[H] Hanamaru's Store");
            }

            System.out.print("\nChoice: ");

            try {
                choice = Character.toLowerCase(
                s.nextLine().charAt(0));
            } catch (StringIndexOutOfBoundsException e) {
                //error catching
                choice = 'x';
            }

            switch (choice) {
                case '1':
                    if (!dungeons[0].isCompleted(Yohane) && !bossUnlocked) {
                        runDungeon(dungeons[0]);
                    }
                    if (bossUnlocked) {
                        bossOngoing = true;
                        runFinalBoss(dungeons[3]);
                    }
                    break;
                case '2':
                    if (!dungeons[1].isCompleted(Yohane)) {
                        runDungeon(dungeons[1]);
                    }
                    break;
                case '3':
                    if (!dungeons[2].isCompleted(Yohane)) {
                        runDungeon(dungeons[2]);
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
        } while (choice != 'q' && !gameOver && ongoingGame);
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

        System.out.println("Lailaps: These are the items you have, Yohane!");
        System.out.println();

        displayStats();

        System.out.println("\nItems available");

        if (Yohane.getInventory().isEmpty()) {
            System.out.println("No items.");
        } else {
            System.out.println("No items.");
            System.out.println("1. Tears of a fallen angel         x        " + Collections.frequency(Yohane.getInventory(), items[0]));
            System.out.println("2. Noppo bread                     x        " + Collections.frequency(Yohane.getInventory(), items[1]));
            System.out.println("3. Choco-mint ice cream            x        " + Collections.frequency(Yohane.getInventory(), items[8]));
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
    public static void runDungeon(Dungeon dungeon){
        boolean firstMove = true;
        char input;

        do {
            int index = dungeon.getCurFloor() - 1;
            Floor currentFloor = dungeon.getFloors()[index];
            Yohane.setFloor(dungeon.getFloors()[index]);
            System.out.println("Current position: " + Yohane.getX() + " " + Yohane.getY());

            //spawn Yohane on the first move
            if (firstMove) {
                int x, y;
                //assigns coordinates to Yohane
                Yohane.findCharTile(currentFloor.getMap());

                x = Yohane.getX();
                y = Yohane.getY();

                //sets underlying tile to passable tile
                currentFloor.getMap()[x][y] = new Tile(x, y, '.');
                firstMove = false;
            }

            displayDungeonMenu(dungeon, index);

            try { //in case user returns without input
                input = s.nextLine().charAt(0);
                input = Character.toLowerCase(input);
            } catch (StringIndexOutOfBoundsException e) {
                input = 'x';
            };

            Yohane.incrementTurn(); //all actions are counted as a 'turn'
            characterMoves(input);
            enemyMoves(currentFloor);

            //check if floor is complete
            if (currentFloor.completeFloor(Yohane)) {
                dungeon.incrementCurFloor();
                firstMove = true;
                Yohane.setTurnCount(0);
            }

            if (dungeon.isCompleted(Yohane)) {
                //saves idol member attached to dungeon
                dungeon.getMember().setSaved(true);

                //increment npc objects
                int i, size = npcs.length;
                boolean found = false;
                for (i = 0; i < size && !found; i++) {
                    if(npcs[i].getName().equalsIgnoreCase(dungeon.getMember().getName())) {
                        dungeon.getMember().incrementTimesSaved();

                        //defends against mismatched references from initialization
                        if (dungeon.getMember().getTimesSaved() != npcs[i].getTimesSaved()) {
                            npcs[i].incrementTimesSaved();
                            npcs[i].setSaved(true);
                        }
                    }
                }

                //if member is Hanamaru, unlock item shop
                if (dungeon.getMember().getName().equalsIgnoreCase("Hanamaru Kunikida")) {
                    shopUnlocked = true;
                }

                displayDungeonClearScene(dungeon);
                break;
            }
        } while (!dungeon.gameOver(Yohane) && !dungeon.isCompleted(Yohane));

        //resets Yohane's health if dungeon ends in death
        if (dungeon.gameOver(Yohane)) {
            gameOverScreen(dungeon);
        }
    }

    public static void gameOverScreen(Dungeon dungeon) {
        String RED = "\u001B[38;5;196m";
        String RESET = "\u001B[0m";

        String dead = (Lailaps.charDeath()) ? "Lailaps" : "You";
        System.out.println(RED + dead + " Died!" + RESET);
        String killer = (Lailaps.charDeath()) ? Lailaps.getCauseOfDeath() : Yohane.getCauseOfDeath();
        System.out.println("Killed by " + RED + killer + RESET);

        //system updates
        gameOvers++;
        gameOver = true;
        initialize(); //initialize

        if (bossUnlocked) {
            Lailaps.setHealth(Lailaps.getMaxHealth());
            Lailaps.setTurnCount(0);
        }
        //regen floor
        dungeon.getFloors()[dungeon.getCurFloor()-1].generateFloor();

        System.out.println("\nPress Enter to return...");
        s.nextLine();
    }

    public static void characterMoves(char input) {
        //if valid direction, moves characters
        if ("wasd".contains(Character.toString(input))) {
            Yohane.move(input, Lailaps.getX(), Lailaps.getY());
            if (bossUnlocked) { //if boss level, move lailaps as well
                Lailaps.move(input, Yohane.getX(), Yohane.getY());
            }
        }
        //item logic for Yohane
        else if (input == ' ') {
            Yohane.useItem();
        }
        else if (input == '[') {
            Yohane.prevItem();
        }
        else if (input == ']') {
            Yohane.nextItem();
        }
    }

    public static void enemyMoves(Floor currentFloor) {
        //prompts action from enemy characters
        Iterator<EnemyChar> it = currentFloor.getEnemies().iterator();
        while (it.hasNext()){
            EnemyChar enemy = it.next();
            if (enemy.charDeath()) { //if enemy is dead
                enemy.dropGold(currentFloor);
                it.remove();
            } else if (enemy instanceof Bat) { //if enemy is alive and a bat
                Bat bat = (Bat)enemy;
                bat.move(Yohane);
            } else if (!enemy.charDeath() && enemy instanceof Siren) { //if enemy is alive and a siren
                Siren siren = (Siren)enemy;
                siren.move(Yohane, Lailaps);
            }
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
    public static void displayDungeonMenu(Dungeon dungeon, int index) {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        if (bossUnlocked) {
            System.out.println("\nFinal Battle: Siren of the Mirror world!");
        } else {
            System.out.println("\nDungeon #" + dungeon.getDungeonNum() + ": " + dungeon.getName());
            System.out.println("Floor " + dungeon.getCurFloor() + " of " + dungeon.getNumFloors());
        }

        System.out.println();
        displayStats();

        System.out.println();
        if (bossUnlocked) {
            dungeon.getFloors()[index].displayMap(Yohane, Lailaps);    
        } else {
            dungeon.getFloors()[index].displayMap(Yohane, null);
        }

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
        System.out.println("                        Dungeon Cleared!");
        System.out.println("              "+dungeon.getName()+" Completed!");
        if(dungeon.getMember() != null) { //if the dungeon contains an NPC member
            System.out.println("                 "+dungeon.getMember().getName()+" rescued!");
            //if the member character is Hanamaru, unlocks shop dialogue
            if(dungeon.getMember().getName().equalsIgnoreCase("Hanamaru Kunikida")) {
                System.out.println("\nUnlocked: Hanamaru's Store Now Available!\n");
                System.out.println("************************************************************\n");
                System.out.println("Hanamaru: Yohane-chan, zura! You're here!");
                System.out.println("Yohane: Hanamaru! We have to get out of here quickly!");
                System.out.println("Hanamaru: Oh? I was wondering what this place was and why there \nare bats everywhere, zura!");
                System.out.println("Yohane: Seems like there's a Siren that wants to take your voices \nand is holding you in this dimension so that your \ncounterparts in the real world can't sing!");
                System.out.println("Hanamaru: Really? That sounds terrifying, zura. What have we \ngot to do?");
                System.out.println("Yohane: First, we have to get out of here, Zuramaru! I know the \nway out.");
                System.out.println("Hanamaru: Lead the way, zura!");
            }
        }
        System.out.println("************************************************************\n");
        System.out.println("\nPress Enter to return...");
        s.nextLine();
    }

    public static void displayShop() {
        Shop shop = new Shop(items);
        String input;
        String statusMessage = "";

        do {
            // Clear terminal screen
            System.out.print("\033[H\033[2J");
            System.out.flush();

            // 1. Display shop items along with player's gold and last transaction message
            shop.displayItems(npcs, Yohane, statusMessage);

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
                    Item purchased = items[choice - 1]; // direct reference
                    goldSpent += purchased.getPrice();
                    statusMessage = "Successfully purchased " + purchased.getName() + "!";
                } else {
                    statusMessage = "Purchase failed! Check your gold balance or duplicate items.";
                }

            } catch (NumberFormatException e) {
                statusMessage = "Invalid selection. Please enter a valid number or 'R'.";
            }

        } while (true);
    }

    public static void displayStats() {
        System.out.print("HP: " + Yohane.getHealth() + "/" + Yohane.getMaxHealth());
        if (bossOngoing && bossUnlocked) { //if boss level, display lailaps hp
            System.out.println("\tLailaps HP: " + Lailaps.getHealth() + "/" + Lailaps.getMaxHealth());
        }
        String YELLOW = "\u001B[38;5;227m";
        String RESET = "\u001B[0m";
        System.out.println("\t\tTotal Gold: " + YELLOW + Yohane.getGoldOwned() + " GP" + RESET);

        int quantity;
        String displayQuantity;

        try {
            quantity = Collections.frequency(Yohane.getInventory(), Yohane.getCurItem());
            displayQuantity = (quantity > 1) ?  "(" + quantity + ")" : "";
            System.out.println("Item on Hand: " + Yohane.getCurItem().getName() + displayQuantity);
        } catch (NullPointerException e) {
            System.out.println("Item on Hand: N/A");
        }
    }

    /**
     * Orchestrates the Final Boss battle against the Siren of the Mirror World.
     * Manages dual-character movement (Yohane & Lailaps), switch activation pairing,
     * dynamic bat spawning tiers, Siren movement AI, and win/loss states.
     * 
     * @param yohane  the primary user-controlled character
     * @param dungeon the active final dungeon structure
     */
    public static void runFinalBoss(Dungeon dungeon) {
        // int index = dungeon.getCurFloor() - 1;
        BossFloor bossFloor = (BossFloor)dungeon.getFloors()[0];
        Yohane.setFloor(dungeon.getFloors()[0]);
        Lailaps.setFloor(dungeon.getFloors()[0]);

        // 1. Locate starting tiles on map ('Y' and 'L')
        int x, y;
        Yohane.findCharTile(bossFloor.getMap());
        Lailaps.findCharTile(bossFloor.getMap());

        // Clear initial spawn tiles to passable floor
        x = Yohane.getX();
        y = Yohane.getY();
        bossFloor.getMap()[x][y] = new Tile(x, y, '.');
        x = Lailaps.getX();
        y = Lailaps.getY();
        bossFloor.getMap()[x][y] = new Tile(x, y, '.');

        // 2. variable
        Siren siren = (Siren)bossFloor.getEnemies().get(0);
        //TESTING
        //System.out.println("enemyMoves Siren@" + System.identityHashCode(siren));

        char input;
        // Spawn initial pair of switches ('0')
        Tile[] activeSwitches = bossFloor.spawnSwitchPair();

        // Main Boss Stage Loop
        do {
            // Render HUD & Boss Map with both characters
            displayDungeonMenu(dungeon, 0);
            //TESTING
            //System.out.println("TRIGGERS " + bossFloor.getTriggers());

            // Process User Input
            try {
                input = s.nextLine().charAt(0);
                input = Character.toLowerCase(input);
            } catch (StringIndexOutOfBoundsException e) {
                input = 'x';
            }

            Yohane.incrementTurn(); 
            //every 8 moves from Yohane, summon a bat
            if (Yohane.getTurnCount() % 8 == 0 && !siren.charDeath()) {
                siren.summonBat(Yohane.getX(), Yohane.getY(), Lailaps.getX(), Lailaps.getY());
            }
            //character and enemy moves
            characterMoves(input);
            enemyMoves(bossFloor);

            // --- PHASE 1: SWITCH ACTIVATION MECHANIC ---
            if (!siren.isReleased()) {
                boolean yOnS1 = (Yohane.getX() == activeSwitches[0].getX() && Yohane.getY() == activeSwitches[0].getY());
                boolean lOnS2 = (Lailaps.getX() == activeSwitches[1].getX() && Lailaps.getY() == activeSwitches[1].getY());

                boolean yOnS2 = (Yohane.getX() == activeSwitches[1].getX() && Yohane.getY() == activeSwitches[1].getY());
                boolean lOnS1 = (Lailaps.getX() == activeSwitches[0].getX() && Lailaps.getY() == activeSwitches[0].getY());

                // Check if BOTH stand on switches simultaneously
                if ((yOnS1 && lOnS2) || (yOnS2 && lOnS1)) {
                    bossFloor.incrementTriggers();

                    // Clear current switches
                    bossFloor.destroyTile(activeSwitches[0]);
                    bossFloor.destroyTile(activeSwitches[1]);

                    if (bossFloor.getTriggers() < 3) {
                        activeSwitches = bossFloor.spawnSwitchPair();
                    } else {
                        // Trigger Phase 2: Break Siren's barriers
                        siren.release();
                        bossFloor.releaseSiren();
                    }
                }
            }

            if (siren.charDeath()) {
                bossFloor.killBats();
                bossFloor.spawnExit();
            }

            // Check Stage Clear (Stepping on Exit 'E')
            if (bossFloor.completeFloor(Yohane)) {
                sirenDefeated++;
                completed = true; //prepare for new game+ display
                ongoingGame = false; //no more ongoing game
                bossUnlocked = false; //resets boss unlock
                displayDungeonClearScene(dungeon);
                break;
            }
        } while (!Yohane.charDeath() && !Lailaps.charDeath() && !dungeon.isCompleted(Lailaps));

        // Game Over Handler
        if (Yohane.charDeath() || Lailaps.charDeath()) {
            dungeons[3].getFloors()[0] = new BossFloor(1);
            gameOverScreen(dungeon);
        }
    }
}