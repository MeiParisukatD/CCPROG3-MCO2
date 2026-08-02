package Dungeon_Classes;

import Character_Classes.*;
import java.util.Iterator;

/**
 * Represents the special boss floor housing the Siren fight.
 * Extends the standard Floor to manage switch-triggering puzzle mechanics,
 * the Siren's exclusion zone and cage barriers, bat cleanup, and exit spawning
 * once the Siren is defeated.
 *
 * @author Katigbak and Porciuncula
 * @version 2.0
 */
public class BossFloor extends Floor {
    /** The running count of switch pairs successfully triggered by the player. */
    private int triggers;
    /** The Siren boss enemy instance residing on this floor. */
    private Siren siren;

    /**
     * Constructs a BossFloor at the given level index, generating its layout
     * from the standard boss map file and pulling the Siren enemy that was
     * spawned during map generation.
     *
     * @param floorNum the unique level index tracking identifier for this map
     * @param dungeonNum the number of the dungeon (1st/2nd/3rd/Siren's) this floor belongs to
     */
    public BossFloor(int floorNum, int dungeonNum) {
        super(floorNum, dungeonNum, "map_boss.txt");
        this.triggers = 0;
        this.siren = (Siren) this.enemies.get(0);
        this.siren.setFloor(this);
    }

    /**
     * Constructs a BossFloor by re-using the level index and source map file
     * of an existing Floor instance, regenerating its layout and Siren fresh.
     *
     * @param floor the source Floor whose level index and map file are reused
     */
    public BossFloor(Floor floor) {
        super(floor.getFloorNum(), floor.getDungeonNum(), floor.getFile());
        this.triggers = 0;
        this.siren = (Siren) this.enemies.get(0);
        this.siren.setFloor(this);
    }

    /**
     * Retrieves the Siren boss enemy instance residing on this floor.
     *
     * @return the Siren instance
     */
    public Siren getSiren() {
        return this.siren;
    }

    /**
     * Retrieves the running count of switch pairs successfully triggered.
     *
     * @return the number of triggers activated so far
     */
    public int getTriggers() {
        return this.triggers;
    }

    /**
     * Updates the running count of switch pairs successfully triggered.
     *
     * @param triggers the new trigger count
     */
    public void setTriggers(int triggers) {
        this.triggers = triggers;
    }

    /**
     * Increments the running count of switch pairs triggered by one.
     */
    public void incrementTriggers() {
        this.triggers++;
    }

    /**
     * Randomly places a new pair of switch tiles ('0') within a bounded
     * distance of one another, avoiding the Siren's exclusion zone and any
     * already-occupied tiles.
     *
     * @return an array containing the two newly placed switch Tiles
     */
    public Tile[] spawnSwitchPair() {
        int r1, c1, r2, c2;
        boolean valid = false;

        do {
            r1 = (int)(Math.random() * (this.rowLen - 2)) + 1;
            c1 = (int)(Math.random() * (this.colLen - 2)) + 1;

            int rMin = Math.max(1, r1 - 2);
            int rMax = Math.min(this.rowLen - 2, r1 + 2);
            int cMin = Math.max(1, c1 - 5);
            int cMax = Math.min(this.colLen - 2, c1 + 5);

            r2 = (int)(Math.random() * (rMax - rMin + 1)) + rMin;
            c2 = (int)(Math.random() * (cMax - cMin + 1)) + cMin;

            if ((r1 != r2 || c1 != c2) &&
                this.map[r1][c1].getSymbol() == '.' &&
                this.map[r2][c2].getSymbol() == '.'&&
                !exclusionZone(r1, c1) &&
                !exclusionZone(r2, c2)) {
                valid = true;
            }
        } while (!valid);

        this.map[r1][c1] = new Tile(r1, c1, '0');
        this.map[r2][c2] = new Tile(r2, c2, '0');

        return new Tile[] {this.map[r1][c1], this.map[r2][c2]};
    }

    /**
     * Clears barrier walls ('*') surrounding Siren when Phase 2 starts.
     */
    public void releaseSiren() {
        int sx = this.enemies.get(0).getX();
        int sy = this.enemies.get(0).getY();

        // Define a bounding box around the Siren's cage
        int xMin = sx;
        int xMax = sx + 2;
        int yMin = sy - 8;
        int yMax = sy + 9;

        for (int i = xMin; i <= xMax; i++) {
            for (int j = yMin; j <= yMax; j++) {
                if (i >= 0 && i < this.rowLen && j >= 0 && j < this.colLen) {
                    if (this.map[i][j].getSymbol() == '*') {
                        this.map[i][j] = new Tile(i, j, '.');
                    }
                }
            }
        }
    }

    /**
     * Kills all remaining Bat enemies on the field, dropping their gold and
     * removing them from the floor's active enemy tracking collection.
     */
    public void killBats() {
        //kills all remaining bats on field
        Iterator<EnemyChar> it = this.getEnemies().iterator();
        while (it.hasNext()){
            EnemyChar enemy = it.next();
            enemy.setHealth(0);
            enemy.dropGold(this);
            it.remove();
        }
    }

    /**
     * Spawns the floor's Exit tile ('E') at the Siren's current position,
     * called once the Siren has been defeated.
     */
    public void spawnExit() {
        int sx = this.siren.getX();
        int sy = this.siren.getY();
        this.map[sx][sy] = new Tile(sx, sy, 'E');
    }

    /**
     * Checks whether a given coordinate falls within the Siren's protective
     * exclusion zone, which blocks enemy or switch spawns near her cage
     * while she remains unreleased.
     *
     * @param x the target row horizontal grid coordinate
     * @param y the target column vertical grid coordinate
     * @return true if the coordinate falls within the exclusion zone, false otherwise
     */
    public boolean exclusionZone(int x, int y) {
        boolean exclusionZone = false;
        Siren siren = this.siren;

        //exclusion zone is only applicable if the Siren has yet to be released
        if (!siren.isReleased()) {
            int y_Left, y_Right, x_Bot, x_Top;

            //defining parameters of the exclusion zone
            y_Left = siren.getY() - 8;
            y_Right = siren.getY() + 8;
            x_Bot = siren.getX() + 2;
            x_Top = 0; //top border of the map

            //check if coordinate is within those parameters
            if (x >= x_Top && x <= x_Bot && y <= y_Right && y >= y_Left) {
                exclusionZone = true;
            }
        }

        return exclusionZone;
    }
}