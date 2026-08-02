/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import Character_Classes.PlayableChar;
import Item_Classes.*;
import game.*;
/**
 *
 * @author rhian
 */
public class ShopPanel extends JPanel {
    private ImageIcon[] icons;

    private MainFrame frame;
    private JPanel topPanel;
    private JPanel botPanel;
    private JPanel center;
    private JPanel itemGrid;
    private JButton btnReturn;
    private JButton left;
    private JButton right;

    private int pageIndex;

    private Color pink = Color.decode("#ffe6f4");
    private Color darkPink = Color.decode("#ffd5f2");
    private Color purple = Color.decode("#a23e8f");

    /**
     * Creates new form ShopPanel
     */
    public ShopPanel(MainFrame frame) {
        //initComponents();
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(pink);

        //initialize icons
        this.initializeIcons();

        //initialize buttons
        this.btnReturn = new JButton("Return");
        this.left = new JButton("<");
        this.right = new JButton(">");
        btnReturn.addActionListener(this::btnReturnActionPerformed);

        //scroll button actions
        Item[] items = game.GameGUI.getItems();
        this.left.addActionListener(e -> {
            if (pageIndex > 0) {
                pageIndex--;
                itemDisplay(pageIndex, items);
            }
        });

        this.right.addActionListener(e -> {
            if (pageIndex < 3) {
                pageIndex++;
                itemDisplay(pageIndex, items);
            }
        });

        //top panel
        this.topPanel = new JPanel(new BorderLayout());
        //header
        this.topPanel.setBackground(pink);
        JLabel header = new JLabel("Hanamaru's Store", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.PLAIN, 20));
        header.setForeground(purple);
        this.topPanel.add(header, BorderLayout.CENTER);
        //gold tracker
        JLabel gold = new JLabel("Total Gold: " + game.GameGUI.getYohane().getGoldOwned(), SwingConstants.CENTER);
        gold.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gold.setForeground(purple);
        this.topPanel.add(gold, BorderLayout.SOUTH);

        //bottom panel
        this.botPanel = new JPanel(new BorderLayout());
        this.botPanel.setBackground(pink);
        JLabel message = new JLabel("Yohane-chan, zura! What can I do for you today");
        message.setFont(new Font("SansSerif", Font.PLAIN, 14));
        message.setForeground(purple);
        this.botPanel.add(message, BorderLayout.CENTER);
        this.botPanel.add(btnReturn, BorderLayout.SOUTH);

        this.pageIndex = 0;

        //item grid
        this.itemGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        this.itemGrid.setBackground(darkPink);
        //center panel
        this.center = new JPanel(new BorderLayout());
        this.center.setBackground(darkPink);
        this.center.add(this.left, BorderLayout.WEST);
        this.center.add(this.right, BorderLayout.EAST);
        this.center.add(this.itemGrid, BorderLayout.CENTER);

        //add to shop panel
        add(topPanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(botPanel, BorderLayout.SOUTH);
    }

    public void refresh() {
        //refresh components
        this.refreshMessage("Yohane-chan, zura! What can I do for you today");
        this.itemDisplay(0, game.GameGUI.getItems());
        this.refreshGold();
    }

    private void refreshMessage(String message) {
        //remove message
        BorderLayout layout = (BorderLayout) this.botPanel.getLayout();
        Component centerComp = layout.getLayoutComponent(BorderLayout.CENTER);
        if (centerComp != null) {
            this.botPanel.remove(centerComp);
        }

        //insert new message
        JLabel msg = new JLabel(message);
        msg.setFont(new Font("SansSerif", Font.PLAIN, 14));
        msg.setForeground(purple);
        this.botPanel.add(msg, BorderLayout.CENTER);

        this.botPanel.revalidate();
        this.botPanel.repaint();
    }

    private void refreshGold() {
        //remove current gold
        BorderLayout layout = (BorderLayout) this.topPanel.getLayout();
        Component southComp = layout.getLayoutComponent(BorderLayout.SOUTH);
        if (southComp != null) {
            this.topPanel.remove(southComp);
        }

        //update new gold
        JLabel gold = new JLabel("Total Gold: " + game.GameGUI.getYohane().getGoldOwned());
        gold.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gold.setForeground(purple);
        this.topPanel.add(gold, BorderLayout.SOUTH);

        this.topPanel.revalidate();
        this.topPanel.repaint();
    }

    private void itemDisplay(int index, Item[] items) {
        this.itemGrid.removeAll();
        int i, j, k, size = items.length;

        //determines where in the array to get the items from
        if (index == 2) { i = 8; }
        else if (index == 1) { i = 4; }
        else { i = 0; }
        j = i + 4;
        k = 0;

        //creating item display
        JButton[] itemButtons = new JButton[4];
        Item[] visible = new Item[4];
        while (i < j) {
            if (i < size) {
                visible[k] = items[i];
            } else {
                visible[k] = null;
            }

            if (visible[k] == null) { //dummy button
                itemButtons[k] = new JButton("x");
            } else if (items[i].isAvailable()) { //if available
                itemButtons[k] = new JButton(this.icons[i]);
            } else { //if unavailable
                Image grayImage = GrayFilter.createDisabledImage(this.icons[i].getImage());
                ImageIcon grayIcon = new ImageIcon(grayImage);
                itemButtons[k] = new JButton(grayIcon);
            }

            //fix button display
            itemButtons[k].setBorderPainted(false);
            itemButtons[k].setContentAreaFilled(false);

            //item container details
            JPanel box = new JPanel(new BorderLayout());
            box.setBackground(darkPink);
            
            if (visible[k] != null) {
                JLabel name = new JLabel(items[i].getName(), SwingConstants.CENTER);
                name.setForeground(purple);
                JLabel price = new JLabel(items[i].getPrice() + " gold", SwingConstants.CENTER);
                price.setForeground(purple);

                //insert in item container
                box.add(name, BorderLayout.NORTH);
                box.add(itemButtons[k], BorderLayout.CENTER);
                box.add(price, BorderLayout.SOUTH);
            } else {
                //dummy button - just add the button
                box.add(itemButtons[k], BorderLayout.CENTER);
            }
            this.itemGrid.add(box);

            i++;
            k++;
        }

        //defining button actions
        for (int x = 0; x < 4; x++) {
            //to satisfy compiler, create final variable
            final int buttonIndex = x;
            itemButtons[x].addActionListener(e -> {
                if (visible[buttonIndex] != null) {
                    sellItem(visible[buttonIndex], game.GameGUI.getYohane());
                }
            });
        }

        //update arrow visibility depending on page number
        left.setVisible(pageIndex > 0);
        right.setVisible(pageIndex < 2);

        this.center.revalidate();
        this.center.repaint();
    }

    public boolean sellItem(Item item, PlayableChar player) {
        if (item == null) {
            return false;
        } //if item is null

        if (player.getGoldOwned() < item.getPrice()) {
            this.refreshMessage("Insufficient gold to buy this item!");
            return false;
        } //if player has insufficient gold

        if (!item.isAvailable()) {
            this.refreshMessage("Item is no longer available for purchase.");
            return false;
        } //if item is unavaiable

        if (!item.getName().equalsIgnoreCase("Noppo Bread")) {
            item.setAvailable(false);
        } //every item except Noppo Bread should be unavailable after the first purchase

        boolean success = player.buyItem(item);
        if (success) {
            this.refreshGold();
            this.refreshMessage("Successfuly bought " + item.getName() + "!");
            this.itemDisplay(this.pageIndex, game.GameGUI.getItems());
        }
        return success;
    }

    private void initializeIcons() {
        ImageIcon tearsOfAngel = new ImageIcon("sprites/TEARS_OF_A_FALLEN_ANGEL.png");
        ImageIcon noppoBread = new ImageIcon("sprites/NOPPO_BREAD.png");
        ImageIcon shovelUpg = new ImageIcon("sprites/SHOVEL_UPGRADE.png");
        ImageIcon batTamer = new ImageIcon("sprites/BAT_TAMER.png");
        ImageIcon airShoes = new ImageIcon("sprites/AIR_SHOES.png");
        ImageIcon mikanMochi = new ImageIcon("sprites/MIKAN_MOCHI.png");
        ImageIcon stewshine = new ImageIcon("sprites/STEWSHINE.png");
        ImageIcon kurosawaMatcha = new ImageIcon("sprites/KUROSAWA_MATCHA.png");
        ImageIcon chocoMint = new ImageIcon("sprites/CHOCO_MINT_ICE_CREAM.png");

        //initialize the icons attribute
        this.icons = new ImageIcon[] {
                tearsOfAngel,
                noppoBread,
                shovelUpg,
                batTamer,
                airShoes,
                stewshine,
                mikanMochi,
                kurosawaMatcha,
                chocoMint
        };
    }

    private void btnReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturnActionPerformed
        frame.showCard("MENU");
    }//GEN-LAST:event_btnReturnActionPerformed

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
