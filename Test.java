//testing file
import Character_Classes.*;
import Dungeon_Classes.*;
import Item_Classes.*;

public class Test {
    public static void start(PlayableChar Yohane, Floor floor) {
        int x, y;
        Yohane.findCharTile(floor.getMap());
        x = Yohane.getX();
        y = Yohane.getY();
        floor.getMap()[x][y] = new Tile(x, y, '.');
    }

    public static void main (String[] args) {
        PlayableChar Yohane = new PlayableChar("Yohane", 0, 0, null);

        //testing if randomizer and 1/0 floor assignment works
        System.out.println("RANDOMIZER AND 1/0 ASSIGNMENT TESTING");
        Floor t1 = new Floor(1);
        System.out.println(t1.getFile());
        Floor t2 = new Floor(2);
        System.out.println(t2.getFile());
        Floor t3 = new Floor(3);
        System.out.println(t3.getFile());
        Floor t4 = new Floor(0);
        System.out.println(t4.getFile());
        System.out.println();

        System.out.println("MAP TESTING (ALL MAPS)");
        System.out.println("MAP 1 GENERATE...");
        Floor m1 = new Floor(0, "map1.txt");
        System.out.println("MAP 2 GENERATE...");
        Floor m2 = new Floor(0, "map2.txt");
        System.out.println("MAP 3 GENERATE...");
        Floor m3 = new Floor(0, "map3.txt");
        System.out.println("MAP 4 GENERATE...");
        Floor m4 = new Floor(0, "map4.txt");
        System.out.println("MAP 5 GENERATE...");
        Floor m5 = new Floor(0, "map5.txt");
        System.out.println("MAP 6 GENERATE...");
        Floor m6 = new Floor(0, "map6.txt");
        System.out.println("MAP 7 GENERATE...");
        Floor m7 = new Floor(0, "map7.txt");
        System.out.println("MAP 8 GENERATE...");
        Floor m8 = new Floor(0, "map8.txt");
        System.out.println("MAP 9 GENERATE...");
        Floor m9 = new Floor(0, "map_boss.txt");
        System.out.println("BOSS MAP GENERATE...");

        start(Yohane, m1);
        m1.displayMap(Yohane);
        System.out.println();

        start(Yohane, m2);
        m2.displayMap(Yohane);
        System.out.println();

        start(Yohane, m3);
        m3.displayMap(Yohane);
        System.out.println();

        start(Yohane, m4);
        m4.displayMap(Yohane);
        System.out.println();

        start(Yohane, m5);
        m5.displayMap(Yohane);
        System.out.println();

        start(Yohane, m6);
        m6.displayMap(Yohane);
        System.out.println();

        start(Yohane, m7);
        m7.displayMap(Yohane);
        System.out.println();

        start(Yohane, m8);
        m8.displayMap(Yohane);
        System.out.println();
        
        start(Yohane, m9);
        m9.displayMap(Yohane);
        System.out.println();

        Floor ex = new Floor(0, "map1.txt");
    }
}

