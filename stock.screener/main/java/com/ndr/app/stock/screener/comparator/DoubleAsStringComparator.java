package com.ndr.app.stock.screener.comparator;

import java.util.Comparator;

public final class DoubleAsStringComparator implements Comparator<String> {
    public static final DoubleAsStringComparator instance = new DoubleAsStringComparator();
    
    @Override
    public int compare(String firstValue, String secondValue) {
        int result = 0;
        try {
            Double firstDouble = Double.parseDouble(firstValue);
            Double secondDouble = Double.parseDouble(secondValue);
            result = firstDouble.compareTo(secondDouble);
        } catch (NumberFormatException e) {
            result = firstValue.compareTo(secondValue);
        }
        return result;
    }
}