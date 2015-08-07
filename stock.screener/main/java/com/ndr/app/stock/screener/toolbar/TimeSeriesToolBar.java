package com.ndr.app.stock.screener.toolbar;

import com.ndr.app.stock.screener.action.BuildReportAction;
import com.ndr.app.stock.screener.action.CreateTimeSeriesModelAction;
import com.ndr.app.stock.screener.action.ExitAction;
import com.ndr.app.stock.screener.action.InfoAction;
import com.ndr.app.stock.screener.action.TransitionToCriteriaIndexModelsPanelAction;
import com.ndr.app.stock.screener.action.ViewSummaryAction;
import com.ndr.app.stock.screener.button.ButtonSizer;

import javax.annotation.PostConstruct;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JToolBar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class TimeSeriesToolBar extends JToolBar {
    private static final long serialVersionUID = -4174415987982986553L;
    
	@Autowired private TransitionToCriteriaIndexModelsPanelAction transitionToCriteriaIndexModelsPanelAction;
    @Autowired private BuildReportAction buildReportAction;
    @Autowired private ViewSummaryAction viewSummaryAction;
    @Autowired private CreateTimeSeriesModelAction createTimeSeriesModelAction;
    @Autowired private InfoAction infoAction;
    @Autowired private ExitAction exitAction;

    @PostConstruct
    protected void build() {
        setFloatable(false);
        add(buildOpenButton());
        addSeparator();
        add(buildReportButton());
        add(buildSummaryButton());
        addSeparator();        
        add(buildExecuteButton());
        add(Box.createHorizontalGlue());
        add(buildInfoButton());
        add(buildExitButton());
    }

    private JButton buildOpenButton() {
        return ButtonSizer.instance.setDefaultToolBarSize(new JButton(transitionToCriteriaIndexModelsPanelAction));   
    }

    private JButton buildReportButton() {
        return ButtonSizer.instance.setDefaultToolBarSize(new JButton(buildReportAction));
    }

    private JButton buildSummaryButton() {
        return ButtonSizer.instance.setDefaultToolBarSize(new JButton(viewSummaryAction));
    }

    private JButton buildExecuteButton() {
        return ButtonSizer.instance.setDefaultToolBarSize(new JButton(createTimeSeriesModelAction));
    }

    private JButton buildInfoButton() {
        return ButtonSizer.instance.setDefaultToolBarSize(new JButton(infoAction));
    }

    private JButton buildExitButton() {
        return ButtonSizer.instance.setDefaultToolBarSize(new JButton(exitAction));
    }
}