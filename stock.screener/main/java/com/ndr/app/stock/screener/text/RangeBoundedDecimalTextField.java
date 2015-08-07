package com.ndr.app.stock.screener.text;

import java.awt.Color;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

public final class RangeBoundedDecimalTextField extends JFormattedTextField {
    private static final long serialVersionUID = -6738694147515517741L;
	private static final String defaultFormatPattern = "###,###,###,###.###";
    private static final int defaultColumns = 18;
    
    private double minDecimal;
    private double maxDecimal;

    public RangeBoundedDecimalTextField() {
        super(new DecimalFormat(defaultFormatPattern));
        minDecimal = Double.MIN_VALUE;
        maxDecimal = Double.MAX_VALUE;
        setDecimal(minDecimal);
        build(defaultColumns);
    }

    public RangeBoundedDecimalTextField(double minDecimal, double maxDecimal) {
        super(new DecimalFormat(defaultFormatPattern));
        this.minDecimal = minDecimal;
        this.maxDecimal = maxDecimal;
        setDecimal(minDecimal);
        build(defaultColumns);
    }

    public double getDecimal() {
        return toDouble(getValue());
    }

    public void setDecimal(double decimal) {
        if (isValid(decimal)) {
            setValue(decimal);
        }
    }

    public double parseDecimalText() {
        double doubleValue;
        try {
            doubleValue = toDouble(new DecimalFormat(defaultFormatPattern).parse(getText()));
        } catch (ParseException ignoreInvalidDouble) {
            doubleValue = 0.0;
        }
        return doubleValue;
    }

    private void build(int columns) {
        setColumns(columns);
        setMinimumSize(new Dimension(150, 25));
        setMaximumSize(new Dimension(150, 25));
        setPreferredSize(new Dimension(150, 25));
        setInputVerifier(buildInputVerifier());
    }

    private InputVerifier buildInputVerifier() {
        return new InputVerifier() {
            @Override
            public boolean verify(JComponent component) {
                return isValid(parseDecimalText());
            }
        };
    }

    private boolean isValid(double decimal) {
        boolean isValid = (decimal >= minDecimal && decimal <= maxDecimal);
        if (isValid) {
            setBackground(Color.white);
        } else {
            setBackground(Color.lightGray);
        }
        return isValid;
    }

    private double toDouble(Object value) {
        return (value instanceof Long) ? (Long) value : (Double) value;
    }
}