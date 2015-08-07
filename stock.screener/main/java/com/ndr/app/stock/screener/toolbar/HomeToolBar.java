package com.ndr.app.stock.screener.toolbar;

import com.ndr.app.stock.screener.action.ExitAction;
import com.ndr.app.stock.screener.action.InfoAction;
import com.ndr.app.stock.screener.action.TransitionToTimeSeriesPanelAction;
import com.ndr.app.stock.screener.button.ButtonSizer;

import javax.annotation.PostConstruct;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JToolBar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class HomeToolBar extends JToolBar {
    private static final long serialVersionUID = -3373315987982966553L;

    @Autowired private TransitionToTimeSeriesPanelAction transitionToTimeSeriesPanelAction;
    @Autowired private InfoAction infoAction;
    @Autowired private ExitAction exitAction;

    @PostConstruct
    protected void build() {
        setFloatable(false);
        add(buildOpenButton());
        add(Box.createHorizontalGlue());
        add(buildInfoButton());
        add(buildExitButton());
    }

    private JButton buildOpenButton() {
        JButton button = ButtonSizer.instance.setDefaultToolBarSize(new JButton(transitionToTimeSeriesPanelAction));
        button.setFocusable(true);
        button.setFocusCycleRoot(true);
        return button;
    }

    private JButton buildInfoButton() {
        return ButtonSizer.instance.setDefaultToolBarSize(new JButton(infoAction));
    }

    private JButton buildExitButton() {
        return ButtonSizer.instance.setDefaultToolBarSize(new JButton(exitAction));
    }
}