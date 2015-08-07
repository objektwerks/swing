package com.ndr.app.stock.screener.component;

import com.ndr.app.stock.screener.button.LockedUnlockedToggleButton;
import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.text.RangeBoundedDecimalTextField;
import com.ndr.model.stock.screener.HistogramCriteria;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HistogramCriteriaComponent extends CriteriaComponent {
    private static final long serialVersionUID = -4459242705262300296L;
    
	private HistogramSlider histogramSlider;
    private RangeBoundedDecimalTextField lowerRangeDecimalTextField;
    private LockedUnlockedToggleButton lowerLockedUnlockedToggleButton;
    private LockedUnlockedToggleButton upperLockedUnlockedToggleButton;
    private RangeBoundedDecimalTextField upperRangeDecimalTextField;

    public HistogramCriteriaComponent(ResourceManager resourceManager, HistogramCriteria model) {
        super(resourceManager, model);
        build(model);
        set(model);
    }

    @Override
    public HistogramCriteria getModel() {
        return get();
    }

    protected void build(HistogramCriteria model) {
        setMinimumSize(new Dimension(390, 125));
        setMaximumSize(new Dimension(390, 125));
        setPreferredSize(new Dimension(390, 125));
        histogramSlider = new HistogramSlider(model.getFrequencyDistribution());
        add(buildFrequencyDistributionPanel(), BorderLayout.CENTER);
    }

    private JPanel buildFrequencyDistributionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panel.add(histogramSlider, BorderLayout.CENTER);
        panel.add(buildLockedUnlockedPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildLockedUnlockedPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(buildLowerLockedUnlockedPanel());
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(buildUpperLockedUnlockedPanel());
        return panel;
    }

    private JPanel buildLowerLockedUnlockedPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(buildLowerRangeDecimalTextField());
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        lowerLockedUnlockedToggleButton = new LockedUnlockedToggleButton(resourceManager);
        lowerLockedUnlockedToggleButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                if (lowerLockedUnlockedToggleButton.isSelected()) {
                    histogramSlider.setLowerKnobLocked(true);
                    lowerRangeDecimalTextField.setEditable(false);
                } else {
                    histogramSlider.setLowerKnobLocked(false);
                    lowerRangeDecimalTextField.setEditable(true);
                }
            }
        });
        panel.add(lowerLockedUnlockedToggleButton);
        return panel;
    }

    private JPanel buildUpperLockedUnlockedPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        upperLockedUnlockedToggleButton = new LockedUnlockedToggleButton(resourceManager);
        upperLockedUnlockedToggleButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                if (upperLockedUnlockedToggleButton.isSelected()) {
                    histogramSlider.setUpperKnobLocked(true);
                    upperRangeDecimalTextField.setEditable(false);
                } else {
                    histogramSlider.setUpperKnobLocked(false);
                    upperRangeDecimalTextField.setEditable(true);
                }
            }
        });
        panel.add(upperLockedUnlockedToggleButton);
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        panel.add(buildUpperRangeDecimalTextField());
        return panel;
    }

    private JFormattedTextField buildLowerRangeDecimalTextField() {
        lowerRangeDecimalTextField = new RangeBoundedDecimalTextField(histogramSlider.getLowerValue(),
                                                                      histogramSlider.getUpperValue());
        lowerRangeDecimalTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                double lowerValue = lowerRangeDecimalTextField.parseDecimalText();
                double upperValue = upperRangeDecimalTextField.getDecimal();
                if (lowerValue <= upperValue) {
                    histogramSlider.setLowerValue(lowerValue);
                }
            }
        });
        histogramSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                lowerRangeDecimalTextField.setDecimal(histogramSlider.getLowerValue());
            }
        });
        lowerRangeDecimalTextField.setDecimal(histogramSlider.getLowerValue());
        return lowerRangeDecimalTextField;
    }

    private JFormattedTextField buildUpperRangeDecimalTextField() {
        upperRangeDecimalTextField = new RangeBoundedDecimalTextField(histogramSlider.getLowerValue(),
                                                                      histogramSlider.getUpperValue());
        upperRangeDecimalTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                double upperValue = upperRangeDecimalTextField.parseDecimalText();
                double lowerValue = lowerRangeDecimalTextField.getDecimal();
                if (upperValue >= lowerValue) {
                    histogramSlider.setUpperValue(upperValue);
                }
            }
        });
        histogramSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                upperRangeDecimalTextField.setDecimal(histogramSlider.getUpperValue());
            }
        });
        upperRangeDecimalTextField.setDecimal(histogramSlider.getUpperValue());
        return upperRangeDecimalTextField;
    }

    private HistogramCriteria get() {
        HistogramCriteria model = (HistogramCriteria) super.getModel();
        model.setLowerDecimal(lowerRangeDecimalTextField.getDecimal());
        model.setLowerLockedUnlockedSelected(lowerLockedUnlockedToggleButton.isSelected());
        model.setUpperLockedUnlockedSelected(upperLockedUnlockedToggleButton.isSelected());
        model.setUpperDecimal(upperRangeDecimalTextField.getDecimal());
        return model;
    }

    private HistogramCriteria set(HistogramCriteria model) {
        lowerRangeDecimalTextField.setDecimal(histogramSlider.getLowerValue());
        histogramSlider.setLowerKnobLocked(model.isLowerLockedUnlockedSelected());
        histogramSlider.setUpperKnobLocked(model.isUpperLockedUnlockedSelected());
        upperRangeDecimalTextField.setDecimal(histogramSlider.getUpperValue());
        return model;
    }
}