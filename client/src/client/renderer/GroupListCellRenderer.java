package client.renderer;

import domain.Group;
import client.resource.ResourceManager;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

public final class GroupListCellRenderer extends DefaultListCellRenderer {
    private ImageIcon icon;

    public GroupListCellRenderer() {
    }

    @Override
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int selectedIndex,
                                                  boolean isSelected,
                                                  boolean hasFocus) {
        Group group = (Group) list.getModel().getElementAt(selectedIndex);
        setText(group.getName());
        setIcon(icon);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.icon = resourceManager.getImageIcon("icon.group");
    }
}