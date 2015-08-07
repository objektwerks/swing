package com.ndr.app.stock.screener.button;

import com.ndr.app.stock.screener.action.RemoveCriteriaAction;

import javax.annotation.PostConstruct;
import javax.swing.JButton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class RemoveCriteriaButton extends JButton {
    private static final long serialVersionUID = 4789786721813808834L;
    
	@Autowired private RemoveCriteriaAction removeCriteriaAction;

    @PostConstruct
    protected void build() {
        setAction(removeCriteriaAction);
        ButtonSizer.instance.setDefaultToolBarSize(this);
        setEnabled(false);
        setFocusPainted(false);
    }
}