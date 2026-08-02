package Controller;

//Playable file
import Character_Classes.*;
import Dungeon_Classes.*;
import Item_Classes.*;

import java.util.Iterator;

/**
 * Serves as the main orchestrator for "Yohane The Parhelion! The Siren in the Mirror World!".
 * Holds all game state (player, dungeons, items, npcs) and exposes the operations the
 * Swing UI (GUI.MainFrame and its panels) calls in response to user actions.
 * @author Katigbak and Porciuncula
 * @version 2.0
 */
public class GameGUI {

    /** Flag tracking whether a game run is currently in progress. */
    private static boolean ongoingGame = false;
    /** Flag tracking whether Hanamaru's shop has been unlocked. */
    private static boolean shopUnlocked = false;
    /** Flag tracking whether the boss dungeon has been unlocked. */
    private static boolean bossUnlocked = false;
    /** Flag tracking whether the Siren boss fight is currently in progress. */
    private static boolean bossOngoing = false;
    /** Flag tracking whether the current run has been completed. */
    private static boolean completed = false;
    /** Flag tracking whether the current run has ended in a game over. */
    private static boolean gameOver = false;
    /** The running total of gold spent across the current run. */
    private static int goldSpent = 0;
    /** The running count of times the Siren has been defeated across all runs. */
    private static int sirenDefeated = 0;
    /** The running count of game overs suffered across all runs. */
    private static int gameOvers = 0;

    // Central initializer
    /** The Initialize instance responsible for building the current run's game state. */
    private static Initialize init;

    // References pulled from initializer
    /** The primary playable character. */
    private static PlayableChar Yohane;
    /** The secondary playable character, active during the boss fight. */
    private static PlayableChar Lailaps;
    /** The set of dungeons for the current run. */
    private static Dungeon[] dungeons;
    /** The catalog of purchasable items for the current run. */
    private static Item[] items;
    /** The roster of rescuable idol NPCs for the current run. */
    private static NPChar[] npcs;

    // Which dungeon/floor the player is currently exploring (null until startDungeon() runs)
    /** The dungeon the player is currently exploring, or null if none has been entered yet. */
    private static Dungeon currentDungeon;
    /** The floor the player is currently exploring, or null if none has been entered yet. */
    private static Floor currentFloor;

    // Currently active switch pair during the Siren boss fight (null outside of it)
    /** The currently active pair of switch tiles during Phase 1 of the Siren fight, or null outside of it. */
    private static Tile[] activeSwitches;

    /**
     * Private constructor to prevent instantiation of utility main runner class.
     */
    private GameGUI() { /* Utility class */ }

    /**
     * Resets game state for a new game (or a New Game+ / respawn after death).
     * Does NOT place the player on any dungeon floor - that only happens once the
     * player actually picks a dungeon, via startDungeon().
     */
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

        currentDungeon = null;
        currentFloor = null;

        // boss fight state is only valid mid-fight - clear it on every (re)initialize
        bossUnlocked = false;
        activeSwitches = null;
    }

    /**
     * Enters a dungeon for the first time: sets it as current, places Yohane on its
     * first floor, and clears the underlying 'Y' tile so it's not drawn twice.
     * If the dungeon's first floor is the BossFloor, sets up the Siren fight instead
     * (spawning Lailaps too and dealing out the first pair of switches).
     *
     * @param dungeon the Dungeon instance the player has chosen to enter
     */
    public static void startDungeon(Dungeon dungeon) {
        currentDungeon = dungeon;
        Floor floor = dungeon.getFloors()[0];

        if (floor instanceof BossFloor) {
            startBossFloor((BossFloor) floor);
        } else {
            spawnYohaneOnFloor(floor);
        }
    }

    /**
     * Places both Yohane and Lailaps on the boss floor (clearing their spawn tiles),
     * and spawns the first pair of switches to kick off Phase 1 of the fight.
     *
     * @param bossFloor the BossFloor instance to set up    
     */
    private static void startBossFloor(BossFloor bossFloor) {
        currentFloor = bossFloor;
        Yohane.setFloor(bossFloor);
        Lailaps.setFloor(bossFloor);

        Yohane.findCharTile(bossFloor.getMap());
        Lailaps.findCharTile(bossFloor.getMap());

        int x = Yohane.getX();
        int y = Yohane.getY();
        bossFloor.getMap()[x][y] = new Tile(x, y, '.');

        x = Lailaps.getX();
        y = Lailaps.getY();
        bossFloor.getMap()[x][y] = new Tile(x, y, '.');

        activeSwitches = bossFloor.spawnSwitchPair();
    }

    /**
     * Locates Yohane's spawn tile ('Y') on the given floor, sets it as her current
     * floor, and clears that tile to a passable floor tile so it isn't drawn twice.
     *
     * @param floor the Floor instance to place Yohane on
     */
    private static void spawnYohaneOnFloor(Floor floor) {
        currentFloor = floor;
        Yohane.setFloor(floor);
        Yohane.findCharTile(floor.getMap());

        int x = Yohane.getX();
        int y = Yohane.getY();
        floor.getMap()[x][y] = new Tile(x, y, '.');
    }

    /**
     * Executes one turn: moves/uses an item per input, runs enemy AI, and advances
     * to the next floor (or reports the dungeon as cleared) if the floor is complete.
     *
     * @param input the key pressed ('w'/'a'/'s'/'d' to move, ' ' to use item)
     * @return true if the dungeon has just been fully cleared, false otherwise
     */
    public static boolean processTurn(char input) {
        // Delegate to the boss-fight turn loop while Yohane is on the Siren's floor
        if (currentFloor instanceof BossFloor) {
            return processBossTurn(input, (BossFloor) currentFloor);
        }

        Yohane.incrementTurn();

        int prevX = Yohane.getX();
        int prevY = Yohane.getY();

        characterMoves(input);
        Yohane.checkHeatDamage(prevX, prevY);

        if (currentFloor != null) {
            Yohane.setAttackedThisTurn(false);
            enemyMoves(currentFloor);
        }

        if (currentFloor.completeFloor(Yohane)) {

            // Last floor in dungeon
            if (currentDungeon.getCurFloor() == currentDungeon.getNumFloors()) {
                currentDungeon.getMember().setSaved(true);

                // Increment the matching npc's lifetime "times saved" counter.
                // (Two separate objects - dungeon.getMember() and the entry in
                // npcs[] - can end up tracking the same idol; keep them in sync.)
                for (NPChar npc : npcs) {
                    if (npc.getName().equalsIgnoreCase(currentDungeon.getMember().getName())) {
                        currentDungeon.getMember().incrementTimesSaved();

                        if (currentDungeon.getMember().getTimesSaved() != npc.getTimesSaved()) {
                            npc.incrementTimesSaved();
                            npc.setSaved(true);
                        }
                        break;
                    }
                }

                // Unlock Hanamaru's shop once she's been rescued
                if (currentDungeon.getMember().getName().equalsIgnoreCase("Hanamaru Kunikida")) {
                    shopUnlocked = true;
                }

                // Recheck whether all 3 non-boss dungeons are now cleared
                refreshBossUnlockStatus();

                return true;
            }

            // Go to next floor
            currentDungeon.incrementCurFloor();
            Yohane.setTurnCount(0);
            spawnYohaneOnFloor(currentDungeon.getFloors()[currentDungeon.getCurFloor() - 1]);
        }

        return false;
    }

    /**
     * Executes one turn of the Siren boss fight: summons a bat every 8 turns, moves
     * both Yohane and Lailaps, runs enemy AI (bats + Siren), checks whether the pair
     * has just triggered a switch pair (advancing to the next pair, or releasing the
     * Siren once all 3 have been triggered), and checks whether the Siren has been
     * defeated and the exit reached.
     *
     * @param input the key pressed
     * @param bossFloor the Siren's boss floor
     * @return true if the Siren has just been defeated and the exit reached
     */
    private static boolean processBossTurn(char input, BossFloor bossFloor) {
        Siren siren = bossFloor.getSiren();

        Yohane.incrementTurn();

        // Every 8 moves, the Siren summons another bat (unless already defeated or released)
        if (Yohane.getTurnCount() % 8 == 0 && !siren.charDeath() && !siren.isReleased()) {
            siren.summonBat(Yohane.getX(), Yohane.getY(), Lailaps.getX(), Lailaps.getY());
        }

        int prevXY = Yohane.getX();
        int prevYY = Yohane.getY();
        int prevXL = Lailaps.getX();
        int prevYL = Lailaps.getY();

        characterMoves(input);

        Yohane.checkHeatDamage(prevXY, prevYY);
        Lailaps.checkHeatDamage(prevXL, prevYL);
        
        Yohane.setAttackedThisTurn(false);
        Lailaps.setAttackedThisTurn(false);

        enemyMoves(bossFloor);

        // --- Phase 1: switch activation ---
        if (!siren.isReleased()) {
            boolean yOnS1 = (Yohane.getX() == activeSwitches[0].getX() && Yohane.getY() == activeSwitches[0].getY());
            boolean lOnS2 = (Lailaps.getX() == activeSwitches[1].getX() && Lailaps.getY() == activeSwitches[1].getY());

            boolean yOnS2 = (Yohane.getX() == activeSwitches[1].getX() && Yohane.getY() == activeSwitches[1].getY());
            boolean lOnS1 = (Lailaps.getX() == activeSwitches[0].getX() && Lailaps.getY() == activeSwitches[0].getY());

            // Both characters standing on the two switches at once = a successful trigger
            if ((yOnS1 && lOnS2) || (yOnS2 && lOnS1)) {
                bossFloor.incrementTriggers();

                bossFloor.destroyTile(activeSwitches[0]);
                bossFloor.destroyTile(activeSwitches[1]);

                if (bossFloor.getTriggers() < 3) {
                    activeSwitches = bossFloor.spawnSwitchPair();
                } else {
                    // Phase 2: release the Siren and drop her barriers
                    siren.release();
                    bossFloor.releaseSiren();
                }
            }
        }

        // Siren defeated: clear remaining bats and open the exit
        if (siren.charDeath()) {
            bossFloor.killBats();
            bossFloor.spawnExit();
        }

        // Stage clear: Yohane reached the exit
        if (bossFloor.completeFloor(Yohane)) {
            sirenDefeated++;
            completed = true;   //prepare for New Game+ display
            ongoingGame = false; //no more ongoing game
            bossUnlocked = false; //resets boss unlock
            return true;
        }

        return false;
    }

    /**
     * Routes a key press to the appropriate playable-character action: movement
     * for 'w'/'a'/'s'/'d' (moving Lailaps too while on the boss floor), item use
     * for ' ', or cycling the selected item for '[' and ']'.
     *
     * @param input the key pressed
     */    
    public static void characterMoves(char input) {
        //if valid direction, moves characters
        if ("wasd".contains(Character.toString(input))) {
            Yohane.move(input, Lailaps.getX(), Lailaps.getY());
            if (currentFloor instanceof BossFloor) { //if on the boss floor, move lailaps as well
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

    /**
     * Prompts a turn of action from every enemy on the given floor: removes and
     * drops gold for any enemy that has died, and otherwise delegates to that
     * enemy's own move behavior (Bat or Siren).
     *
     * @param currentFloor the Floor whose enemies should act this turn
     */    
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
     * Recomputes whether the final (boss) dungeon should be unlocked, i.e.
     * whether the first three dungeons have all been cleared.
     */
    private static void refreshBossUnlockStatus() {
        bossUnlocked = dungeons[0].isCompleted(Yohane)
                && dungeons[1].isCompleted(Yohane)
                && dungeons[2].isCompleted(Yohane);
    }

    /**
     * Retrieves the primary playable character.
     *
     * @return the Yohane PlayableChar instance
     */    
    public static PlayableChar getYohane() {
        return Yohane;
    }

    /**
     * Retrieves the set of dungeons for the current run.
     *
     * @return the array of Dungeon instances
     */    
    public static Dungeon[] getDungeons() {
        return dungeons;
    }
    
    /**
     * Retrieves the catalog of purchasable items for the current run.
     *
     * @return the array of Item instances
     */
    public static Item[] getItems() {
        return items;
    }
    
    /**
     * Checks whether Hanamaru's shop has been unlocked.
     *
     * @return true if the shop is unlocked, false otherwise
     */
    public static boolean isShopUnlocked() {
        return shopUnlocked;
    }
    
    /**
     * Checks whether the boss dungeon has been unlocked.
     *
     * @return true if the boss dungeon is unlocked, false otherwise
     */
    public static boolean isBossUnlocked() {
        return bossUnlocked;
    }
    
    /**
     * Checks whether a game run is currently in progress.
     *
     * @return true if a run is ongoing, false otherwise
     */
    public static boolean isOngoingGame() {
        return ongoingGame;
    }

    /**
     * Call when the player has just chosen "New Game" / "New Game+" from the
     * main menu. Resets game state and marks a run as in progress.
     */
    public static void beginNewRun() {
        initialize();
        ongoingGame = true;
    }

    /**
     * Call once, right after detecting Yohane.charDeath() (or Lailaps' during
     * the boss fight), to record the game over and reset state for next time.
     * Read any death info (HP, cause) from the player BEFORE calling this -
     * initialize() replaces Yohane with a fresh PlayableChar.
     */
    public static void handleGameOver() {
        gameOvers++;
        gameOver = true;

        // Dying mid boss-fight resets the Siren's floor (fresh switches/triggers) for next time
        if (currentFloor instanceof BossFloor) {
            dungeons[dungeons.length - 1].getFloors()[0] = new BossFloor(1);
        }

        initialize();
    }

    /**
     * True once the player has ever completed a run or suffered a game over -
     * used to swap the main menu's "New Game" label to "New Game+".
     *
     * @return true if the player has a prior completed or ended run, false otherwise
     */
    public static boolean hasPriorRun() {
        return completed || gameOver;
    }
    
    /**
     * Retrieves the secondary playable character.
     *
     * @return the Lailaps PlayableChar instance
     */
    public static PlayableChar getLailaps() {
        return Lailaps;
    }
    
    /**
     * Retrieves the floor the player is currently exploring.
     *
     * @return the current Floor instance, or null if none has been entered yet
     */
    public static Floor getCurrentFloor() {
        return currentFloor;
    }
    
    /**
     * Retrieves the dungeon the player is currently exploring.
     *
     * @return the current Dungeon instance, or null if none has been entered yet
     */
    public static Dungeon getCurrentDungeon() {
        return currentDungeon;
    }
}
