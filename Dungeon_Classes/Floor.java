//Floor class
package Dungeon_classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Floor {
    //attributes
    private Tile[][] map;
    private Tile prevTile;
    private int rowLen;
    private int colLen;
    private int floorNum;

    //constructor
    public Floor(int floorNum) {
        generateMap();
        prevTile = null;
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

    //additional methods

    public void generateMap() {
        int row, col, ROW, COL;
        String line;
        ROW = 12; //standard row count across all maps
        COL = 55; //standard column count across all maps
        row = col = 0;

        map = new Tile[ROW][COL];
        File file = new File("map1.txt");

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()){
                line = reader.nextLine();

                for (col = 0; col < COL; col++) {
                    map[row][col] = new Tile(row, col, line.charAt(col));
                    //col is y value
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

    public boolean validateMove(char direction, Character entity) {
        boolean valid;
        int x, y;

        valid = false;
        x = entity.getTile().getX();
        y = entity.getTile().getY();

        switch (direction) {
            case 'w': y++; break;
            case 's': y--; break;
            case 'd': x++; break;
            case 'a': x--; break;
            default: 
                System.out.println("[!] Invalid direction.");
                break;
        }

        //check #1: if the new tile is within map bounds
        if (x >= 0 && x < rowLen && y >= 0 && y < colLen) {
            //check #2: if the new tile is destructible (only applicable for Yohane)
            if (map[x][y].isDestructible() && entity instanceof PlayableChar) {
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
    
    public boolean gameOver() {
        //TODO
        return false;
    }
}
