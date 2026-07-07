//Floor class
package Dungeon_classes;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import Character_classes.*;

public class Floor {
    //attributes
    private Tile[][] map;
    private ArrayList<EnemyChar> enemies;
    private Tile prevTile;
    private int rowLen;
    private int colLen;
    private int floorNum;

    //constructor
    public Floor(int floorNum) {
        enemies = new ArrayList<>();
        this.floorNum = floorNum;
        generateFloor();
        prevTile = new Tile(0, 0, '.');
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

    public Tile getPrevTile() {
        return this.prevTile;
    }

    public void setPrevTile(Tile prevTile) {
        this.prevTile = prevTile;
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
        char c;
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
                    this.map[row][col] = new Tile(row, col, line.charAt(col));

                    if (this.map[row][col].isDestructible()) {
                        this.map[row][col] = new DestructibleTile(this.map[row][col]);

                        if(line.charAt(col) == 'b' || line.charAt(col) == 'S') {
                            generateEnemy(line.charAt(col), row, col);
                        }
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
                    this.map[row][col]         // Tile
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
                    this.map[row][col]         // Tile
                );
                this.enemies.add(siren);
        }
    }

    public void displayMap() {
        int i, j;
        String COLOR, RESET = "\u001B[0m";

        for (i = 0; i < rowLen; i++) {
            for (j = 0; j < colLen; j++) {
                COLOR = map[i][j].assignColor();
                System.out.print(COLOR + map[i][j].getSymbol() + RESET);
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

        //check #1: if the new tile is within map bounds
        if (x >= 0 && x < rowLen && y >= 0 && y < colLen) {
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
        map[x][y] = new Tile(x, y, '.');
    }

    public void moveCharacter(Tile prev, Tile next, GameCharacter entity) {
        int x, y;
        char symbol;

        //store previous tile in temp variable
        Tile temp = new Tile(prevTile);
        
        //get x and y values of destination tile
        x = next.getX();
        y = next.getY();

        //update prevTile to hold current properties of destination tile
        prevTile.setSymbol(map[x][y].getSymbol());
        prevTile.setX(x);
        prevTile.setY(y);
        prevTile.assignProperties();

        //update destination tile to hold properties of entity's tile
        symbol = entity.getTile().getSymbol();
        next.setSymbol(symbol);
        map[x][y] = new DestructibleTile(next);
        entity.setTile(next);
        
        //get x and y values of tile entity was previously on
        x = prev.getX();
        y = prev.getY();

        //update previous tile to hold properties of temp variable
        symbol = temp.getSymbol();
        map[x][y] = new Tile(x, y, symbol);
    }

    public boolean completeFloor() {
        boolean flag, complete;
        int i, j;

        flag = complete = false;

        //check if Exit Tile exists on the map
        for (i = 0; i < rowLen && !flag; i++) {
            for (j = 0; j < colLen && !flag; j++) {
                if (map[i][j].getSymbol() == 'E') {
                    flag = true;
                }
            }
        }

        //if exit tile does not exist, floor is complete
        if (!flag) {
            complete = true;
        }

        return complete;
    }
}
