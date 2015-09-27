package client.frame;

import client.resource.ResourceManager;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public final class MenuBar extends JMenuBar {
    private ResourceManager resourceManager;
    private Action exitAction;

    public MenuBar() {
        super();
    }

    public void build() {
        JMenu menu = new JMenu(resourceManager.getString("menu.bar.task"));
        menu.add(new JMenuItem(exitAction));
        add(menu);
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setExitAction(Action exitAction) {
        this.exitAction = exitAction;
    }
}