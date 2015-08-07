package com.ndr.app.stock.screener.list;

import com.ndr.model.stock.screener.RebalanceFrequencyDate;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public final class RebalanceFrequencyDateList<E> extends EntityList<E> {
    private static final long serialVersionUID = 145894059051332933L;
    
    public RebalanceFrequencyDateList() {
        super();
    }

    public void updateRebalanceFrequencyDate(RebalanceFrequencyDate rebalanceFrequencyDate) {
        RebalanceFrequencyDate selectedRebalanceFrequencyDate = (RebalanceFrequencyDate) getSelectedEntity();
        selectedRebalanceFrequencyDate.setDate(rebalanceFrequencyDate.getDate());
        setSelectedValue(selectedRebalanceFrequencyDate, true);
    }

    public RebalanceFrequencyDate getNextRebalanceFrequencyDate() {
        RebalanceFrequencyDate nextRebalanceFrequencyDate;
        if (isNotEmpty()) {
            RebalanceFrequencyDate oldestRebalanceFrequencyDate = (RebalanceFrequencyDate) entityModel.get(entityModel.size() - 1);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(oldestRebalanceFrequencyDate.getDate());
            calendar.add(Calendar.DATE, 1);
            nextRebalanceFrequencyDate = new RebalanceFrequencyDate(calendar.getTime());
        } else {
            nextRebalanceFrequencyDate = new RebalanceFrequencyDate(new Date());
        }
        return nextRebalanceFrequencyDate;
    }
}