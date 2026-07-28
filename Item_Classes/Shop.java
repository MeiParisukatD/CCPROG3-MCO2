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

    /**
     * Constructs Hanamaru's Shop and initializes the item catalog with default inventory.
     */
    public Shop(Item[] items) {
        this.items = items;
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
    public void displayItems(NPChar[] idols, PlayableChar player, String lastMessage) {
        System.out.println("\n************************************************************");
        System.out.println("                     Hanamaru's Store                      ");
        System.out.println("************************************************************");
        System.out.println("Hanamaru: Yohane-chan, zura! What can I do for you today?\n");

        // 1. Display Current Gold Balance
        int currentGold = (player != null) ? player.getGoldOwned() : 0;
        String YELLOW = "\u001B[38;5;227m";
        String RESET = "\u001B[0m";
        System.out.println("Total Gold: " + YELLOW + currentGold + " GP\n" + RESET);

        // 2. Display Feedback Message from previous transaction (if any)
        if (lastMessage != null && !lastMessage.isEmpty()) {
            System.out.println(">> " + lastMessage + " <<\n");
        }

        // 3. Display Only Unlocked Items
        int displayIndex = 1;
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            if(item.isUnlocked()) {
                System.out.printf("[%d] %-25s %d GP%n", displayIndex++, item.getName(), item.getPrice());
            }
        }

        System.out.println("\n[R]eturn");
        System.out.print("\nChoice: ");
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

        if (!item.isAvailable()) {
            return false;
        }

        if (!(item.getName().equalsIgnoreCase("Tears of a fallen angel") ||
            item.getName().equalsIgnoreCase("Noppo Bread"))) {
            item.setAvailable(false);
        }

        return player.buyItem(item);
    }
}