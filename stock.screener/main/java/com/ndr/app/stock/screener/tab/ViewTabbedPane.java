package com.ndr.app.stock.screener.tab;

import com.ndr.app.stock.screener.resource.ResourceManager;

import javax.annotation.PostConstruct;
import javax.swing.JTabbedPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class ViewTabbedPane extends JTabbedPane {
    private static final long serialVersionUID = -1435097394098736639L;
    
	@Autowired private ResourceManager resourceManager;
    @Autowired private TimeSeriesTab timeSeriesTab;
    @Autowired private AlphaDecayTab alphaDecayTab;

    @PostConstruct
    protected void build() {
        addTab(resourceManager.getString("time.series.tab.title"), timeSeriesTab);
        addTab(resourceManager.getString("alpha.decay.tab.title"), alphaDecayTab);
    }    
}