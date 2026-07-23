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
        this.Lailaps = new PlayableChar("Lailaps", 1, 0);
        this.taken = new ArrayList<>(); //helper attribute for dungeon-making

        this.initializeItems();
        this.initializeNPCs();
        this.initializeDungeons();
    }

    //Getters
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
        this.items = new Item[] {
            new ConsumableItem("Tears of a fallen angel", 30, 0.5f),
            new ConsumableItem("Noppo Bread", 100, 0.5f),
            new PassiveItem("Shovel Upgrade", 300, "Spike Immunity"),
            new PassiveItem("Bat Tamer", 400, "Bat Damage Reduction"),
            new PassiveItem("Air Shoes", 500, "Water & Heat Immunity"),
            new UpgradeItem("Stewshine", 1000, "Max Health", 1.0f),
            new UpgradeItem("Mikan Mochi", 1000, "Max Health", 1.0f),
            new UpgradeItem("Kurosawa Matcha", 1000, "Max Health", 1.0f),
            new ConsumableItem("Choco-Mint Ice Cream", 2000, 0.0f)
        };
    }

    private void initializeNPCs() {
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
            "Siren of Numazu"
        };
        
        this.dungeons = new Dungeon[4];
        int i, n, NumFloor;
        int size = this.dungeons.length;
        String name;
        for (i = 0; i < size; i++) {
            //if dungeon name is already taken by randomizer, reroll
            do {
                n = (int)(Math.random() * 8);
                name = names[n];
                this.taken.add(name);
            } while(taken.contains(name));

            //deciding number of floors
            switch(i) {
                case 0: NumFloor = 1; break; //1 floor (first dungeon)
                case 1: NumFloor = (int)(Math.random() * 2) + 2; break; //2-3 floors
                case 2: NumFloor = (int)(Math.random() * 2) + 3; break; //3-4 floors
                default: NumFloor = 1; break; //1 floor (final dungeon)
            }

            Floor[] floors = assignFloors(NumFloor);
            this.dungeons[i] = new Dungeon(name, i, NumFloor, floors, assignNPC(name));
        }
    }

    private Floor[] assignFloors(int amount) {
        Floor[] floors = new Floor[amount];
        int i;

        for (i = 0; i < amount; i++) {
            //if floor file is already taken by randomizer, reroll
            do {
                floors[i] = new Floor(i+1);
                this.taken.add(floors[i].getFile());
            } while(taken.contains(floors[i].getFile()));
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
