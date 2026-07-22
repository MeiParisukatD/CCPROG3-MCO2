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
                case 'b': // TEMPORARY TESTING KEY FOR BOSS BATTLE
                    runFinalBoss(Yohane, dungeon);
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
            dungeon.getFloors()[dungeon.getCurFloor()-1].generateFloor();
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
        dungeon.getFloors()[index].displayMap(Yohane, null);

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

    /**
     * Orchestrates the Final Boss battle against the Siren of the Mirror World.
     * Manages dual-character movement (Yohane & Lailaps), switch activation pairing,
     * dynamic bat spawning tiers, Siren movement AI, and win/loss states.
     * 
     * @param yohane  the primary user-controlled character
     * @param dungeon the active final dungeon structure
     */
    public static void runFinalBoss(PlayableChar yohane, Dungeon dungeon) {
        dungeon.getFloors()[dungeon.getCurFloor() - 1] = new Floor(dungeon.getCurFloor(), "map_boss.txt");
        
        int index = dungeon.getCurFloor() - 1;
        Floor bossFloor = dungeon.getFloors()[index];

        // 1. Initialize Lailaps & locate starting tiles on map ('Y' and 'L')
        PlayableChar lailaps = new PlayableChar("Lailaps", 4.0f, 0.0f, null);
        
        yohane.findCharTile(bossFloor.getMap());
        lailaps.findCharTile(bossFloor.getMap());

        // Clear initial spawn tiles to passable floor
        bossFloor.getMap()[yohane.getX()][yohane.getY()] = new Tile(yohane.getX(), yohane.getY(), '.');
        bossFloor.getMap()[lailaps.getX()][lailaps.getY()] = new Tile(lailaps.getX(), lailaps.getY(), '.');

        // 2. Fetch Siren created by Floor.generateFloor()
        EnemyChar siren = null;
        for (EnemyChar enemy : bossFloor.getEnemies()) {
            if (enemy.getName().equalsIgnoreCase("Siren")) {
                siren = enemy;
                break;
            }
        }

        // 3. State Tracking Variables
        int switchTriggersCount = 0;
        boolean sirenPhase = false;
        char input;
        
        // Spawn initial pair of switches ('0')
        Tile[] activeSwitches = spawnSwitchPair(bossFloor);

        // Main Boss Stage Loop
        do {
            // Render HUD & Boss Map with both characters
            displayBossMenu(dungeon, index, yohane, lailaps);

            // Process User Input
            try {
                input = s.nextLine().charAt(0);
                input = Character.toLowerCase(input);
            } catch (StringIndexOutOfBoundsException e) {
                input = 'x';
            }

            yohane.incrementTurn(); // Turn engine step

            // Save position prior to move to evaluate attack step
            int prevYohaneX = yohane.getX();
            int prevYohaneY = yohane.getY();

            // Handle Input & Item Commands or Synchronized Dual Movement
            if ("wasd".contains(Character.toString(input))) {
                moveDualCharacters(input, yohane, lailaps, bossFloor);
            } else if (input == ' ') {
                yohane.useItem();
            } else if (input == '[') {
                yohane.prevItem();
            } else if (input == ']') {
                yohane.nextItem();
            }

            // --- PHASE 1: SWITCH ACTIVATION MECHANIC ---
            if (!sirenPhase) {
                boolean yOnS1 = (yohane.getX() == activeSwitches[0].getX() && yohane.getY() == activeSwitches[0].getY());
                boolean lOnS2 = (lailaps.getX() == activeSwitches[1].getX() && lailaps.getY() == activeSwitches[1].getY());

                boolean yOnS2 = (yohane.getX() == activeSwitches[1].getX() && yohane.getY() == activeSwitches[1].getY());
                boolean lOnS1 = (lailaps.getX() == activeSwitches[0].getX() && lailaps.getY() == activeSwitches[0].getY());

                // Check if BOTH stand on switches simultaneously
                if ((yOnS1 && lOnS2) || (yOnS2 && lOnS1)) {
                    switchTriggersCount++;

                    // Clear current switches
                    bossFloor.destroyTile(activeSwitches[0]);
                    bossFloor.destroyTile(activeSwitches[1]);

                    if (switchTriggersCount < 3) {
                        activeSwitches = spawnSwitchPair(bossFloor);
                    } else {
                        // Trigger Phase 2: Break Siren's barriers
                        sirenPhase = true;
                        breakSirenBarriers(bossFloor, siren);
                    }
                }

                // Bat Spawning Engine (Every 8 turns)
                if (yohane.getTurnCount() % 8 == 0) {
                    spawnBossBat(bossFloor, switchTriggersCount);
                }
            }

            // --- ENEMY ACTIONS (BATS) ---
            Iterator<EnemyChar> it = bossFloor.getEnemies().iterator();
            while (it.hasNext()) {
                EnemyChar enemy = it.next();
                
                // Skip Siren here as she uses custom phase movement
                if (enemy.getName().equalsIgnoreCase("Siren")) continue;

                if (enemy.charDeath()) {
                    enemy.dropGold(bossFloor);
                    it.remove();
                } else {
                    enemy.move(bossFloor, yohane);
                }
            }

            // --- PHASE 2: SIREN PURSUIT & COMBAT ---
            if (sirenPhase && siren != null && !siren.charDeath()) {
                // Check if Yohane stepped into Siren's tile to ATTACK
                if (yohane.getX() == siren.getX() && yohane.getY() == siren.getY()) {
                    // Defeat Siren
                    yohane.dealDmg(siren);
                    siren.dropGold(bossFloor);
                    
                    // Revert Yohane to pre-move tile so Exit 'E' spawns cleanly at Siren's spot
                    yohane.setX(prevYohaneX);
                    yohane.setY(prevYohaneY);
                    bossFloor.getMap()[siren.getX()][siren.getY()] = new Tile(siren.getX(), siren.getY(), 'E');
                } else {
                    // Siren pursues closest target
                    moveSiren(siren, yohane, lailaps, bossFloor);

                    // Attack Evaluation
                    if (isAdjacent(siren, lailaps)) {
                        lailaps.takeDmg(siren); // Game Over for Lailaps
                    } else if (isAdjacent(siren, yohane)) {
                        yohane.takeDmg(siren); // Triggers Choco-Mint Ice Cream or Death
                    }
                }
            }

            // Check Stage Clear (Stepping on Exit 'E')
            if (bossFloor.completeFloor(yohane)) {
                displayDungeonClearScene(dungeon);
                break;
            }

        } while (!yohane.charDeath() && !lailaps.charDeath() && !dungeon.gameOver(yohane));

        // Game Over Handler
        if (yohane.charDeath() || lailaps.charDeath()) {
            String RED = "\u001B[38;5;196m";
            String RESET = "\u001B[0m";

            System.out.println(RED + "You Died!" + RESET);
            String killer = lailaps.charDeath() ? "Siren (Lailaps Defeated)" : yohane.getCauseOfDeath();
            System.out.println("Killed by " + RED + killer + RESET);

            yohane.setHealth(3);
            bossFloor.generateFloor(); // Regenerate stage map
        }
    }

    // =========================================================================
    // HELPER METHODS
    // =========================================================================

    /**
     * Spawns 2 switches ('0') within row distance <= 2 and col distance <= 5.
     */
    private static Tile[] spawnSwitchPair(Floor floor) {
        Tile[][] map = floor.getMap();
        Tile[] switches = new Tile[2];
        int r1, c1, r2, c2;
        boolean valid = false;

        do {
            r1 = (int)(Math.random() * (floor.getRowLen() - 2)) + 1;
            c1 = (int)(Math.random() * (floor.getColLen() - 2)) + 1;

            int rMin = Math.max(1, r1 - 2);
            int rMax = Math.min(floor.getRowLen() - 2, r1 + 2);
            int cMin = Math.max(1, c1 - 5);
            int cMax = Math.min(floor.getColLen() - 2, c1 + 5);

            r2 = (int)(Math.random() * (rMax - rMin + 1)) + rMin;
            c2 = (int)(Math.random() * (cMax - cMin + 1)) + cMin;

            if ((r1 != r2 || c1 != c2) && 
                map[r1][c1].getSymbol() == '.' && 
                map[r2][c2].getSymbol() == '.') {
                valid = true;
            }
        } while (!valid);

        switches[0] = new Tile(r1, c1, '0');
        switches[1] = new Tile(r2, c2, '0');
        map[r1][c1] = switches[0];
        map[r2][c2] = switches[1];

        return switches;
    }

    /**
     * Moves Yohane and Lailaps simultaneously, evaluating individual tile collisions.
     */
    private static void moveDualCharacters(char input, PlayableChar yohane, PlayableChar lailaps, Floor floor) {
        if ("wasd".contains(Character.toString(input))) {
            yohane.move(input, floor);
            lailaps.move(input, floor);
        }
    }

    /**
     * Spawns a Bat with tiered difficulty based on switch triggers.
     */
    private static void spawnBossBat(Floor floor, int switchTriggers) {
        int tier = switchTriggers + 1; // Tier 1, 2, or 3
        int r, c;
        
        do {
            r = (int)(Math.random() * floor.getRowLen());
            c = (int)(Math.random() * floor.getColLen());
        } while (floor.getMap()[r][c].getSymbol() != '.');

        EnemyChar bat = new EnemyChar(
            "Bat",
            1.0f,
            0.5f * tier,
            5 * tier,
            tier == 1 ? 2 : 1, // Tier 1 moves every 2 turns; Tier 2/3 move every turn
            1,
            tier == 3, // Diagonal allowed on Tier 3
            r, c
        );
        floor.getEnemies().add(bat);
    }

    /**
     * Clears barrier walls ('*') surrounding Siren when Phase 2 starts.
     */
    private static void breakSirenBarriers(Floor floor, EnemyChar siren) {
        Tile[][] map = floor.getMap();
        int sx = siren.getX();
        int sy = siren.getY();

        for (int i = sx - 2; i <= sx + 2; i++) {
            for (int j = sy - 2; j <= sy + 2; j++) {
                if (i >= 0 && i < floor.getRowLen() && j >= 0 && j < floor.getColLen()) {
                    if (map[i][j].getSymbol() == '*') {
                        floor.destroyTile(map[i][j]);
                    }
                }
            }
        }
    }

    /**
     * Moves the Siren towards the closest target (Yohane or Lailaps).
     */
    private static void moveSiren(EnemyChar siren, PlayableChar yohane, PlayableChar lailaps, Floor floor) {
        int distY = Math.abs(siren.getX() - yohane.getX()) + Math.abs(siren.getY() - yohane.getY());
        int distL = Math.abs(siren.getX() - lailaps.getX()) + Math.abs(siren.getY() - lailaps.getY());
        
        PlayableChar target = (distY <= distL) ? yohane : lailaps;

        int dx = Integer.compare(target.getX(), siren.getX());
        int dy = Integer.compare(target.getY(), siren.getY());

        int newX = siren.getX() + dx;
        int newY = siren.getY() + dy;

        if (floor.getMap()[newX][newY].isPassable()) {
            siren.setX(newX);
            siren.setY(newY);
        }
    }

    /**
     * Checks if an enemy and player are adjacent (orthogonally or diagonally).
     */
    private static boolean isAdjacent(EnemyChar enemy, PlayableChar player) {
        int dx = Math.abs(enemy.getX() - player.getX());
        int dy = Math.abs(enemy.getY() - player.getY());
        return dx <= 1 && dy <= 1;
    }

    /**
     * Boss HUD Display renderer.
     */
    private static void displayBossMenu(Dungeon dungeon, int index, PlayableChar yohane, PlayableChar lailaps) {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("\nFinal Battle: Siren of the Mirror world!");
        System.out.print("HP: " + yohane.getHealth() + "/" + yohane.getMaxHealth());
        System.out.println("\tLailaps HP: " + lailaps.getHealth() + "/" + lailaps.getMaxHealth());
        System.out.println("Total Gold: " + yohane.getGoldOwned() + " GP");

        System.out.println();
        dungeon.getFloors()[index].displayMap(yohane, lailaps);

        System.out.println("\nTurn Counter: " + yohane.getTurnCount());
        System.out.print("Where to, Yohane? ");
    }
}
