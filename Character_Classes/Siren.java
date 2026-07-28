package Character_Classes;

import Dungeon_Classes.*;

public class Siren extends EnemyChar {
    private boolean released;
    //private BossFloor bossFloor;

    public Siren(int x, int y) {
        super("Siren", 1.0f, 10.0f, 750,1, 100, true, x, y);
        //this.bossFloor = null;
        this.released = false;
    }

    public boolean isReleased() {
        return this.released;
    }
    
    public void release() {
        System.out.println("RELEASE SUCCESSFUL");
        this.released = true;
        System.out.println("release() set released=" + this.released + " on Siren@" + System.identityHashCode(this));
    }

    /**
     * Spawns a Bat with tiered difficulty based on switch triggers.
     */
    public void summonBat(int xL, int yL, int xY, int yY) {
        BossFloor bossFloor = (BossFloor)this.floor;
        int tier = bossFloor.getTriggers() + 1; // Tier 1, 2, or 3
        int r, c;
        boolean taken;
        
        do { //finds an empty position for a new bat to spawn
            taken = false;
            r = (int)(Math.random() * bossFloor.getRowLen());
            c = (int)(Math.random() * bossFloor.getColLen());

            //if Lailaps or Yohane already exists in that position
            if ((this.x == xL && this.y == yL) || (this.x == xY && this.y == yY)) {
                taken = true;
            }

            //check if an enemy already exists in that position
            taken = bossFloor.tileTaken(r, c);
        } while (!(bossFloor.validateMove(bossFloor.getMap()[r][c]) && !taken && !bossFloor.exclusionZone(r, c)));

        //create new bat enemy on that tile
        Bat bat = new Bat(
            0.5f * tier,
            5 * tier,
            tier == 1 ? 2 : 1, // Tier 1 moves every 2 turns; Tier 2/3 move every turn
            tier == 3 ? 1.5f : 1,
            tier == 3, // Diagonal allowed on Tier 3
            r, c
        );
        bat.setFloor(bossFloor);

        //add enemy to floor's ArrayList
        bossFloor.addEnemy(bat);
    }

    public void move(PlayableChar Yohane, PlayableChar Lailaps) {
        BossFloor bossFloor = (BossFloor)this.floor;
        System.out.println("RELEASED " + this.released);

        //Siren should only move when released
        if (this.released) {
            //get distance from Siren to both playable characters
            double distY = calcDistance(this.x, this.y, Yohane.getX(), Yohane.getY());
            double distL = calcDistance(this.x, this.y, Lailaps.getX(), Lailaps.getY());

            if (distL <= 1.5) { //if lailaps is adjacent
                System.out.println("DAMAGING LAILAPS");
                dealDmg(Lailaps);
            } else if (distY <= 1.5) { //if yohane is adjacent
                System.out.println("DAMAGING YOHANE");
                dealDmg(Yohane);
            } else { //if neither character is adjacent
                System.out.println("MOVEMENT");
                //target the playable character which is nearer
                PlayableChar target = (distY <= distL) ? Yohane : Lailaps;

                //find direction for Siren to move in
                int dx = Integer.compare(target.getX(), this.x);
                int dy = Integer.compare(target.getY(), this.y);

                int newX = this.x + dx;
                int newY = this.y + dy;

                //check if tile is already taken by another enemy
                boolean taken = bossFloor.tileTaken(newX, newY);

                if (bossFloor.validateMove(bossFloor.getMap()[newX][newY]) && !taken) {
                    this.x = newX;
                    this.y = newY;
                }
            }
        }
    }
}
