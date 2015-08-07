package com.ndr.app.stock.screener.button;

import com.ndr.app.stock.screener.action.AddColumnAction;

import javax.annotation.PostConstruct;
import javax.swing.JButton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class AddColumnButton extends JButton {
    private static final long serialVersionUID = -6475965012319622634L;
    
	@Autowired private AddColumnAction addColumnAction;

    @PostConstruct
    protected void build() {
        setAction(addColumnAction);
        setFocusPainted(false);
        ButtonSizer.instance.setDefaultToolBarSize(this);
        setEnabled(false);
    }
}