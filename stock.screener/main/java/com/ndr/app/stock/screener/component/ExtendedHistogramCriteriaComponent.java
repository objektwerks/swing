package com.ndr.app.stock.screener.component;

import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.model.stock.screener.ExtendedHistogramCriteria;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public final class ExtendedHistogramCriteriaComponent extends HistogramCriteriaComponent {
    private static final long serialVersionUID = -3195539394684132826L;
    
	private JSpinner dailyChangeSpinner;
    private JComboBox weeklyChangeComboBox;

    public ExtendedHistogramCriteriaComponent(ResourceManager resourceManager, ExtendedHistogramCriteria model) {
        super(resourceManager, model);
        build();
        set(model);
    }

    @Override
    public ExtendedHistogramCriteria getModel() {
        return get();
    }

    private void build() {
        setMinimumSize(new Dimension(390, 150));
        setMaximumSize(new Dimension(390, 150));
        setPreferredSize(new Dimension(390, 150));
        add(buildPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel(resourceManager.getString("extended.distribution.criteria.component.text.one")));
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(buildDailyChangeSpinner());
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(new JLabel(resourceManager.getString("extended.distribution.criteria.component.text.two")));
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(buildComboBox());
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(new JLabel(resourceManager.getString("extended.distribution.criteria.component.text.three")));
        return panel;
    }

    private JSpinner buildDailyChangeSpinner() {
        SpinnerModel spinnerModel = new SpinnerNumberModel(1.0, 0.0, 100.0, 1.0);
        dailyChangeSpinner = new JSpinner(spinnerModel);
        dailyChangeSpinner.setMinimumSize(new Dimension(70, 25));
        dailyChangeSpinner.setMaximumSize(new Dimension(70, 25));
        dailyChangeSpinner.setPreferredSize(new Dimension(70, 25));
        return dailyChangeSpinner;
    }
    private JComboBox buildComboBox() {
        weeklyChangeComboBox = new JComboBox();
        String model = resourceManager.getString("extended.distribution.criteria.component.combobox.model");
        String[] values = model.split(",");
        for (String value : values) {
            weeklyChangeComboBox.addItem(value);
        }
        weeklyChangeComboBox.setMinimumSize(new Dimension(50, 25));
        weeklyChangeComboBox.setMaximumSize(new Dimension(50, 25));
        weeklyChangeComboBox.setPreferredSize(new Dimension(50, 25));
        return weeklyChangeComboBox;
    }

    private ExtendedHistogramCriteria get() {
        ExtendedHistogramCriteria model = (ExtendedHistogramCriteria) super.getModel();
        model.setDailyChangeDecimal((Double) dailyChangeSpinner.getValue());
        model.setWeeklyChange(weeklyChangeComboBox.getSelectedItem());
        return model;
    }

    private ExtendedHistogramCriteria set(ExtendedHistogramCriteria model) {
        dailyChangeSpinner.setValue(model.getDailyChangeDecimal());
        weeklyChangeComboBox.setSelectedItem(model.getWeeklyChange());
        return model;
    }    
}