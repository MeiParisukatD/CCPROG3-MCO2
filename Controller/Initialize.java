package Controller;

//class for initializing variables
import java.util.ArrayList; // Import the ArrayList class
import Character_Classes.*;
import Dungeon_Classes.*;
import Item_Classes.*;

/**
 * Handles the one-time setup and randomized generation of core game state.
 * Constructs the playable characters, the roster of rescuable NPCs, the shop
 * item catalog, and the set of dungeons (with randomized names, floor counts,
 * and map files) used to start a game or New Game+ run.
 *
 * @author Katigbak and Porciuncula
 * @version 2.0
 */
public class Initialize {
    /** The catalog of purchasable items available across the run. */
    private Item[] items;
    /** The roster of rescuable idol NPCs available across the run. */
    private NPChar[] NPCs;
    /** The primary playable character. */
    private PlayableChar Yohane;
    /** The secondary playable character, active during the boss fight. */
    private PlayableChar Lailaps;
    /** The set of generated dungeons for this run. */
    private Dungeon[] dungeons;
    /** Helper collection tracking dungeon names and map files already assigned, to avoid duplicates. */
    private ArrayList<String> taken;
    
    /**
     * Constructs the initializer, creating fresh playable characters and
     * generating the run's dungeons. NPCs and items are only generated if
     * they have not already been assigned, so that New Game+ runs can reuse
     * the same rescued-idol and item state.
     */
    public Initialize() {
        this.Yohane = new PlayableChar("Yohane", 3, 1);
        this.Lailaps = new PlayableChar("Lailaps", 4, 0);
        this.taken = new ArrayList<>(); //helper attribute for dungeon-making

        //to facilitate new game+ logic, NPCs and items are not regenerated after the first time
        if (this.NPCs == null) initializeNPCs();
        if (this.items == null) initializeItems();
        initializeDungeons();
    }

    /**
     * Assigns the item catalog to reuse, typically carried over from a prior run.
     *
     * @param items the item catalog to assign
     */
    public void setItems(Item[] items) {
        this.items = items;
    }
    
    /**
     * Assigns the NPC roster to reuse, typically carried over from a prior run.
     *
     * @param NPCs the NPC roster to assign
     */
    public void setNPCs(NPChar[] NPCs) {
        this.NPCs = NPCs;
    }

    /**
     * Retrieves the item catalog.
     *
     * @return the array of items
     */
    public Item[] getItems() {
        return this.items;
    }

    /**
     * Retrieves the NPC roster.
     *
     * @return the array of rescuable NPCs
     */
    public NPChar[] getNPCs() {
        return this.NPCs;
    }

    /**
     * Retrieves the primary playable character.
     *
     * @return the Yohane PlayableChar instance
     */
    public PlayableChar getYohane() {
        return this.Yohane;
    }

    /**
     * Retrieves the secondary playable character.
     *
     * @return the Lailaps PlayableChar instance
     */
    public PlayableChar getLailaps() {
        return this.Lailaps;
    }

    /**
     * Retrieves the set of generated dungeons.
     *
     * @return the array of Dungeon instances for this run
     */
    public Dungeon[] getDungeons() {
        return this.dungeons;
    }

    //initializers
    /**
     * Builds the full catalog of purchasable items, linking each locked item
     * to the NPC whose rescue unlocks it.
     */
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

    /**
     * Builds the fixed roster of rescuable idol NPCs, one per dungeon.
     */
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

    /**
     * Randomly assigns names, floor counts, and map layouts to build the
     * four dungeons for this run: three standard dungeons of increasing
     * floor count, followed by the fixed single-floor Siren boss dungeon.
     */
    private void initializeDungeons() {
        String[] names = { //library of dungeon names
            "Uchiura Bay Pier",
            "Shougetsu Confectionary",
            "Nagahama Castle Ruins",
            "Numazugoyotei",
            "Yasudaya Ryokan",
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
                n = (int)(Math.random() * names.length);
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

    /**
     * Generates a set of Floor instances for a standard dungeon, randomly
     * assigning a distinct, not-yet-used map file to each floor.
     *
     * @param amount the number of floors to generate
     * @return the array of generated Floor instances
     */
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

    /**
     * Resolves the NPC idol associated with a given dungeon name.
     *
     * @param name the name of the dungeon to look up
     * @return the corresponding NPChar rescue target, or null if the name is
     *         unrecognized or belongs to the Siren's dungeon
     */
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
