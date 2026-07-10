//Floor class
package Dungeon_Classes;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import Character_Classes.*;


public class Floor {
    //attributes
    private Tile[][] map;
    private ArrayList<EnemyChar> enemies;
    private int rowLen;
    private int colLen;
    private int floorNum;

    //constructor
    public Floor(int floorNum) {
        enemies = new ArrayList<>();
        this.floorNum = floorNum;
        generateFloor();
        rowLen = map.length;
        colLen = map[0].length;
    }  

    //getters/setters
    public Tile[][] getMap() {
        return this.map;
    }

    public void setMap(Tile[][] map) {
        this.map = map;
    }

    public int getFloorNum() {
        return this.floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }  

    public ArrayList<EnemyChar> getEnemies() {
        return enemies;
    }

    public int getRowLen() {
        return this.rowLen;
    }

    public void setRowLen(int rowLen) {
        this.rowLen = rowLen;
    }

    public int getColLen() {
        return this.colLen;
    }

    public void setColLen(int colLen) {
        this.colLen = colLen;
    }


    //additional methods
    public void generateFloor() {
        int row, col, ROW, COL;
        String line;
        ROW = 12; //standard row count across all maps
        COL = 55; //standard column count across all maps
        row = col = 0;

        this.map = new Tile[ROW][COL];
        File file = new File("map1.txt");

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

    public void destroyTile(Tile tile) {
        int x, y;
        
        x = tile.getX();
        y = tile.getY();

        //sets tile to a passable Tile
        map[x][y] = new Tile(x, y, '.');
    }

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
