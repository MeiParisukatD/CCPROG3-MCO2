package Dungeon_Classes;

import Character_Classes.*;
import java.util.Iterator;

public class BossFloor extends Floor {
    private int triggers;
    private Siren siren;

    public BossFloor(int floorNum) {
        super(floorNum, "map_boss.txt");
        this.triggers = 0;
        this.siren = (Siren) this.enemies.get(0);
        this.siren.setFloor(this);
    }

    public BossFloor(Floor floor) {
        super(floor.getFloorNum(), floor.getFile());
        this.triggers = 0;
        this.siren = (Siren) this.enemies.get(0);
        this.siren.setFloor(this);
    }
    
    public Siren getSiren() {
        return this.siren;
    }

    public int getTriggers() {
        return this.triggers;
    }

    public void setTriggers(int triggers) {
        this.triggers = triggers;
    }

    public void incrementTriggers() {
        this.triggers++;
    }

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
        Siren siren = this.siren;
        System.out.println("BossFloor.releaseSiren called, Siren@" + System.identityHashCode(siren));
        System.out.println("Before: released=" + siren.isReleased());
        int sx = this.enemies.get(0).getX();
        int sy = this.enemies.get(0).getY();

        // Define a bounding box around the Siren’s cage
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
        System.out.println("After: released=" + siren.isReleased());
    }

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

    public void spawnExit() {
        int sx = this.siren.getX();
        int sy = this.siren.getY();
        this.map[sx][sy] = new Tile(sx, sy, 'E');
    }

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
