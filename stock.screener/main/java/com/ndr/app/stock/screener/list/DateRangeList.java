package com.ndr.app.stock.screener.list;

import com.ndr.model.stock.screener.DateRange;

import java.util.Calendar;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class DateRangeList<E> extends EntityList<E> {
    private static final long serialVersionUID = 6099973307592569140L;

    public DateRangeList() {
        super();
    }

    public DateRange getNextDateRange() {
        DateRange nextDateRange;
        DateRange oldestDateRange;
        if (isNotEmpty()) {
            oldestDateRange = (DateRange) entityModel.get(entityModel.size() - 1);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(oldestDateRange.getEndDate());
            calendar.add(Calendar.DATE, 1);
            nextDateRange = DateRange.newInstance(calendar.getTime());
        } else {
            nextDateRange = DateRange.newInstance();
        }
        return nextDateRange;
    }
}