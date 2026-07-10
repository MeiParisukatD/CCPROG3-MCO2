//Dungeon Class
package Dungeon_Classes;
import Character_Classes.*;

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
        this.curFloor = 1; //curFloor always starts at one
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

    //additional methods
    public void assignFloors(Floor[] catalogue) {
        //TODO
    }

    public boolean isCompleted(PlayableChar entity) {
        this.completion = this.floors[numFloors-1].completeFloor(entity);
        return this.completion;
    }

    public void incrementCurFloor() {
        if (this.curFloor != this.numFloors) {
            this.curFloor++;
        }
    }

    public boolean gameOver(PlayableChar entity) {
        boolean gameOver = entity.charDeath();
        String RED, RESET;
        RED = "\u001B[38;5;196m";
        RESET = "\u001B[0m";

        if (gameOver) {
            System.out.println(RED + "You Died!" + RESET );
            System.out.println("Killed by " + RED + entity.getCauseOfDeath() + RESET);
        }
        return gameOver;
    }
}
