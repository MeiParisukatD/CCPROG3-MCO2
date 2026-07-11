//Floor class
package Dungeon_Classes;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import Character_Classes.*;

/**
 * Represents a single layer or floor layout within the dungeon.
 * Handles reading structural text maps, generating and spawning hostile entities, 
 * rendering colorized matrix maps to the console, and evaluating step coordinates.
 * 
 * @author Katigbak and Porciuncula
 * @version 1.0
 */
public class Floor {
    //attributes
    /** The 2D grid matrix of Tile items making up the map landscape. */
    private Tile[][] map;
    /** The active tracking collection storing hostile NPCs on this floor layer. */
    private ArrayList<EnemyChar> enemies;
    /** The overall row boundary limit capacity of the current map matrix grid. */
    private int rowLen;
    /** The overall column boundary limit capacity of the current map matrix grid. */
    private int colLen;
    /** The designation or layout index track level number of this floor. */
    private int floorNum;

    //constructor
    /**
     * Constructs a floor tracking environment, sets standard boundaries, and populates 
     * structural elements via dynamic tile generations.
     *
     * @param floorNum the unique level index tracking identifier for this map
     */
    public Floor(int floorNum) {
        enemies = new ArrayList<>();
        this.floorNum = floorNum;
        generateFloor();
        rowLen = map.length;
        colLen = map[0].length;
    }  

    //getters/setters
    /**
     * Retrieves the entire grid layout of the game world.
     * 
     * @return the multi-dimensional layout array of tiles
     */
    public Tile[][] getMap() {
        return this.map;
    }

    /**
     * Overwrites the current game grid with a completely new grid layout.
     * 
     * @param map the replacement 2D array structure layout
     */
    public void setMap(Tile[][] map) {
        this.map = map;
    }

    /**
     * Gets the current level or dungeon floor the player is exploring.
     * 
     * @return the current floor numerical index
     */
    public int getFloorNum() {
        return this.floorNum;
    }

    /**
     * Updates the current floor tracker to a new level index.
     * 
     * @param floorNum the new floor number value to record
     */
    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }  

    /**
     * Retrieves a collection of all active opponents currently present on this floor.
     * 
     * @return the list reference containing spawned enemies
     */
    public ArrayList<EnemyChar> getEnemies() {
        return enemies;
    }

    /**
     * Gets the maximum number of rows (horizontal height) defining the map boundaries.
     * 
     * @return the row maximum size metric
     */
    public int getRowLen() {
        return this.rowLen;
    }

    /**
     * Sets the maximum number of rows to redefine the map's horizontal boundary.
     * 
     * @param rowLen the new horizontal limit length
     */
    public void setRowLen(int rowLen) {
        this.rowLen = rowLen;
    }

    /**
     * Gets the maximum number of columns (vertical width) defining the map boundaries.
     * 
     * @return the column maximum size metric
     */
    public int getColLen() {
        return this.colLen;
    }

    /**
     * Sets the maximum number of columns to redefine the map's vertical boundary.
     * 
     * @param colLen the new vertical limit length
     */
    public void setColLen(int colLen) {
        this.colLen = colLen;
    }
    
    //additional methods
    /**
     * Parses the flat document matrix file stream to initialize structural symbols, 
     * allocating destructible elements and forwarding enemy spawn definitions seamlessly.
     */
    public void generateFloor() {
        int row, col, ROW, COL;
        String line;
        ROW = 12; //standard row count across all maps
        COL = 55; //standard column count across all maps
        row = col = 0;

        this.map = new Tile[ROW][COL];
        File file = new File("map1.txt");
        this.enemies.clear();

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()){
                line = reader.nextLine();

                for (col = 0; col < COL; col++) {
                    //col is y value
                    char symbol = line.charAt(col);

                    switch (symbol) {
                    case 'b': // bat spawn
                        this.map[row][col] = new Tile(row, col, '.'); // floor tile
                        generateEnemy('b', row, col);
                        break;
                    case 'S': // siren spawn
                        this.map[row][col] = new Tile(row, col, '.');
                        generateEnemy('S', row, col);
                        break;
                    default:
                        this.map[row][col] = new Tile(row, col, symbol);
                        if (this.map[row][col].isDestructible()) {
                            this.map[row][col] = new DestructibleTile(this.map[row][col]);
                        }
                        break;
                }
                }

                row++; //row is x value
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("[!] File not found. Map could not be generated.");
        };
    }

    /**
     * Instantiates hostile NPCs directly into the floor layout tracker, scaling metrics 
     * like attack thresholds or step movement delay intervals based on current level progress.
     *
     * @param symbol the character key identifying enemy types ('b' for Bat, 'S' for Siren)
     * @param row the target X coordinate destination
     * @param col the target Y coordinate destination
     */
    private void generateEnemy(char symbol, int row, int col) {
        switch (symbol) {
            case 'b': //spawns Bat and assigns to corresponding tile
                int moves = this.floorNum == 1 ? 2 : 1;

                EnemyChar bat = new EnemyChar(
                    "Bat",
                    1.0f,                      // HP
                    0.5f * this.floorNum,      // Attack
                    5 * this.floorNum,         // Gold Drop
                    moves,                     // Moves every 2 turns
                    1,                         // Detection Range
                    row,
                    col
                );
                this.enemies.add(bat);
                break;
            case 'S': //spawns Siren and assigns to corresponding tile
                EnemyChar siren = new EnemyChar(
                    "Siren",
                    1.0f,                      // HP
                    10.0f,                     // Attack
                    750,                       // Gold Drop
                    1,                         // Moves every turn
                    rowLen,                    // Detection Range
                    row,
                    col
                );
                this.enemies.add(siren);
        }
    }

    /**
     * Compiles and outputs colorized display strings to print the visual map grid out, 
     * handling priority layer overlays for the player character and active enemy status transformations.
     *
     * @param player the user character instance utilized to cross-check grid overlapping
     */
    public void displayMap(PlayableChar player) {
        int i, j;
        String COLOR, RESET = "\u001B[0m";

        for (i = 0; i < rowLen; i++) {
            for (j = 0; j < colLen; j++) {
                boolean occupied = false;

                // check player
                if (player.getX() == i && player.getY() == j) {
                    COLOR = "\u001B[38;5;153m";
                    System.out.print(COLOR + 'Y' + RESET);
                    occupied = true;
                }

                // check enemies
                for (EnemyChar e : this.enemies) {
                    if (e.getX() == i && e.getY() == j) {
                        COLOR = "\u001B[38;5;196m";
                        char symbol = e.getName().equals("Bat") ? 'b' : 'S';

                        if (e.getName().equals("Bat") && e.detectPlayer(this.map, player)) {
                            symbol = 'B';
                        }

                        System.out.print(COLOR + symbol + RESET);
                        occupied = true;
                        break;
                    }
                }

                // if no entity, print base tile
                if (!occupied) {
                    COLOR = map[i][j].assignColor(); //assigns color before printing
                    System.out.print(COLOR + map[i][j].getSymbol() + RESET);
                }
            }
            System.out.println();
        }
    }

    /**
     * Validates if a chosen step destination falls safely inside the boundaries of the 
     * map grid and checks whether the physical layout features allow structural passability.
     *
     * @param dest the target destination Tile component node being evaluated
     * @return true if the character can move into this position, false otherwise
     */
    public boolean validateMove(Tile dest) {
        boolean valid;
        int x, y;

        valid = false;
        x = dest.getX();
        y = dest.getY();

        //check #1: if the destinatino tile is within map bounds
        if (x >= 0 && x < rowLen && y >= 0 && y < colLen) {
            //check #2: if the destination tile is passable
            if (map[x][y].isPassable()) {
                valid = true;
            }
        }

        return valid;
    }

    /**
     * Replaces a specific block node with a regular, passable floor layout tile upon breakdown.
     *
     * @param tile the blocking landscape component being wiped
     */
    public void destroyTile(Tile tile) {
        int x, y;
        
        x = tile.getX();
        y = tile.getY();

        //sets tile to a passable Tile
        map[x][y] = new Tile(x, y, '.');
    }

    /**
     * Checks if the user-controlled character stands directly on top of the escape Exit structure, 
     * qualifying them to successfully finish exploration on this map level layer.
     *
     * @param entity the user character instance to cross-reference
     * @return true if escape condition requirements are reached, false otherwise
     */
    public boolean completeFloor(PlayableChar entity) {
        boolean complete;
        int i, j, x, y;

        x = entity.getX();
        y = entity.getY();
        complete = false;

        //find Exit tile
        for (i = 0; i < this.rowLen; i++) {
            for (j = 0; j < this.colLen; j++) {
                if (map[i][j].getSymbol() == 'E' && x == map[i][j].getX() && y == map[i][j].getY()) {
                    complete = true;
                }
            }
        }

        return complete;
    }

    /**
     * Iterates across tracking lists to find if a hostile enemy occupies the specified 
     * coordinate values.
     *
     * @param x target row horizontal grid element coordinate
     * @param y target column vertical grid element coordinate
     * @return the matching EnemyChar instance found at that position, or null if none exist
     */
    public EnemyChar findEnemy(int x, int y) {
        for (EnemyChar enemy : enemies) {
            if (enemy.getX() == x &&
                enemy.getY() == y) {
                    return enemy;
            }
        }
        return null;
    }
}
