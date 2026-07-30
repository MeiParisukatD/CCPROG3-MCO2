// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package GUI;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.GroupLayout.Alignment;

public class GameMenuGUI extends JFrame {
   private static final Logger logger = Logger.getLogger(GameMenuGUI.class.getName());

   public GameMenuGUI() {
      this.initComponents();
   }

   private void initComponents() {
      this.setDefaultCloseOperation(3);
      GroupLayout var1 = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(var1);
      var1.setHorizontalGroup(var1.createParallelGroup(Alignment.LEADING).addGap(0, 400, 32767));
      var1.setVerticalGroup(var1.createParallelGroup(Alignment.LEADING).addGap(0, 300, 32767));
      this.pack();
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

      EventQueue.invokeLater(() -> (new GameMenuGUI()).setVisible(true));
   }
}
