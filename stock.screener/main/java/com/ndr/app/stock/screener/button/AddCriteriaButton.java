package com.ndr.app.stock.screener.button;

import com.ndr.app.stock.screener.action.AddCriteriaAction;

import javax.annotation.PostConstruct;
import javax.swing.JButton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class AddCriteriaButton extends JButton {
    private static final long serialVersionUID = 57504000272730706L;
    
	@Autowired private AddCriteriaAction addCriteriaAction;

    @PostConstruct
    protected void build() {
        setAction(addCriteriaAction);
        setFocusPainted(false);
        ButtonSizer.instance.setDefaultToolBarSize(this);
        setEnabled(false);
    }
}