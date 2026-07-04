//Dungeon Class
package Dungeon_classes;
import Character_classes.*;

public class Dungeon {
    //attributes
    private String name;
    private int dungeonNum;
    private int numFloors;
    private int curFloor;
    private Floor[] floors;
    private boolean completion;

    //constructor
    public Dungeon(String name, int dungeonNum, int numFloors, Floor[] floors) {
        this.name = name;
        this.dungeonNum = dungeonNum;
        this.numFloors = numFloors;
        this.floors = floors;
        this.completion = false;
    }

    //getters/setters
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDungeonNum() {
        return this.dungeonNum;
    }

    public void setDungeonNum(int dungeonNum) {
        this.dungeonNum = dungeonNum;
    }

    public int getNumFloors() {
        return this.numFloors;
    }

    public void setNumFloors(int numFloors) {
        this.numFloors = numFloors;
    }

    public int getCurFloor() {
        return this.curFloor;
    }

    public void setCurFloor(int curFloor) {
        this.curFloor = curFloor;
    }

    public Floor[] getFloors() {
        return this.floors;
    }

    public void setFloors(Floor[] floors) {
        this.floors = floors;
    }

    public boolean isCompletion() {
        return this.completion;
    }

    public boolean getCompletion() {
        return this.completion;
    }

    public void setCompletion(boolean completion) {
        this.completion = completion;
    }

    //additional methods
    public void assignFloors(Floor[] catalogue) {
        //TODO
    }

    public boolean gameOver(PlayableChar Yohane) {
        return Yohane.charDeath();
    }
}
