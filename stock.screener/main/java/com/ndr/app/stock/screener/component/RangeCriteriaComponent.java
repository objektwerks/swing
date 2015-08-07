package com.ndr.app.stock.screener.component;

import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.app.stock.screener.text.RangeBoundedDecimalTextField;
import com.ndr.model.stock.screener.RangeCriteria;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class RangeCriteriaComponent extends CriteriaComponent {
    private static final long serialVersionUID = -4610978810322844018L;
    
	private RangeBoundedDecimalTextField minRangeDecimalTextField;
    private RangeBoundedDecimalTextField maxRangeDecimalTextField;
    private JCheckBox minPercentageCheckBox;
    private JCheckBox maxPercentageCheckBox;

    public RangeCriteriaComponent(ResourceManager resourceManager, RangeCriteria model) {
        super(resourceManager, model);
        build(model);
        set(model);
        setToolTipText(buildToolTipText(get()));
    }

    @Override
    public RangeCriteria getModel() {
        return get();
    }

    protected void build(RangeCriteria model) {
        setMinimumSize(new Dimension(390, 50));
        setMaximumSize(new Dimension(390, 50));
        setPreferredSize(new Dimension(390, 50));
        add(buildPanel(model), BorderLayout.CENTER);
    }

    private JPanel buildPanel(RangeCriteria model) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(buildMinPercentageCheckBox(model));
        panel.add(buildMinRangeDecimalTextField(model));
        panel.add(new JLabel("-"));
        panel.add(buildMaxRangeDecimalTextField(model));
        panel.add(buildMaxPercentageCheckBox(model));
        addListeners();
        return panel;
    }

    private JCheckBox buildMinPercentageCheckBox(RangeCriteria model) {
        minPercentageCheckBox = new JCheckBox("%", model.isMinPercentageSelected());
        return minPercentageCheckBox;
    }

    private RangeBoundedDecimalTextField buildMinRangeDecimalTextField(RangeCriteria model) {
        minRangeDecimalTextField = new RangeBoundedDecimalTextField(model.getMin(), model.getMax());
        return minRangeDecimalTextField;
    }

    private RangeBoundedDecimalTextField buildMaxRangeDecimalTextField(RangeCriteria model) {
        maxRangeDecimalTextField = new RangeBoundedDecimalTextField(model.getMin(), model.getMax());
        return maxRangeDecimalTextField;
    }

    private JCheckBox buildMaxPercentageCheckBox(RangeCriteria model) {
        maxPercentageCheckBox = new JCheckBox("%", model.isMaxPercentageSelected());
        return maxPercentageCheckBox;
    }

    private void addListeners() {
        minRangeDecimalTextField.addFocusListener(new DecimatTextFieldFocusListener());
        minRangeDecimalTextField.addKeyListener(new DecimalTextFieldKeyListener(minPercentageCheckBox, minRangeDecimalTextField));
        minPercentageCheckBox.addItemListener(new CheckBoxItemListener(minPercentageCheckBox, minRangeDecimalTextField));
        maxRangeDecimalTextField.addKeyListener(new DecimalTextFieldKeyListener(maxPercentageCheckBox, maxRangeDecimalTextField));
        maxRangeDecimalTextField.addFocusListener(new DecimatTextFieldFocusListener());
        maxPercentageCheckBox.addItemListener(new CheckBoxItemListener(maxPercentageCheckBox, maxRangeDecimalTextField));
    }
    
    private String buildToolTipText(RangeCriteria model) {
        DecimalFormat formatter = new DecimalFormat("##,###,###,###.##");
        return "<html>Low: " + formatter.format(model.getLow()) +
               "<br>Median: " + formatter.format(model.getMedian()) +
               "</br><br>High: " + formatter.format(model.getHigh()) +
               "</html>";
    }

    private RangeCriteria get() {
        RangeCriteria model = (RangeCriteria) super.getModel();
        model.setLow(minRangeDecimalTextField.getDecimal());
        model.setHigh(maxRangeDecimalTextField.getDecimal());
        model.setMinPercentageSelected(minPercentageCheckBox.isSelected());
        model.setMaxPercentageSelected(maxPercentageCheckBox.isSelected());
        return model;
    }

    private RangeCriteria set(RangeCriteria model) {
        minRangeDecimalTextField.setDecimal(model.getLow());
        maxRangeDecimalTextField.setDecimal(model.getHigh());
        minPercentageCheckBox.setSelected(model.isMinPercentageSelected());
        maxPercentageCheckBox.setSelected(model.isMaxPercentageSelected());
        return model;
    }

    private void applyRules() {
        applyDecimalRule();
        applyPercentageRule();
        setToolTipText(buildToolTipText(get()));
    }

    private void applyDecimalRule() {
        if (!minPercentageCheckBox.isSelected() && !maxPercentageCheckBox.isSelected()) {
            applyMinMaxRule();
        }
    }

    private void applyPercentageRule() {
        if (minPercentageCheckBox.isSelected() && maxPercentageCheckBox.isSelected()) {
            applyMinMaxRule();
        }
    }

    private void applyMinMaxRule() {
        double min = minRangeDecimalTextField.parseDecimalText();
        double max = maxRangeDecimalTextField.parseDecimalText();
        if (min > max) {
            minRangeDecimalTextField.setDecimal(max);
        } else if (max < min) {
            maxRangeDecimalTextField.setDecimal(min);
        }
    }

    private void validatePercentage(JCheckBox checkBox, RangeBoundedDecimalTextField textField) {
        if (checkBox.isSelected()) {
            double decimal = textField.parseDecimalText();
            if (decimal < 0 || decimal > 100) {
                textField.setDecimal(100);
            }
        }
    }

    private class DecimatTextFieldFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent event) {
            applyRules();
        }

        @Override
        public void focusLost(FocusEvent event) {
            applyRules();
        }
    }

    private class DecimalTextFieldKeyListener implements KeyListener {
        private JCheckBox checkBox;
        private RangeBoundedDecimalTextField textField;

        private DecimalTextFieldKeyListener(JCheckBox checkBox, RangeBoundedDecimalTextField textField) {
            this.checkBox = checkBox;
            this.textField = textField;
        }

        @Override
        public void keyTyped(KeyEvent event) {}

        @Override
        public void keyPressed(KeyEvent event) {}

        @Override
        public void keyReleased(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                validatePercentage(checkBox, textField);
                applyRules();                
            }
        }
    }

    private class CheckBoxItemListener implements ItemListener {
        private JCheckBox checkBox;
        private RangeBoundedDecimalTextField textField;

        private CheckBoxItemListener(JCheckBox checkBox, RangeBoundedDecimalTextField textField) {
            this.checkBox = checkBox;
            this.textField = textField;
        }

        @Override
        public void itemStateChanged(ItemEvent event) {
            validatePercentage(checkBox, textField);
            applyRules();
        }
    }
}