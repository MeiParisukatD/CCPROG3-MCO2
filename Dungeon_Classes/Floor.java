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
        generateMap();
        prevTile = new Tile(0, 0, '.');
        rowLen = map.length;
        colLen = map[0].length;
        this.floorNum = floorNum;
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

    public void generateMap() {
        int row, col, ROW, COL;
        String line;
        char c;
        ROW = 12; //standard row count across all maps
        COL = 55; //standard column count across all maps
        row = col = 0;

        map = new Tile[ROW][COL];
        File file = new File("map1.txt");

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()){
                line = reader.nextLine();

                for (col = 0; col < COL; col++) {
                    //col is y value
                    map[row][col] = new Tile(row, col, line.charAt(col));

                    if (line.charAt(col) == 'b') {
                        enemies.add(createBat(map[row][col]));
                    }

                    if (map[row][col].isDestructible()) {
                        map[row][col] = new DestructibleTile(map[row][col]);
                    }
                }

                row++; //row is x value
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("[!] File not found. Map could not be generated.");
        };
    }

    public void displayMap() {
        int i, j;

        for (i = 0; i < rowLen; i++) {
            for (j = 0; j < colLen; j++) {
                System.out.print(map[i][j].getSymbol());
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
            //check #2: if the new tile is destructible
            if (map[x][y].isDestructible()) {
                valid = true;
            }

            //check #3: if the new tile is passable
            else if (map[x][y].isPassable()) {
                valid = true;
            }
        }

        return valid;
    }

    public void destroyTile(Tile tile) {
        int x, y;
        
        x = tile.getX();
        y = tile.getY();
        map[x][y].setSymbol('.');
        map[x][y].assignProperties();
    }

    public void moveCharacter(Tile prev, Tile next, GameCharacter entity) {
        int x, y;
        char symbol;

        //store previous tile in temp variable
        Tile temp = new Tile(prevTile);
        
        x = next.getX();
        y = next.getY();

        prevTile.setSymbol(map[x][y].getSymbol());
        prevTile.setX(map[x][y].getX());
        prevTile.setY(map[x][y].getY());

        symbol = entity.getTile().getSymbol();
        map[x][y].setSymbol(symbol);
        map[x][y].assignProperties();
        entity.setTile(next);
        
        x = prev.getX();
        y = prev.getY();

        symbol = temp.getSymbol();
        map[x][y].setSymbol(symbol);
        map[x][y].assignProperties();
    }

    private EnemyChar createBat(Tile tile) {
        EnemyChar bat = new EnemyChar(
            "Bat",
            1,      // HP
            1,      // Attack
            5,      // Gold Drop
            2,      // Moves every 2 turns
            1       // Detection Range
        );

        bat.setTile(tile);

        return bat;
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
