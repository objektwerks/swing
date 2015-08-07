package com.ndr.app.stock.screener.component;

import com.ndr.app.stock.screener.list.SelectableEntityList;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.model.stock.screener.MultiSelectCriteria;
import com.ndr.model.stock.screener.MultiSelectCriteriaItem;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class MultiSelectCriteriaComponent extends CriteriaComponent {
    private static final long serialVersionUID = -7524649434042004241L;
    
	private SelectableEntityList<MultiSelectCriteriaItem> selectableEntityList;

    public MultiSelectCriteriaComponent(ResourceManager resourceManager, MultiSelectCriteria model) {
        super(resourceManager, model);
        build();
        set(model);
    }

    @Override
    public MultiSelectCriteria getModel() {
        return get();
    }

    private void build() {
        setMinimumSize(new Dimension(390, 100));
        setMaximumSize(new Dimension(390, 100));
        setPreferredSize(new Dimension(390, 100));
        selectableEntityList = new SelectableEntityList<MultiSelectCriteriaItem>();
        add(buildPanel(), BorderLayout.CENTER);
    }

    private JPanel buildPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JScrollPane(selectableEntityList));
        return panel;
    }

    private MultiSelectCriteria get() {
        return (MultiSelectCriteria) super.getModel();
    }

    private MultiSelectCriteria set(MultiSelectCriteria model) {
        selectableEntityList.setSelectables(model.getItems(), model.getSelectedItemIndices());
        return model;
    }
}