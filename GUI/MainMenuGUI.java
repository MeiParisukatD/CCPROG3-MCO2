// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package GUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class MainMenuGUI extends JFrame {
   private static final Logger logger = Logger.getLogger(MainMenuGUI.class.getName());
   private JButton jButton1;
   private JButton jButton2;
   private JButton jButton3;
   private JLabel jLabel1;
   private JLabel jLabel2;

   public MainMenuGUI() {
      this.initComponents();
   }

   private void initComponents() {
      this.jLabel1 = new JLabel();
      this.jLabel2 = new JLabel();
      this.jButton1 = new JButton();
      this.jButton2 = new JButton();
      this.jButton3 = new JButton();
      this.setDefaultCloseOperation(3);
      this.jLabel1.setText(" Yohane The Parhelion!");
      this.jLabel2.setText("The Siren in the Mirror World! ");
      this.jButton1.setText("New Game");
      this.jButton1.addActionListener(this::jButton1ActionPerformed);
      this.jButton2.setText("Status");
      this.jButton2.addActionListener(this::jButton2ActionPerformed);
      this.jButton3.setText("Quit");
      this.jButton3.addActionListener(this::jButton3ActionPerformed);
      GroupLayout var1 = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(var1);
      var1.setHorizontalGroup(var1.createParallelGroup(Alignment.LEADING).addGroup(var1.createSequentialGroup().addGap(120, 120, 120).addGroup(var1.createParallelGroup(Alignment.CENTER).addComponent(this.jLabel1).addComponent(this.jLabel2).addGroup(var1.createParallelGroup(Alignment.LEADING, false).addComponent(this.jButton3, -1, -1, 32767).addComponent(this.jButton2, -1, -1, 32767).addComponent(this.jButton1, -1, -1, 32767))).addContainerGap(120, 32767)));
      var1.setVerticalGroup(var1.createParallelGroup(Alignment.LEADING).addGroup(var1.createSequentialGroup().addGap(61, 61, 61).addComponent(this.jLabel1).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.jLabel2).addGap(42, 42, 42).addComponent(this.jButton1).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jButton2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jButton3).addContainerGap(72, 32767)));
      this.pack();
   }

   private void jButton2ActionPerformed(ActionEvent var1) {
      StatusGUI var2 = new StatusGUI();
      var2.setVisible(true);
      this.dispose();
   }

   private void jButton3ActionPerformed(ActionEvent var1) {
      System.exit(0);
   }

   private void jButton1ActionPerformed(ActionEvent var1) {
      GameMenuGUI var2 = new GameMenuGUI();
      var2.setVisible(true);
      this.dispose();
   }

   public static void main(String[] var0) {
      try {
         for(UIManager.LookAndFeelInfo var4 : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(var4.getName())) {
               UIManager.setLookAndFeel(var4.getClassName());
               break;
            }
         }
      } catch (UnsupportedLookAndFeelException | ReflectiveOperationException var5) {
         logger.log(Level.SEVERE, (String)null, var5);
      }

      EventQueue.invokeLater(() -> (new MainMenuGUI()).setVisible(true));
   }
}
