package com.ndr.app.stock.screener.slider;

import javax.swing.JSlider;

public final class RangeSlider extends JSlider {
    private static final long serialVersionUID = 4175952445563585296L;

	public RangeSlider() {
        super();
    }

    public RangeSlider(int min, int max) {
        super(min, max);
    }

    public RangeSlider(int min, int max, int lowerValue, int upperValue) {
        this(min, max);
        setValue(lowerValue);
        setUpperValue(upperValue);
    }

    public int lowerX() {
        return getRangeSliderUI().lowerX();        
    }

    public int upperX() {
        return getRangeSliderUI().upperX();
    }

    public int toValue(int x) {
        return getRangeSliderUI().toValue(x);
    }

    @Override
    public void updateUI() {
        setUI(new RangeSliderUI(this));
        updateLabelUIs();
    }

    @Override
    public int getValue() {
        return super.getValue();
    }

    @Override
    public void setValue(int value) {
        int oldValue = getValue();
        if (oldValue != value) {
            int oldExtent = getExtent();
            int newValue = Math.min(Math.max(getMinimum(), value), oldValue + oldExtent);
            int newExtent = oldExtent + oldValue - newValue;
            getModel().setRangeProperties(newValue, newExtent, getMinimum(), getMaximum(), getValueIsAdjusting());
        }
    }

    public int getUpperValue() {
        return getValue() + getExtent();
    }

    public void setUpperValue(int value) {
        int lowerValue = getValue();
        int newExtent = Math.min(Math.max(0, value - lowerValue), getMaximum() - lowerValue);
        setExtent(newExtent);
    }

    public boolean isLowerKnobLocked() {
        return getRangeSliderUI().isLowerKnobLocked();
    }

    public void setLowerKnobLocked(boolean isLowerKnobLocked) {
        getRangeSliderUI().setLowerKnobLocked(isLowerKnobLocked);
    }

    public boolean isUpperKnobLocked() {
        return getRangeSliderUI().isUpperKnobLocked();
    }

    public void setUpperKnobLocked(boolean isUpperKnobLocked) {
        getRangeSliderUI().setUpperKnobLocked(isUpperKnobLocked);
    }

    private RangeSliderUI getRangeSliderUI() {
        return (RangeSliderUI) getUI();       
    }
}