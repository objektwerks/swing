package com.ndr.app.stock.screener.button;

import com.ndr.app.stock.screener.action.DemoteCriteriaAction;

import javax.annotation.PostConstruct;
import javax.swing.JButton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class DemoteCriteriaButton extends JButton {
    private static final long serialVersionUID = -5235927796015694355L;
    
	@Autowired private DemoteCriteriaAction demoteCriteriaAction;

    @PostConstruct
    protected void build() {
        setAction(demoteCriteriaAction);
        setFocusPainted(false);
        ButtonSizer.instance.setDefaultToolBarSize(this);
        setEnabled(false);
    }
}