package com.ndr.app.stock.screener.button;

import com.ndr.app.stock.screener.action.RemoveColumnAction;

import javax.annotation.PostConstruct;
import javax.swing.JButton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class RemoveColumnButton extends JButton {
    private static final long serialVersionUID = 7048093462158551229L;
    
	@Autowired private RemoveColumnAction removeColumnAction;

    @PostConstruct
    protected void build() {
        setAction(removeColumnAction);
        ButtonSizer.instance.setDefaultToolBarSize(this);
        setEnabled(false);
        setFocusPainted(false);
    }
}