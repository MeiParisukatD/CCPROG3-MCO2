/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import game.GameGUI;
import Character_Classes.*;
import Dungeon_Classes.Floor;
import java.awt.GridLayout;
import Dungeon_Classes.Tile;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 *
 * @author rhian
 */
public class GamePanel extends javax.swing.JPanel {

    private MainFrame frame;
    
    /**
     * Creates new form GamePanel
     */
    public GamePanel(MainFrame frame) {
        initComponents();
        mapPanel.setLayout(new GridLayout(12, 55));
        this.frame = frame;
        mapPanel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        setupKeyBindings();
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
        lblFloor.setText("Floor: " + floor.getFloorNum());
    }
    
    public void refreshMap() {
        PlayableChar player = GameGUI.getYohane();
        
        mapPanel.removeAll();
        
        Floor floor = GameGUI.getCurrentFloor();
        if (floor == null) return;

        Tile[][] map = floor.getMap();
        mapPanel.setLayout(new GridLayout(map.length, map[0].length));

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                char symbol= map[i][j].getSymbol();
                
                for (EnemyChar enemy : floor.getEnemies()) {
                    if (enemy.getX() == i && enemy.getY() == j) {
                        symbol = (enemy instanceof Siren) ? 'S' : 'b';
                        break;
                    }
                }
                
                if (player != null && player.getX() == i && player.getY() == j) {
                    symbol = 'Y';
                }
            
                JLabel tile = new JLabel(String.valueOf(symbol), SwingConstants.CENTER);
                if (symbol == 'Y')
                    tile.setForeground(Color.BLUE);
                else if (symbol == 'b' || symbol == 'B' || symbol == 'S')
                    tile.setForeground(Color.RED);
                else if (symbol == 'E')
                    tile.setForeground(Color.GREEN);
                
                mapPanel.add(tile);
            }
        }

        mapPanel.revalidate();
        mapPanel.repaint();
    }
    
    /**
     * Executes one turn sequence whenever the player performs an action.
     */
    private void processTurn(char input) {

        boolean dungeonCleared = GameGUI.processTurn(input);

        PlayableChar player = GameGUI.getYohane();

        // --- Game Over ---
        if (player != null && player.charDeath()) {
            String killer = player.getCauseOfDeath();

            // Reads/records the death BEFORE calling handleGameOver(), which
            // replaces Yohane with a fresh PlayableChar via initialize().
            GameGUI.handleGameOver();

            JOptionPane.showMessageDialog(this,
                    "You Died!\nKilled by: " + killer,
                    "Game Over",
                    JOptionPane.ERROR_MESSAGE);

            frame.showCard("MENU");
            return;
        }

        refreshStats();
        refreshMap();

        // --- Dungeon Cleared ---
        if (dungeonCleared) {
            Dungeon_Classes.Dungeon dungeon = GameGUI.getCurrentDungeon();
            String memberName = dungeon.getMember().getName();

            StringBuilder msg = new StringBuilder();
            msg.append("Dungeon Cleared!\n");
            msg.append(dungeon.getName()).append(" Completed!\n");
            msg.append(memberName).append(" rescued!");

            if (memberName.equalsIgnoreCase("Hanamaru Kunikida")) {
                msg.append("\n\nUnlocked: Hanamaru's Store Now Available!");
            }

            JOptionPane.showMessageDialog(this,
                    msg.toString(),
                    "Dungeon Cleared!",
                    JOptionPane.INFORMATION_MESSAGE);

            frame.getGameMenuPanel().refresh();
            frame.showCard("GAMEMENU");
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
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        mapPanel = new javax.swing.JPanel();
        lblTurn = new javax.swing.JLabel();

        lblHP.setText("HP: 20/20");

        lblGold.setText("Gold: 67 GP");

        lblFloor.setText("Floor 1");

        txtLog.setEditable(false);
        txtLog.setColumns(20);
        txtLog.setLineWrap(true);
        txtLog.setRows(5);
        txtLog.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtLog);

        mapPanel.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N

        javax.swing.GroupLayout mapPanelLayout = new javax.swing.GroupLayout(mapPanel);
        mapPanel.setLayout(mapPanelLayout);
        mapPanelLayout.setHorizontalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 428, Short.MAX_VALUE)
        );
        mapPanelLayout.setVerticalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 198, Short.MAX_VALUE)
        );

        lblTurn.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFloor)
                            .addComponent(lblHP))
                        .addGap(114, 114, 114))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblGold)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTurn)
                        .addGap(37, 37, 37)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(mapPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(251, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblHP)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblGold)
                            .addComponent(lblTurn))
                        .addGap(26, 26, 26)
                        .addComponent(lblFloor))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(259, Short.MAX_VALUE)
                .addComponent(mapPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFloor;
    private javax.swing.JLabel lblGold;
    private javax.swing.JLabel lblHP;
    private javax.swing.JLabel lblTurn;
    private javax.swing.JPanel mapPanel;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
