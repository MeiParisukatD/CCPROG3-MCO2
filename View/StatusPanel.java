/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import Character_Classes.*;
import View.MainFrame;
import Controller.*;

/**
 *
 * @author rhian
 */
public class StatusPanel extends JPanel {
    private MainFrame frame;
    private JPanel topPanel;
    private JPanel npcPanel;
    private JPanel bottomPanel;
    private JButton btnReturn;

    private Color pink = Color.decode("#ffe6f4");
    private Color darkPink = Color.decode("#ffd5f2");
    private Color purple = Color.decode("#a23e8f");

    /**
     * Creates new form StatusPanel
     */
    public StatusPanel(MainFrame frame) {
        //initComponents();
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(pink);

        //creates panels
        this.topPanel = new JPanel();
        this.topPanel.setBackground(pink);
        JLabel header = new JLabel("STATUS");
        header.setFont(new Font("SansSerif", Font.PLAIN, 20));
        header.setForeground(purple);
        this.topPanel.add(header);

        this.npcPanel = new JPanel();
        this.npcPanel.setBackground(pink);
        this.npcPanel.setLayout(new GridLayout(0, 1, 5, 5)); // row count set dynamically in refresh()

        this.bottomPanel = new JPanel();
        this.bottomPanel.setBackground(pink);
        this.bottomPanel.setLayout(new GridLayout(4, 1));

        this.btnReturn = new JButton();
        btnReturn.setText("Return");
        btnReturn.addActionListener(this::btnReturnActionPerformed);

        //add to status panel
        add(topPanel, BorderLayout.NORTH);
        add(npcPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refresh() {
        npcPanel.removeAll();
        bottomPanel.removeAll();

        NPChar[] npcs = Controller.GameGUI.getNPCs();
        int goldSpent = Controller.GameGUI.getGoldSpent();
        int gameOvers = Controller.GameGUI.getGameOvers();
        int sirenDefeats = Controller.GameGUI.getSirenDefeated();

        this.NPCLabels(npcs);
        this.bottomLabels(goldSpent, gameOvers, sirenDefeats);
        bottomPanel.add(btnReturn);

        npcPanel.revalidate();
        npcPanel.repaint();
        bottomPanel.revalidate();
        bottomPanel.repaint();
    }

    private void bottomLabels(int goldSpent, int gameOvers, int sirenDefeats) {
        //initializes JLabels (unlike NPCLabels, these will not have borders)

        JPanel siren = new JPanel(new BorderLayout());
        siren.setBackground(pink);
        //text
        JLabel left1 = new JLabel("Times Siren was defeated");
        JLabel right1 = new JLabel(sirenDefeats + " times");
        left1.setFont(new Font("SansSerif", Font.PLAIN, 14));
        right1.setFont(new Font("SansSerif", Font.PLAIN, 14));
        left1.setForeground(purple);
        right1.setForeground(purple);
        //add to respective panels
        siren.add(left1, BorderLayout.WEST);
        siren.add(right1, BorderLayout.EAST);
        this.bottomPanel.add(siren);

        JPanel gameOver = new JPanel(new BorderLayout());
        gameOver.setBackground(pink);
        //text
        JLabel left2 = new JLabel("No. of game overs");
        JLabel right2 = new JLabel(gameOvers + " times");
        left2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        right2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        left2.setForeground(purple);
        right2.setForeground(purple);
        //add to respective panels
        gameOver.add(left2, BorderLayout.WEST);
        gameOver.add(right2, BorderLayout.EAST);
        this.bottomPanel.add(gameOver);

        JPanel gold = new JPanel(new BorderLayout());
        gold.setBackground(pink);
        //text
        JLabel left3 = new JLabel("Total gold spent");
        JLabel right3 = new JLabel(goldSpent + " gp");
        left3.setFont(new Font("SansSerif", Font.PLAIN, 14));
        right3.setFont(new Font("SansSerif", Font.PLAIN, 14));
        left3.setForeground(purple);
        right3.setForeground(purple);
        //add to respective panels
        gold.add(left3, BorderLayout.WEST);
        gold.add(right3, BorderLayout.EAST);
        this.bottomPanel.add(gold);
    }
    
    /**
     * Builds one labeled row per NPC showing their rescue count, sizing the
     * grid's row count to match the roster so no empty rows are left over
     * (and none are cut off) regardless of how many NPCs exist.
     *
     * @param npcs the roster of rescuable idol NPCs to display
     */
    private void NPCLabels(NPChar[] npcs) {
        if (npcs == null) {
            return; 
        }

        int size = npcs.length;
        this.npcPanel.setLayout(new GridLayout(size, 1, 5, 5));

        for (int i = 0; i < size; i++) {
            // creates new box for each NPC display
            JPanel box = new JPanel(new BorderLayout());
            box.setBackground(darkPink);
            box.setPreferredSize(new Dimension(1000, 20));

            // sets up label text
            JLabel name = new JLabel("Time " + npcs[i].getName() + " was saved");
            JLabel stat = new JLabel(npcs[i].getTimesSaved() + " times");
            name.setFont(new Font("SansSerif", Font.PLAIN, 14));
            stat.setFont(new Font("SansSerif", Font.PLAIN, 14));
            name.setForeground(purple);
            stat.setForeground(purple);

            box.add(name, BorderLayout.WEST);
            box.add(stat, BorderLayout.EAST);

            this.npcPanel.add(box);
        }
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
}
