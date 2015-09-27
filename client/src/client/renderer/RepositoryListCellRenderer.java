package client.renderer;

import domain.Repository;
import client.resource.ResourceManager;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

public final class RepositoryListCellRenderer extends DefaultListCellRenderer {
    private ImageIcon icon;

    public RepositoryListCellRenderer() {
    }

    @Override
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int selectedIndex,
                                                  boolean isSelected,
                                                  boolean hasFocus) {
        Repository repository = (Repository) list.getModel().getElementAt(selectedIndex);
        setText(repository.getName());
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
        this.icon = resourceManager.getImageIcon("icon.repository");
    }
}