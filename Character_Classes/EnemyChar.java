//EnemyChar subclass
package Character_classes;

public class EnemyChar extends GameCharacter {
    //attributes
    private int goldDrop;
    private int turnsPerMove;
    private int detectionRange;

    //constructor
    public EnemyChar(String name, int health, int attack, int goldDrop, int turnsPerMove, int detectionRange) {
        super(name, health, attack);
        this.goldDrop = goldDrop;
        this.turnsPerMove = turnsPerMove;
        this.detectionRange = detectionRange;
    }

    //getters/setters
    public int getGoldDrop() {
        return this.goldDrop;
    }

    public void setGoldDrop(int goldDrop) {
        this.goldDrop = goldDrop;
    }

    public int getTurnsPerMove() {
        return this.turnsPerMove;
    }

    public void setTurnsPerMove(int turnsPerMove) {
        this.turnsPerMove = turnsPerMove;
    }

    public int getDetectionRange() {
        return this.detectionRange;
    }

    public void setDetectionRange(int detectionRange) {
        this.detectionRange = detectionRange;
    }

    //additional methods
    public boolean detectPlayer(Tile[][] map, PlayableChar Yohane) {
        //TODO
    }
}