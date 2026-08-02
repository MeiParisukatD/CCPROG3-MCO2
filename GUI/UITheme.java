/*
 * Simple shared color theme for the app: pink backgrounds, dark pink
 * buttons, purple text. Call UITheme.apply(this) once at the end of a
 * panel's constructor (after initComponents()) and it will recursively
 * re-color every child component already on the panel.
 */
package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class UITheme {

    public static final Color PINK = Color.decode("#ffe6f4");
    public static final Color DARK_PINK = Color.decode("#ffd5f2");
    public static final Color PURPLE = Color.decode("#a23e8f");

    private UITheme() {
        // static helper only
    }

    /** Recursively themes root and all of its current children. */
    public static void apply(Container root) {
        if (root instanceof JPanel) {
            root.setBackground(PINK);
        }
        for (Component child : root.getComponents()) {
            style(child);
            if (child instanceof Container) {
                apply((Container) child);
            }
        }
    }

    private static void style(Component c) {
        if (c instanceof JButton) {
            JButton b = (JButton) c;
            b.setOpaque(true);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setBackground(DARK_PINK);
            b.setForeground(PURPLE);
        } else if (c instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) c;
            sp.setBackground(PINK);
            if (sp.getViewport() != null) {
                sp.getViewport().setBackground(PINK);
            }
        } else if (c instanceof JList) {
            c.setBackground(PINK);
            c.setForeground(PURPLE);
        } else if (c instanceof JLabel) {
            c.setForeground(PURPLE);
        } else if (c instanceof JPanel) {
            c.setBackground(PINK);
        }
    }
}
