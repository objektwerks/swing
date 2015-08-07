package com.ndr.app.stock.screener.button;

import com.ndr.app.stock.screener.resource.ResourceManager;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.annotation.PostConstruct;
import javax.swing.JToggleButton;

import org.jdesktop.swingx.JXTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class TreeExpandCollapseToggleButton extends JToggleButton {
    @Autowired ResourceManager resourceManager;
    private JXTree tree;

    public void setTree(JXTree tree) {
        this.tree = tree;
    }

    @Override
    public void setSelected(boolean isSelected) {
        super.setSelected(isSelected);
        expandOrCollapseTree(isSelected);
    }

    @PostConstruct
    protected void build() {
        setIcon(resourceManager.getImageIcon("expand.icon"));
        setToolTipText(resourceManager.getString("expand"));
        ButtonSizer.instance.setDefaultToolBarSize(this);
        addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                expandOrCollapseTree(isSelected());
            }
        });
    }

    private void expandOrCollapseTree(boolean isSelected) {
        if (tree != null) {
            if (isSelected) {
                setIcon(resourceManager.getImageIcon("collapse.icon"));
                setToolTipText(resourceManager.getString("collapse"));
                tree.expandAll();
            } else {
                setIcon(resourceManager.getImageIcon("expand.icon"));
                setToolTipText(resourceManager.getString("expand"));
                tree.collapseAll();
            }
        }
    }
}