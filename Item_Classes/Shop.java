package Item_Classes;

import Character_Classes.*;

/**
 * Manages item offerings, dynamic menu rendering, and purchasing logic in Hanamaru's Store.
 * Handles store availability, item unlocking based on rescued NPCs, and player transactions.
 * 
 * @author Katigbak and Porciuncula
 * @version 1.0
 */
public class Shop {
    /** The catalog of items available in the shop. */
    private Item[] items;

    /** Flag indicating whether the store is open and accessible. */
    private boolean available;

    /**
     * Constructs Hanamaru's Shop and initializes the item catalog with default inventory.
     */
    public Shop() {
        this.available = true;
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

    /**
     * Displays all shop items using default parameters without player feedback or NPC filters.
     */
    public void displayItems() {
        displayItems(null, null, "");
    }

    /**
     * Displays only unlocked shop items alongside the player's total gold balance 
     * and transaction outcome notifications.
     *
     * @param savedIdols  array of rescued NPChar idols to evaluate unlock conditions
     * @param player      the active PlayableChar accessing the shop
     * @param lastMessage status message from the previous purchase attempt
     */
    public void displayItems(NPChar[] savedIdols, PlayableChar player, String lastMessage) {
        System.out.println("\n************************************************************");
        System.out.println("                     Hanamaru's Store                      ");
        System.out.println("************************************************************");
        System.out.println("Hanamaru: Yohane-chan, zura! What can I do for you today?\n");

        // 1. Display Current Gold Balance
        int currentGold = (player != null) ? player.getGoldOwned() : 0;
        System.out.println("Total Gold: " + currentGold + " GP\n");

        // 2. Display Feedback Message from previous transaction (if any)
        if (lastMessage != null && !lastMessage.isEmpty()) {
            System.out.println(">> " + lastMessage + " <<\n");
        }

        // 3. Display Only Unlocked Items
        int displayIndex = 1;
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            boolean isUnlocked = false;

            if (i == 0 || i == 1) {
                isUnlocked = itemAvailability(null);
            } else if (savedIdols != null) {
                for (NPChar idol : savedIdols) {
                    if (idol != null) {
                        String name = idol.getName();
                        if (i == 2 && name.equalsIgnoreCase("Kanan Matsuura")) isUnlocked = itemAvailability(idol);
                        if (i == 3 && name.equalsIgnoreCase("Riko Sakurauchi")) isUnlocked = itemAvailability(idol);
                        if (i == 4 && name.equalsIgnoreCase("You Watanabe")) isUnlocked = itemAvailability(idol);
                        if (i == 5 && name.equalsIgnoreCase("Mari Ohara")) isUnlocked = itemAvailability(idol);
                        if (i == 6 && name.equalsIgnoreCase("Chika Takami")) isUnlocked = itemAvailability(idol);
                        if (i == 7 && name.equalsIgnoreCase("Dia Kurosawa")) isUnlocked = itemAvailability(idol);
                        if (i == 8 && name.equalsIgnoreCase("Ruby Kurosawa")) isUnlocked = itemAvailability(idol);
                    }
                }
            }

            if (isUnlocked) {
                System.out.printf("[%d] %-25s %d GP%n", displayIndex++, item.getName(), item.getPrice());
            }
        }

        System.out.println("[R]eturn");
        System.out.print("Choice: ");
    }

    /**
     * Checks if a specific shop offering is available based on store state and 
     * the rescue status of the associated NPC idol.
     *
     * @param entity the NPChar required to unlock an item, or null for base items
     * @return true if the store is open and the NPC is saved (or null); false otherwise
     */
    public boolean itemAvailability(NPChar entity) {
        if (!this.available) return false;
        if (entity == null) return true;
        return entity.getSaved();
    }

    /**
     * Processes an item sale transaction for the player after verifying gold 
     * reserves and duplicate ownership rules.
     *
     * @param choice the 1-based index of the item selected from the catalog
     * @param player the PlayableChar attempting to purchase the item
     * @return true if the transaction succeeds; false otherwise
     */
    public boolean sellItem(int choice, PlayableChar player) {
        if (choice < 1 || choice > items.length) {
            System.out.println("Invalid selection.");
            return false;
        }

        Item item = items[choice - 1];

        if (player.getGoldOwned() < item.getPrice()) {
            return false;
        }

        if (!item.getName().equals("Noppo Bread")) {
            for (Item invItem : player.getInventory()) {
                if (invItem.getName().equalsIgnoreCase(item.getName())) {
                    return false;
                }
            }
        }

        return player.buyItem(item);
    }
}