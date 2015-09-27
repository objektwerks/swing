package client.renderer;

import domain.Index;
import client.resource.ResourceManager;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

public final class IndexComboBoxCellRenderer extends DefaultListCellRenderer {
    private ImageIcon icon;

    public IndexComboBoxCellRenderer() {
    }

    @Override
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int selectedIndex,
                                                  boolean isSelected,
                                                  boolean hasFocus) {
        Index index = (Index) value;
        if (null != index) {
            setText(index.getName());
            setIcon(icon);
        }
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
        this.icon = resourceManager.getImageIcon("icon.index");
    }
}