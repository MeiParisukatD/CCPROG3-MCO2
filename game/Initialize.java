package game;

//class for initializing variables
import java.util.ArrayList; // Import the ArrayList class
import Character_Classes.*;
import Dungeon_Classes.*;
import Item_Classes.*;

public class Initialize {
    private Item[] items;
    private NPChar[] NPCs;
    private PlayableChar Yohane;
    private PlayableChar Lailaps;
    private Dungeon[] dungeons;
    private ArrayList<String> taken;

    public Initialize() {
        this.Yohane = new PlayableChar("Yohane", 3, 1);
        this.Lailaps = new PlayableChar("Lailaps", 4, 0);
        this.taken = new ArrayList<>(); //helper attribute for dungeon-making

        //to facilitate new game+ logic, NPCs and items are not regenerated after the first time
        if (this.NPCs == null) initializeNPCs();
        if (this.items == null) initializeItems();
        initializeDungeons();
    }

    //Getters
    public void setItems(Item[] items) {
        this.items = items;
    }

    public void setNPCs(NPChar[] NPCs) {
        this.NPCs = NPCs;
    }

    public Item[] getItems() {
        return this.items;
    }

    public NPChar[] getNPCs() {
        return this.NPCs;
    }

    public PlayableChar getYohane() {
        return this.Yohane;
    }

    public PlayableChar getLailaps() {
        return this.Lailaps;
    }

    public Dungeon[] getDungeons() {
        return this.dungeons;
    }

    //initializers
    private void initializeItems() {
        //all game items
        this.items = new Item[] {
            new ConsumableItem("Tears of a fallen angel", 30, 0.5f, null),
            new ConsumableItem("Noppo Bread", 100, 0.5f, null),
            new PassiveItem("Shovel Upgrade", 300, "Spike Immunity", this.NPCs[0]),
            new PassiveItem("Bat Tamer", 400, "Bat Damage Reduction", this.NPCs[6]),
            new PassiveItem("Air Shoes", 500, "Water & Heat Immunity", this.NPCs[5]),
            new UpgradeItem("Stewshine", 1000, "Max Health", 1.0f, this.NPCs[7]),
            new UpgradeItem("Mikan Mochi", 1000, "Max Health", 1.0f, this.NPCs[4]),
            new UpgradeItem("Kurosawa Matcha", 1000, "Max Health", 1.0f, this.NPCs[3]),
            new ConsumableItem("Choco-Mint Ice Cream", 2000, 0.0f, this.NPCs[2])
        };
    }

    private void initializeNPCs() {
        //idols to be saved
        this.NPCs = new NPChar[] {
            new NPChar("Kanan Matsuura"),
            new NPChar("Hanamaru Kunikida"),
            new NPChar("Ruby Kurosawa"),
            new NPChar("Dia Kurosawa"),
            new NPChar("Chika Takami"),
            new NPChar("You Watanabe"),
            new NPChar("Riko Sakurauchi"),
            new NPChar("Mari Ohara")
        };
    }

    private void initializeDungeons() {
        String[] names = { //library of dungeon names
            "Uchiura Bay Pier",
            "Shougetsu Confectionary",
            "Nagahama Castle Ruins",
            "Numazugoyotei",
            "Izu-Mito Sea Paradise",
            "Numazu Deep Sea Aquarium",
            "Awashima Marine Park",
        };
        
        this.dungeons = new Dungeon[4];
        int i, n, NumFloor;
        String name;
        //assigns for every member in this.dungeons
        for (i = 0; i < 4; i++) {
            //if dungeon name is already taken by randomizer, reroll
            do {
                n = (int)(Math.random() * 7);
                name = names[n];
            } while(taken.contains(name));
            this.taken.add(name);

            //deciding number of floors
            switch(i) {
                case 0: NumFloor = 1; break; //1 floor (first dungeon)
                case 1: NumFloor = (int)(Math.random() * 2) + 2; break; //2-3 floors
                case 2: NumFloor = (int)(Math.random() * 2) + 3; break; //3-4 floors
                default: NumFloor = 1; break; //1 floor (final dungeon)
            }

            if (i != 3) { //if not final floor, generate normally
                Floor[] floors = assignFloors(NumFloor);
                this.dungeons[i] = new Dungeon(name, i+1, NumFloor, floors, assignNPC(name));
            } else { //if final floor, boss map
                Floor[] boss = new BossFloor[] {new BossFloor(1)};
                this.dungeons[i] = new Dungeon("Siren of Numazu", i+1, 1, boss, null);
            }
        }
    }

    private Floor[] assignFloors(int amount) {
        Floor[] floors = new Floor[amount];
        String file;
        int i;

        //assigns for every member of local variable floors[]
        for (i = 0; i < amount; i++) {
            //if floor file is already taken by randomizer, reroll
            do {
                int num = (int)(Math.random() * 8) + 1;
                file = "map" + num + ".txt";
            } while(taken.contains(file));
            this.taken.add(file);

            floors[i] = new Floor(i+1, file);
        }

        return floors;
    }

    private NPChar assignNPC(String name) {
        //assigns corresponding NPC based on dungeon name
        if (name.equalsIgnoreCase("Uchiura Bay Pier")) {
            return this.NPCs[0]; //returns Kanan
        } else if (name.equalsIgnoreCase("Shougetsu Confectionary")) {
            return this.NPCs[1]; //returns Hanamaru
        } else if (name.equalsIgnoreCase("Nagahama Castle Ruins")) {
            return this.NPCs[2]; //returns Ruby
        } else if (name.equalsIgnoreCase("Numazugoyotei")) {
            return this.NPCs[3]; //returns Dia
        } else if (name.equalsIgnoreCase("Yasudaya Ryokan")) {
            return this.NPCs[4]; //returns Chika
        } else if (name.equalsIgnoreCase("Izu-Mito Sea Paradise")) {
            return this.NPCs[5]; //returns You
        } else if (name.equalsIgnoreCase("Numazu Deep Sea Aquarium")) {
            return this.NPCs[6]; //returns Riko
        } else if (name.equalsIgnoreCase("Awashima Marine Park")) {
            return this.NPCs[7]; //returns Mari
        } else { //unrecognized dungeon name and/or Siren's dungeon
            return null;
        }
    }
}
