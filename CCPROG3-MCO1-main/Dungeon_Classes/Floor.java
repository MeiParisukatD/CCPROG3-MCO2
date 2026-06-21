//Floor class
package Dungeon_classes;

public class Floor {
    //attributes
    private Tile[][] map;
    private int floorNum;

    //constructor
    public Floor(Tile[][] map, int floorNum) {
        this.map = map;
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
    public void displayStats(PlayableChar Yohane) {
        //TODO
    }

    public void displayMap() {
        //TODO  
    }

    public Tile[][] generateMap() {
        //TODO
    }

    public boolean completeFloor() {
        //TODO
    }
    
    public boolean gameOver() {
        //TODO
    }
}
