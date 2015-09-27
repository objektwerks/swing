package client.renderer;

import domain.Domain;
import client.resource.ResourceManager;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

public final class DomainListCellRenderer extends DefaultListCellRenderer {
    private ImageIcon icon;

    public DomainListCellRenderer() {
    }

    @Override
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int selectedIndex,
                                                  boolean isSelected,
                                                  boolean hasFocus) {
        Domain domain = (Domain) list.getModel().getElementAt(selectedIndex);
        setText(domain.getName());
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
        this.icon = resourceManager.getImageIcon("icon.domain");
    }
}