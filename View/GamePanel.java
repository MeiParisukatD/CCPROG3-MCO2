/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import Controller.GameGUI;
import Character_Classes.*;
import Dungeon_Classes.*;
import java.awt.GridLayout;
import Dungeon_Classes.Tile;
import Item_Classes.Item;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.Collections;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 *
 * @author rhian
 */
public class GamePanel extends javax.swing.JPanel {

    private MainFrame frame;
    private static final int CELL_SIZE = 18;
    private final java.util.Map<Character, ImageIcon> tileIcons = new java.util.HashMap<>();
    
    /**
     * Creates new form GamePanel
     */
    public GamePanel(MainFrame frame) {
        initComponents();
        UITheme.apply(this);
        loadIcons();
        this.frame = frame;
        mapPanel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        setupKeyBindings();
    }
    private void loadIcons() {
        tileIcons.put('.', loadIcon("/resources/floor.png"));
        tileIcons.put('*', loadIcon("/resources/border.png"));
        tileIcons.put('v', loadIcon("/resources/wall.png"));
        tileIcons.put('x', loadIcon("/resources/spike.png"));
        tileIcons.put('w', loadIcon("/resources/water.png"));
        tileIcons.put('h', loadIcon("/resources/heat.png"));
        tileIcons.put('T', loadIcon("/resources/treasure.png"));
        tileIcons.put('E', loadIcon("/resources/exit.png"));
        tileIcons.put('g', loadIcon("/resources/gold.png"));
        tileIcons.put('0', loadIcon("/resources/switch.png"));
        tileIcons.put('Y', loadIcon("/resources/yohane.png"));
        tileIcons.put('L', loadIcon("/resources/lailaps.png"));
        tileIcons.put('b', loadIcon("/resources/bat.png"));
        tileIcons.put('B', loadIcon("/resources/bat_attack.png")); // the capital-B fix from before
        tileIcons.put('S', loadIcon("/resources/siren.png"));
    }

    private ImageIcon loadIcon(String path) {
        java.net.URL url = getClass().getResource(path);
        if (url == null) {
            System.out.println("[!] Missing image: " + path);
            return null;
        }
        Image scaled = new ImageIcon(url).getImage()
                .getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
    
    public void refreshStats() {

        PlayableChar player = GameGUI.getYohane();

        if (player == null)
            return;

        lblHP.setText(String.format("HP: %.1f / %.1f",
                player.getHealth(),
                player.getMaxHealth()));

        lblGold.setText("Gold: " + player.getGoldOwned() + " GP");

        lblTurn.setText("Turn: " + player.getTurnCount());

        Floor floor = GameGUI.getCurrentFloor();
        boolean isBossFight = floor instanceof BossFloor;

        lblFloor.setText(isBossFight
                ? "Final Battle: Siren of the Mirror World!"
                : "Floor: " + floor.getFloorNum());

        Item curItem = player.getCurItem();
        if (curItem != null) {
            int qty = Collections.frequency(player.getInventory(), curItem);
            String text = "Item on hand: " + curItem.getName();
            if (qty > 1) {
                text += " (" + qty + ")";
            }
            lblItemOnHand.setText(text);
        } else {
            lblItemOnHand.setText("Item on hand: N/A");
        }

        if (isBossFight) {
            PlayableChar lailaps = GameGUI.getLailaps();
            lblLailapsHP.setText(String.format("Lailaps HP: %.1f / %.1f",
                    lailaps.getHealth(), lailaps.getMaxHealth()));
            lblLailapsHP.setVisible(true);
        } else {
            lblLailapsHP.setVisible(false);
        }
    }
    
    public void refreshMap() {
        PlayableChar player = GameGUI.getYohane();

        mapPanel.removeAll();
        
        Floor floor = GameGUI.getCurrentFloor();
        if (floor == null) return;

        boolean isBossFight = floor instanceof BossFloor;
        PlayableChar lailaps = isBossFight ? GameGUI.getLailaps() : null;

        Tile[][] map = floor.getMap();
        mapPanel.setLayout(new GridLayout(map.length, map[0].length));
        mapPanel.setPreferredSize(new java.awt.Dimension(map[0].length * CELL_SIZE, map.length * CELL_SIZE));

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                char symbol= map[i][j].getSymbol();
                
                for (EnemyChar enemy : floor.getEnemies()) {
                    if (enemy.getX() == i && enemy.getY() == j) {
                        if (enemy instanceof Siren) {
                            symbol = 'S';
                        } else {
                            symbol = 'b';
                            if (player != null
                                    && enemy.detectPlayer(map, player)
                                    && player.getTurnCount() % enemy.getTurnsPerMove() == 0) {
                                symbol = 'B';
                            }
                        }
                        break;
                    }
                }

                if (lailaps != null && lailaps.getX() == i && lailaps.getY() == j) {
                    symbol = 'L';
                }

                if (player != null && player.getX() == i && player.getY() == j) {
                    symbol = 'Y';
                }
            
                JLabel tile = new JLabel();
                tile.setPreferredSize(new java.awt.Dimension(CELL_SIZE, CELL_SIZE));
                tile.setHorizontalAlignment(SwingConstants.CENTER);
                ImageIcon icon = tileIcons.get(symbol);

                if (icon != null) {
                    tile.setIcon(icon);
                } else {
                    // fallback until you've made an image for this symbol
                    tile.setOpaque(true);
                    tile.setText(String.valueOf(symbol));
                    tile.setBackground(fallbackColor(symbol));
                }

                mapPanel.add(tile);
            }
        }

        mapPanel.revalidate();
        mapPanel.repaint();
    }
    
    private Color fallbackColor(char symbol) {
        switch (symbol) {
            case '*': return Color.DARK_GRAY;
            case 'v': return new Color(120, 80, 40);
            case 'x': return Color.ORANGE;
            case 'w': return Color.CYAN;
            case 'h': return Color.RED.darker();
            case 'T': return Color.MAGENTA;
            case 'E': return Color.GREEN;
            case 'g': return Color.YELLOW;
            case 'L': return Color.BLUE;
            case 'b': case 'B': case 'S': return Color.RED;
            default: return Color.WHITE;
        }
    }
    
    /**
     * Executes one turn sequence whenever the player performs an action.
     */
    private void processTurn(char input) {

        boolean dungeonCleared = GameGUI.processTurn(input);

        PlayableChar player = GameGUI.getYohane();
        PlayableChar lailaps = GameGUI.getLailaps();
        boolean isBossFight = GameGUI.getCurrentFloor() instanceof BossFloor;

        // --- Game Over --- (Yohane's death, or Lailaps' during the boss fight)
        boolean yohaneDied = player != null && player.charDeath();
        boolean lailapsDied = isBossFight && lailaps != null && lailaps.charDeath();

        if (yohaneDied || lailapsDied) {
            String dead = lailapsDied ? "Lailaps" : "You";
            String killer = lailapsDied ? lailaps.getCauseOfDeath() : player.getCauseOfDeath();

            // Reads/records the death BEFORE calling handleGameOver(), which
            // replaces Yohane with a fresh PlayableChar via initialize().
            GameGUI.handleGameOver();

            JOptionPane.showMessageDialog(this,
                    dead + " Died!\nKilled by: " + killer,
                    "Game Over",
                    JOptionPane.ERROR_MESSAGE);

            frame.showCard("MENU");
            return;
        }

        refreshStats();
        refreshMap();

        // --- Dungeon Cleared (or Siren defeated) ---
        if (dungeonCleared) {
            Dungeon dungeon = GameGUI.getCurrentDungeon();
            Dungeon[] allDungeons = GameGUI.getDungeons();
            boolean bossVictory = (dungeon == allDungeons[allDungeons.length - 1]);

            StringBuilder msg = new StringBuilder();

            if (bossVictory) {
                msg.append("The Siren of the Mirror World has been defeated!\n");
                msg.append(dungeon.getName()).append(" Cleared!\n");
                msg.append("You have completed the game!");
            } else {
                String memberName = dungeon.getMember().getName();
                msg.append("Dungeon Cleared!\n");
                msg.append(dungeon.getName()).append(" Completed!\n");
                msg.append(memberName).append(" rescued!");

                if (memberName.equalsIgnoreCase("Hanamaru Kunikida")) {
                    msg.append("\n\nUnlocked: Hanamaru's Store Now Available!");
                }
            }

            JOptionPane.showMessageDialog(this,
                    msg.toString(),
                    bossVictory ? "Victory!" : "Dungeon Cleared!",
                    JOptionPane.INFORMATION_MESSAGE);

            if (bossVictory) {
                frame.showCard("MENU");
            } else {
                frame.getGameMenuPanel().refresh();
                frame.showCard("GAMEMENU");
            }
        }
    }
    
    private void setupKeyBindings() {
        javax.swing.InputMap im = this.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        javax.swing.ActionMap am = this.getActionMap();

        im.put(javax.swing.KeyStroke.getKeyStroke('w'), "moveUp");
        im.put(javax.swing.KeyStroke.getKeyStroke('a'), "moveLeft");
        im.put(javax.swing.KeyStroke.getKeyStroke('s'), "moveDown");
        im.put(javax.swing.KeyStroke.getKeyStroke('d'), "moveRight");
        im.put(javax.swing.KeyStroke.getKeyStroke(' '), "useItem");
        im.put(javax.swing.KeyStroke.getKeyStroke('['), "prevItem");
        im.put(javax.swing.KeyStroke.getKeyStroke(']'), "nextItem");
        im.put(javax.swing.KeyStroke.getKeyStroke('x'), "stayStill");

        am.put("moveUp", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { processTurn('w'); }
        });
        am.put("moveLeft", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { processTurn('a'); }
        });
        am.put("moveDown", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { processTurn('s'); }
        });
        am.put("moveRight", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { processTurn('d'); }
        });
        am.put("useItem", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { processTurn(' '); }
        });
        am.put("prevItem", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { processTurn('['); }
        });
        am.put("nextItem", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { processTurn(']'); }
        });
        am.put("stayStill", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { processTurn('x'); }
        });
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblHP = new javax.swing.JLabel();
        lblGold = new javax.swing.JLabel();
        lblFloor = new javax.swing.JLabel();
        mapPanel = new javax.swing.JPanel();
        lblTurn = new javax.swing.JLabel();
        lblItemOnHand = new javax.swing.JLabel();
        lblLailapsHP = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1050, 420));

        lblHP.setText("HP: 20/20");

        lblGold.setText("Gold: 67 GP");

        lblFloor.setText("Floor 1");

        mapPanel.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        mapPanel.setPreferredSize(new java.awt.Dimension(1024, 700));

        javax.swing.GroupLayout mapPanelLayout = new javax.swing.GroupLayout(mapPanel);
        mapPanel.setLayout(mapPanelLayout);
        mapPanelLayout.setHorizontalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1024, Short.MAX_VALUE)
        );
        mapPanelLayout.setVerticalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );

        lblTurn.setText("jLabel1");

        lblItemOnHand.setText("jLabel1");

        lblLailapsHP.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblItemOnHand)
                            .addComponent(lblFloor)
                            .addComponent(mapPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGold)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblHP)
                                .addGap(52, 52, 52)
                                .addComponent(lblLailapsHP)
                                .addGap(55, 55, 55)
                                .addComponent(lblTurn)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHP)
                    .addComponent(lblTurn)
                    .addComponent(lblLailapsHP))
                .addGap(18, 18, 18)
                .addComponent(lblGold)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblItemOnHand)
                .addGap(20, 20, 20)
                .addComponent(lblFloor)
                .addGap(18, 18, 18)
                .addComponent(mapPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblFloor;
    private javax.swing.JLabel lblGold;
    private javax.swing.JLabel lblHP;
    private javax.swing.JLabel lblItemOnHand;
    private javax.swing.JLabel lblLailapsHP;
    private javax.swing.JLabel lblTurn;
    private javax.swing.JPanel mapPanel;
    // End of variables declaration//GEN-END:variables
}
