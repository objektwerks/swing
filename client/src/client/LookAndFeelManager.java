package client;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.UIDefaults;

public final class LookAndFeelManager {
    private static final LookAndFeelManager instance = new LookAndFeelManager();
    private static final Color navy = new Color(25, 25, 112);

    private LookAndFeelManager() {
    }

    public static LookAndFeelManager getInstance() {
        return instance;
    }

    public void setDefaultLookAndFeel() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIDefaults uiDefaults = UIManager.getDefaults();
        Object [] defaults = { "Button.foreground", navy,
                               "ComboBox.foreground", navy,
                               "Label.foreground", navy,
                               "List.foreground", navy,
                               "MenuBar.foreground", navy,
                               "Menu.foreground", navy,
                               "MenuItem.foreground", navy,
                               "TabbedPane.foreground", navy,
                               "TextField.foreground", navy,
                               "TextField.inactiveForeground", navy,
                               "TitledBorder.titleColor", navy,
                               "ToolTip.background", navy,
                               "ToolTip.foreground", Color.white
                             };
        uiDefaults.putDefaults(defaults);
    }
}