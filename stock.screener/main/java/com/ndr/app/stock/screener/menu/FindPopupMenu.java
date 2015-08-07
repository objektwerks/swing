package com.ndr.app.stock.screener.menu;

import com.ndr.app.stock.screener.resource.ResourceManager;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public final class FindPopupMenu extends JPopupMenu {
    private static final long serialVersionUID = -5713538525242471255L;

	public FindPopupMenu(ResourceManager resourceManager, Action action) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setIcon(resourceManager.getImageIcon("search.icon"));
        menuItem.setText(resourceManager.getString("search"));
        add(menuItem);
    }
}