package com.ndr.app.stock.screener.cache;

import com.ndr.model.stock.screener.TimeSeriesRows;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public final class TimeSeriesRowsCache {
    private Map<Date, TimeSeriesRows> cache;

    private TimeSeriesRowsCache() {
        cache = new LinkedHashMap<Date, TimeSeriesRows>();
    }

    public void put(Date date, TimeSeriesRows rows) {
        cache.put(date, rows);
    }

    public TimeSeriesRows get(Date date) {
        return cache.get(date);
    }

    public boolean containsKey(Date date) {
        return cache.containsKey(date);
    }

    public void clear() {
        cache.clear();
    }
}