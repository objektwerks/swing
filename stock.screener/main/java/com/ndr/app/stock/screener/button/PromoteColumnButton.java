package com.ndr.app.stock.screener.button;

import com.ndr.app.stock.screener.action.PromoteColumnAction;

import javax.annotation.PostConstruct;
import javax.swing.JButton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class PromoteColumnButton extends JButton {
    private static final long serialVersionUID = 6295816119242292067L;
    
	@Autowired private PromoteColumnAction promoteColumnAction;

    @PostConstruct
    protected void build() {
        setAction(promoteColumnAction);
        ButtonSizer.instance.setDefaultToolBarSize(this);
        setEnabled(false);
        setFocusPainted(false);
    }
}