//Floor class
package Dungeon_Classes;

import java.util.ArrayList;
import java.io.InputStream;

import java.util.Scanner;
import Character_Classes.*;

/**
 * Represents a single layer or floor layout within the dungeon.
 * Handles reading structural text maps, generating and spawning hostile entities,
 * rendering colorized matrix maps to the console, and evaluating step coordinates.
 *
 * @author Katigbak and Porciuncula
 * @version 2.0
 */
public class Floor {
    //attributes
    /** The 2D grid matrix of Tile items making up the map landscape. */
    protected Tile[][] map;
    /** The name of the text map file this floor is generated from. */
    protected String file;
    /** The active tracking collection storing hostile NPCs on this floor layer. */
    protected ArrayList<EnemyChar> enemies;
    /** The overall row boundary limit capacity of the current map matrix grid. */
    protected int rowLen;
    /** The overall column boundary limit capacity of the current map matrix grid. */
    protected int colLen;
    /** The designation or layout index track level number of this floor. */
    protected int floorNum;
    /** The number of the dungeon (1st/2nd/3rd) this floor belongs to; used to scale enemy difficulty. */
    protected int dungeonNum;
    /** Flag tracking whether this floor has been cleared (the player has reached its exit). */
    protected boolean complete;

    //constructor
    /**
     * Constructs a floor tracking environment, sets standard boundaries, and populates
     * structural elements via dynamic tile generations.
     *
     * @param floorNum the unique level index tracking identifier for this map
     * @param dungeonNum the number of the dungeon (1st/2nd/3rd) this floor belongs to;
     *                    must be set before generateFloor() runs since that call spawns
     *                    enemies using this value
     * @param file the name of the text map file to generate this floor's layout from
     */
    public Floor(int floorNum, int dungeonNum, String file) {
        enemies = new ArrayList<>();
        this.floorNum = floorNum;
        this.dungeonNum = dungeonNum;
        this.file = file;
        this.generateFloor();
        rowLen = map.length;
        colLen = map[0].length;
        this.complete = false;
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
     * Retrieves the name of the text map file this floor was generated from.
     *
     * @return the source map file name
     */
    public String getFile() {
        return this.file;
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
     * Gets the number of the dungeon (1st/2nd/3rd) this floor belongs to.
     *
     * @return the dungeon number this floor belongs to
     */
    public int getDungeonNum() {
        return this.dungeonNum;
    }

    /**
     * Sets the number of the dungeon (1st/2nd/3rd) this floor belongs to.
     * Used to scale enemy difficulty independently of the floor's position within the dungeon.
     *
     * @param dungeonNum the dungeon number to record
     */
    public void setDungeonNum(int dungeonNum) {
        this.dungeonNum = dungeonNum;
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
     * Appends a new hostile entity to this floor's active enemy tracking collection.
     *
     * @param enemy the EnemyChar instance to add
     */
    public void addEnemy(EnemyChar enemy) {
        this.enemies.add(enemy);
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
        ArrayList<Tile> enemyPositions = new ArrayList<>();
        int row, col, ROW, COL;
        String line;
        ROW = 12; //standard row count across all maps
        COL = 55; //standard column count across all maps
        row = col = 0;

        this.map = new Tile[ROW][COL];
        InputStream stream = getClass().getResourceAsStream("/" + this.file);

        if (stream == null) {
            System.out.println("[!] File not found: " + this.file);
            return;
        }

        this.enemies.clear();

        try (Scanner reader = new Scanner(stream)) {
            while (reader.hasNextLine()){
                line = reader.nextLine();

                for (col = 0; col < COL; col++) {
                    //col is y value
                    char symbol = line.charAt(col);

                    if (symbol == 'b') {
                        this.map[row][col] = new Tile(row, col, '.'); // floor tile
                        enemyPositions.add(new Tile(row, col, 'b'));
                    } else if (symbol == 'S') {
                        this.map[row][col] = new Tile(row, col, '.'); // floor tile
                        enemyPositions.add(new Tile(row, col, 'S'));
                    } else {
                        this.map[row][col] = new Tile(row, col, symbol);
                        if (this.map[row][col].isDestructible()) {
                            this.map[row][col] = new DestructibleTile(this.map[row][col]);
                        }
                    }
                }
                row++; //row is x value
            }
            //generate enemies from collected positions
            this.generateEnemies(enemyPositions);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Instantiates hostile NPCs directly into the floor layout tracker, scaling metrics
     * like attack thresholds or step movement delay intervals based on current level progress.
     *
     * @param positions the list of Tile markers collected during map parsing, each carrying
     *                   the enemy type symbol ('b' for Bat, 'S' for Siren) and spawn coordinates
     */
    private void generateEnemies(ArrayList<Tile> positions) {
        int i, size;
        size = positions.size();

        for (i = 0; i < size; i++) {
            char symbol = positions.get(i).getSymbol();
            int x = positions.get(i).getX();
            int y = positions.get(i).getY();

            switch (symbol) {
                case 'b': //bat position
                    //decides number of moves per turn for bat, scaled by which dungeon this is
                    int moves = (this.dungeonNum == 1) ? 2 : 1;
                    float detection;

                    //create bat
                    EnemyChar bat = new Bat(
                        0.5f * this.dungeonNum,      // Attack
                        5 * this.dungeonNum,         // Gold Drop
                        moves,                       // Moves every 1-2 turns
                        detection = (this.dungeonNum == 3) ? 1.5f : 1.0f,
                        this.dungeonNum == 3,       // diagonal movement if 3rd dungeon
                        x, y                    //coordinates
                    );

                    //add bat to enemy arraylist
                    this.enemies.add(bat);
                    break;
                case 'S': //siren position
                    //create siren
                    EnemyChar siren = new Siren(x, y);

                    //add siren to enemy arraylist
                    this.enemies.add(siren);
                    break;
            }
        }

        //once all enemies are generated, attach current floor to each
        for (EnemyChar enemy: this.enemies) {
            enemy.setFloor(this);
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

        //check #1: if the destination tile is within map bounds
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
        int i, j, x, y;

        x = entity.getX();
        y = entity.getY();

        //find Exit tile
        for (i = 0; i < this.rowLen; i++) {
            for (j = 0; j < this.colLen; j++) {
                if (map[i][j].getSymbol() == 'E' && x == map[i][j].getX() && y == map[i][j].getY()) {
                    this.complete = true;
                }
            }
        }

        return this.complete;
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
        //check if enemy exists on parameter coordinates
        for (EnemyChar enemy : enemies) {
            if (enemy.getX() == x &&
                enemy.getY() == y) {
                    return enemy;
            }
        }
        return null;
    }

    /**
     * Checks whether an enemy currently occupies the given grid coordinates.
     *
     * @param x target row horizontal grid element coordinate
     * @param y target column vertical grid element coordinate
     * @return true if an enemy occupies the tile, false otherwise
     */
    public boolean tileTaken(int x, int y) {
        //check for enemy
        EnemyChar enemy = this.findEnemy(x, y);

        //if enemy found at that position, tile is taken
        if (enemy != null && enemy.getX() == x && enemy.getY() == y) {
            return true;
        }

        //otherwise, tile is not taken
        return false;
    }
}