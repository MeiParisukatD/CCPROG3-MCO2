/*
 * Simplified shop panel implementation for reliable UI testing.
 */
package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import Character_Classes.PlayableChar;
import Item_Classes.*;
import Controller.*;

/**
 * View panel for Hanamaru's Store, letting the player browse unlocked and
 * available shop items page by page and purchase them with gold. Reads
 * item and player state from {@link Controller.GameGUI} and forwards
 * purchases back to {@link Character_Classes.PlayableChar}.
 *
 * @author Katigbak and Porciuncula
 * @version 2.0
 */
public class ShopPanel extends JPanel {
    /** The main application frame, used for card navigation. */
    private final MainFrame frame;
    /** The light pink background color used throughout this panel. */
    private final Color pink = Color.decode("#ffe6f4");
    /** The slightly darker pink used for item boxes. */
    private final Color darkPink = Color.decode("#ffd5f2");
    /** The purple used for text foreground throughout this panel. */
    private final Color purple = Color.decode("#a23e8f");

    /** The header panel shown at the top of this panel. */
    private final JPanel topPanel = new JPanel(new BorderLayout());
    /** The panel holding the paging buttons and the item grid. */
    private final JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
    /** The panel holding the status message and the Return button. */
    private final JPanel bottomPanel = new JPanel(new BorderLayout());
    /** The grid displaying up to four items for the current page. */
    private final JPanel itemGrid = new JPanel(new GridLayout(2, 2, 10, 10));
    /** The button used to navigate back to the dungeon-select menu. */
    private final JButton btnReturn = new JButton("Return");
    /** The button used to move to the previous page of items. */
    private final JButton left = new JButton("<");
    /** The button used to move to the next page of items. */
    private final JButton right = new JButton(">");

    /** The loaded item icons, indexed by {@link #getIconIndex}. */
    private final ImageIcon[] icons = new ImageIcon[9];
    /** The index of the currently displayed page of items. */
    private int pageIndex = 0;

    /**
     * Constructs the shop panel, loads item icons, builds the layout,
     * and refreshes the displayed items and gold total.
     *
     * @param frame the main application frame, used for card navigation
     */
    public ShopPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(pink);

        loadIcons();
        buildUI();
        refresh();
    }

    /**
     * Constructs the shop panel, loads item icons, builds the layout,
     * and refreshes the displayed items and gold total.
     */    
    public void refresh() {
        pageIndex = 0;
        refreshMessage("Yohane-chan, zura! What can I do for you today");
        refreshGold();
        renderPage(pageIndex);
    }

    private void buildUI() {
        topPanel.setBackground(pink);
        JLabel header = new JLabel("Hanamaru's Store", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.PLAIN, 20));
        header.setForeground(purple);
        topPanel.add(header, BorderLayout.CENTER);

        bottomPanel.setBackground(pink);
        JLabel message = new JLabel("Yohane-chan, zura! What can I do for you today", SwingConstants.CENTER);
        message.setFont(new Font("SansSerif", Font.PLAIN, 14));
        message.setForeground(purple);
        bottomPanel.add(message, BorderLayout.CENTER);
        bottomPanel.add(btnReturn, BorderLayout.SOUTH);

        centerPanel.setBackground(darkPink);
        itemGrid.setBackground(darkPink);
        itemGrid.setPreferredSize(new Dimension(360, 220));

        left.setFocusable(false);
        right.setFocusable(false);
        btnReturn.setFocusable(false);
        left.addActionListener(e -> {
            if (pageIndex > 0) {
                pageIndex--;
                renderPage(pageIndex);
            }
        });
        right.addActionListener(e -> {
            Item[] visibleItems = getVisibleItems();
            if ((pageIndex + 1) * 4 < visibleItems.length) {
                pageIndex++;
                renderPage(pageIndex);
            }
        });
        btnReturn.addActionListener(e -> frame.showCard("GAMEMENU"));

        centerPanel.add(left, BorderLayout.WEST);
        centerPanel.add(right, BorderLayout.EAST);
        centerPanel.add(itemGrid, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void refreshMessage(String message) {
        Component old = ((BorderLayout) bottomPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (old != null) bottomPanel.remove(old);
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(purple);
        bottomPanel.add(label, BorderLayout.CENTER);
        bottomPanel.revalidate();
        bottomPanel.repaint();
    }

    private void refreshGold() {
        Component old = ((BorderLayout) topPanel.getLayout()).getLayoutComponent(BorderLayout.SOUTH);
        if (old != null) topPanel.remove(old);

        PlayableChar player = GameGUI.getYohane();
        int goldAmount = (player != null) ? player.getGoldOwned() : 0;
        JLabel gold = new JLabel("Total Gold: " + goldAmount, SwingConstants.CENTER);
        gold.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gold.setForeground(purple);
        topPanel.add(gold, BorderLayout.SOUTH);
        topPanel.revalidate();
        topPanel.repaint();
    }

    private void renderPage(int index) {
        Item[] visibleItems = getVisibleItems();
        if (visibleItems.length == 0) {
            pageIndex = 0;
        } else if (index * 4 >= visibleItems.length) {
            pageIndex = Math.max(0, (visibleItems.length - 1) / 4);
        } else {
            pageIndex = index;
        }

        itemGrid.removeAll();
        itemGrid.setLayout(new GridLayout(0, 2, 10, 10));

        int start = pageIndex * 4;
        for (int slot = 0; slot < 4; slot++) {
            int itemIndex = start + slot;
            Item item = (itemIndex < visibleItems.length) ? visibleItems[itemIndex] : null;
            if (item == null) {
                continue;
            }

            JPanel box = new JPanel(new BorderLayout(4, 4));
            box.setBackground(darkPink);
            box.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

            JButton button = new JButton(getScaledIconForItem(item, 64, 64));
            button.setFocusable(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setPreferredSize(new Dimension(64, 64));
            button.setMinimumSize(new Dimension(64, 64));
            button.setMaximumSize(new Dimension(64, 64));
            button.addActionListener(e -> buyItem(item));

            JLabel name = new JLabel(item.getName(), SwingConstants.CENTER);
            name.setFont(new Font("SansSerif", Font.PLAIN, 11));
            name.setForeground(purple);
            JLabel price = new JLabel(item.getPrice() + " gold", SwingConstants.CENTER);
            price.setFont(new Font("SansSerif", Font.PLAIN, 11));
            price.setForeground(purple);

            box.add(name, BorderLayout.NORTH);
            box.add(button, BorderLayout.CENTER);
            box.add(price, BorderLayout.SOUTH);
            itemGrid.add(box);
        }

        left.setVisible(pageIndex > 0);
        right.setVisible((pageIndex + 1) * 4 < visibleItems.length);

        itemGrid.revalidate();
        itemGrid.repaint();
        centerPanel.revalidate();
        centerPanel.repaint();
        revalidate();
        repaint();
    }
    
    /**
     * Handles a click on an item's buy button: validates gold and availability,
     * locks single-purchase items out of future pages, and on a successful
     * purchase logs the gold spent, refreshes the displayed gold total and
     * message, and re-renders the current page.
     *
     * @param item the shop Item that was clicked
     */
    private void buyItem(Item item) {
        PlayableChar player = GameGUI.getYohane();
        if (player == null || item == null) return;

        if (player.getGoldOwned() < item.getPrice()) {
            refreshMessage("Insufficient gold to buy this item!");
            return;
        }
        if (!item.isAvailable()) {
            refreshMessage("Item is no longer available for purchase.");
            return;
        }
        if (!item.getName().equalsIgnoreCase("Noppo Bread")) {
            item.setAvailable(false);
        }

        boolean success = player.buyItem(item);
        if (success) {
            GameGUI.incrementGoldSpent(item.getPrice());            
            refreshGold();
            refreshMessage("Successfully bought " + item.getName() + "!");
            renderPage(pageIndex);
        }
    }

    private Item[] getCurrentItems() {
        Item[] items = GameGUI.getItems();
        return (items != null) ? items : new Item[0];
    }

    private Item[] getVisibleItems() {
        Item[] items = getCurrentItems();
        int count = 0;
        for (Item item : items) {
            if (item != null && item.isUnlocked() && item.isAvailable()) {
                count++;
            }
        }

        Item[] visibleItems = new Item[count];
        int index = 0;
        for (Item item : items) {
            if (item != null && item.isUnlocked() && item.isAvailable()) {
                visibleItems[index++] = item;
            }
        }
        return visibleItems;
    }

    private ImageIcon getScaledIconForItem(Item item, int width, int height) {
        int iconIndex = getIconIndex(item);
        if (iconIndex < 0 || iconIndex >= icons.length || icons[iconIndex] == null) {
            return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
        }
        Image image = icons[iconIndex].getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    private int getIconIndex(Item item) {
        if (item == null) {
            return -1;
        }

        String name = item.getName().trim().toLowerCase();
        switch (name) {
            case "tears of a fallen angel":
                return 0;
            case "noppo bread":
                return 1;
            case "shovel upgrade":
                return 2;
            case "bat tamer":
                return 3;
            case "air shoes":
                return 4;
            case "stewshine":
                return 5;
            case "mikan mochi":
                return 6;
            case "kurosawa matcha":
                return 7;
            case "choco-mint ice cream":
                return 8;
            default:
                return -1;
        }
    }

    private void loadIcons() {
        icons[0] = loadShopIcon("/resources/TEARS_OF_A_FALLEN_ANGEL.png");
        icons[1] = loadShopIcon("/resources/NOPPO_BREAD.png");
        icons[2] = loadShopIcon("/resources/SHOVEL_UPGRADE.png");
        icons[3] = loadShopIcon("/resources/BAT_TAMER.png");
        icons[4] = loadShopIcon("/resources/AIR_SHOES.png");
        icons[5] = loadShopIcon("/resources/STEWSHINE.png");
        icons[6] = loadShopIcon("/resources/MIKAN_MOCHI.png");
        icons[7] = loadShopIcon("/resources/KUROSAWA_MATCHA.png");
        icons[8] = loadShopIcon("/resources/CHOCO_MINT_ICE_CREAM.png");
    }

    private ImageIcon loadShopIcon(String path) {
        java.net.URL url = getClass().getResource(path);
        if (url == null) {
            return new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
        }
        return new ImageIcon(url);
    }
}
