package com.ndr.app.stock.screener.highlighter;

import java.awt.Color;

import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

public final class NegativeNumberHighlighter extends ColorHighlighter {
    public static final HighlightPredicate negativeNumberHighlightPredicate = new NegativeNumberHighlightPredicate();
    public static final NegativeNumberHighlighter instance = new NegativeNumberHighlighter();

    public NegativeNumberHighlighter() {
        super(negativeNumberHighlightPredicate, null, Color.red);
    }

    private static class NegativeNumberHighlightPredicate implements HighlightPredicate {
        public boolean isHighlighted(java.awt.Component component, ComponentAdapter adapter) {
            Object value = adapter.getValue();
            boolean isNegative = false;
            try {
                double number = Double.parseDouble(value.toString());
                if (number < 0) {
                    isNegative = true;
                }
            } catch (NumberFormatException ignore) {}
            return isNegative;
        }
    }
}