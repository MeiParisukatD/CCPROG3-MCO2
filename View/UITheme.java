package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Shared color theme for the app's Swing panels: pink backgrounds, dark
 * pink buttons, and purple text. Call {@link #apply(Container)} once at
 * the end of a panel's constructor, after {@code initComponents()}, to
 * recursively re-color every child component already on the panel.
 *
 * @author Katigbak and Porciuncula
 * @version 2.0
 */
public final class UITheme {

    /** Background fill used for panels, lists, and scroll panes. */
    public static final Color PINK = Color.decode("#ffe6f4");
    /** Background fill used for buttons. */   
    public static final Color DARK_PINK = Color.decode("#ffd5f2");
    /** Text color used for labels and buttons. */   
    public static final Color PURPLE = Color.decode("#a23e8f");

    /**
     * Private constructor - this class is a static utility and is never instantiated.
     */   
    private UITheme() {
        // static helper only
    }

    /**
     * Recursively themes {@code root} and all of its current children,
     * setting panel/list/scroll-pane backgrounds to {@link #PINK}, button
     * backgrounds to {@link #DARK_PINK}, and text colors to {@link #PURPLE}.
     * Only affects components already present at the time of the call -
     * if children are added later, call this again.
     *
     * @param root the top-level container to theme, typically {@code this}
     *             from within a panel's constructor
     */
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

    /**
     * Applies the appropriate theme colors to a single component based on
     * its concrete Swing type.
     *
     * @param c the component to style
     */   
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
