package com.ndr.app.stock.screener.frame;

import com.ndr.app.stock.screener.action.TransitionToCriteriaIndexModelsPanelAction;
import com.ndr.app.stock.screener.action.TransitionToTimeSeriesPanelAction;
import com.ndr.app.stock.screener.action.WindowAction;
import com.ndr.app.stock.screener.panel.CriteriaIndexModelsPanel;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.transition.TransitionContainer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.annotation.PostConstruct;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class Frame extends JFrame {
    private static final long serialVersionUID = -6980932668313977715L;

	@Autowired private ResourceManager resourceManager;
    @Autowired private WindowAction windowAction;
    @Autowired private OnOpenHandler onOpenHandler;
    @Autowired private OnCloseHandler onCloseHandler;
    @Autowired private CriteriaIndexModelsPanel criteriaIndexModelsPanel;
    @Autowired private TransitionToTimeSeriesPanelAction transitionToTimeSeriesPanelAction;
    @Autowired private TransitionToCriteriaIndexModelsPanelAction transitionToCriteriaIndexModelsPanelAction;

    public void onOpen() {
        onOpenHandler.onOpen();
    }

    public void onClose() {
        onCloseHandler.onClose();
    }

    public Point getDefaultScreenLocation() {
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameDimension = getSize();
        return new Point((screenDimension.width - frameDimension.width) / 2, (screenDimension.height - frameDimension.height) / 2);
    }

    @PostConstruct
    protected void build() {
        setTitle(resourceManager.getString("app.title"));
        setIconImage(resourceManager.getImage("app.icon"));
        setLocation(getLocation());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowFocusListener(windowAction);
        addWindowListener(windowAction);
        addWindowStateListener(windowAction);
        add(buildTransitionContainer());
    }

    private TransitionContainer buildTransitionContainer() {
        TransitionContainer transitionContainer = new TransitionContainer(criteriaIndexModelsPanel);
        transitionContainer.add(transitionToTimeSeriesPanelAction);
        transitionContainer.add(transitionToCriteriaIndexModelsPanelAction);
        return transitionContainer;
    }
}