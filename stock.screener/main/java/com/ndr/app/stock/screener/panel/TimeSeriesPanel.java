package com.ndr.app.stock.screener.panel;

import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.statusbar.TimeSeriesStatusBar;
import com.ndr.app.stock.screener.tab.ModelTabbedPane;
import com.ndr.app.stock.screener.tab.ViewTabbedPane;
import com.ndr.app.stock.screener.toolbar.TimeSeriesToolBar;

import java.awt.BorderLayout;

import javax.annotation.PostConstruct;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class TimeSeriesPanel extends JPanel {
    private static final long serialVersionUID = 1184119537003860001L;
    
	@Autowired private ResourceManager resourceManager;
    @Autowired private TimeSeriesToolBar timeSeriesToolBar;
    @Autowired private ModelTabbedPane modelTabbedPane;
    @Autowired private ViewTabbedPane viewTabbedPane;
    @Autowired private TimeSeriesStatusBar timeSeriesStatusBar;

    @PostConstruct
    protected void build() {
        setLayout(new BorderLayout());
        add(timeSeriesToolBar, BorderLayout.NORTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, modelTabbedPane, viewTabbedPane);
        splitPane.setDividerLocation(445);
        splitPane.setOneTouchExpandable(true);
        add(splitPane, BorderLayout.CENTER);
        add(timeSeriesStatusBar, BorderLayout.SOUTH);
        timeSeriesStatusBar.setStatus(resourceManager.getString("status.bar.ready"));
    }
}