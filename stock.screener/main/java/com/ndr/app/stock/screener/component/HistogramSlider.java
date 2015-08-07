package com.ndr.app.stock.screener.component;

import com.ndr.app.stock.screener.slider.RangeSlider;
import com.ndr.model.stock.screener.FrequencyDistribution;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public final class HistogramSlider extends JPanel {
    private static final long serialVersionUID = -1338218402505149139L;
    
	private FrequencyDistribution frequencyDistribution;
    private Histogram histogram;
    private RangeSlider rangeSlider;

    public HistogramSlider(FrequencyDistribution frequencyDistribution) {
        this.frequencyDistribution = frequencyDistribution;
        build();
    }

    public boolean isLowerKnobLocked() {
        return rangeSlider.isLowerKnobLocked();
    }

    public void setLowerKnobLocked(boolean isLowerKnobLocked) {
        rangeSlider.setLowerKnobLocked(isLowerKnobLocked);
    }

    public boolean isUpperKnobLocked() {
        return rangeSlider.isUpperKnobLocked();
    }

    public void setUpperKnobLocked(boolean isUpperKnobLocked) {
        rangeSlider.setUpperKnobLocked(isUpperKnobLocked);
    }

    public double getLowerValue() {
        return rangeSlider.getValue();
    }

    public void setLowerValue(double value) {
        rangeSlider.setValue((int) value);
    }
    
    public double getUpperValue() {
        return rangeSlider.getUpperValue();
    }

    public void setUpperValue(double value) {
        rangeSlider.setUpperValue((int) value);
    }

    public void addChangeListener(ChangeListener changeListener) {
        rangeSlider.addChangeListener(changeListener);
    }

    protected void build() {
        setMinimumSize(new Dimension(390, 125));
        setMaximumSize(new Dimension(390, 125));
        setPreferredSize(new Dimension(390, 125));
        setLayout(new BorderLayout());
        add(buildHistogram(), BorderLayout.CENTER);
        add(buildRangeSlider(), BorderLayout.SOUTH);
    }
    
    private Histogram buildHistogram() {
        histogram = new Histogram(frequencyDistribution);
        histogram.addMouseListener(new HistogramMouseListener());
        return histogram;
    }

    private RangeSlider buildRangeSlider() {
        rangeSlider = new RangeSlider((int) frequencyDistribution.min(), (int) frequencyDistribution.max());
        rangeSlider.setValue((int) frequencyDistribution.min());
        rangeSlider.setUpperValue((int) frequencyDistribution.max());
        rangeSlider.addChangeListener(new RangeSliderChangeListener());
        return rangeSlider;
    }

    private class RangeSliderChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent event) {
            histogram.paintOverlay(rangeSlider.lowerX(), rangeSlider.upperX());
        }
    }

    private class HistogramMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            int x = event.getX();
            int value = rangeSlider.toValue(x);
            int medianX = (rangeSlider.upperX() + rangeSlider.lowerX()) / 2;
            if (x >= medianX) {
                if (!isUpperKnobLocked()) {
                    rangeSlider.setUpperValue(value);
                }
            } else {
                if (!isLowerKnobLocked()) {
                    rangeSlider.setValue(value);                
                }
            }
        }
    }
}