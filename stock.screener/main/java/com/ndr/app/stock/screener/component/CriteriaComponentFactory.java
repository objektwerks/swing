package com.ndr.app.stock.screener.component;

import com.ndr.app.stock.screener.resource.ResourceManager;
import com.ndr.model.stock.screener.Criteria;
import com.ndr.model.stock.screener.ExtendedHistogramCriteria;
import com.ndr.model.stock.screener.HistogramCriteria;
import com.ndr.model.stock.screener.MultiSelectCriteria;
import com.ndr.model.stock.screener.RangeCriteria;

public enum CriteriaComponentFactory {
    instance;

    public CriteriaComponent newInstance(ResourceManager resourceManager, Criteria criteria) {
        CriteriaComponent criteriaComponent;
        if (criteria instanceof RangeCriteria) {
            criteriaComponent = newInstance(resourceManager, (RangeCriteria) criteria);
        } else if (criteria instanceof ExtendedHistogramCriteria) {
            criteriaComponent = newInstance(resourceManager, (ExtendedHistogramCriteria) criteria);
        } else if (criteria instanceof HistogramCriteria) {
            criteriaComponent = newInstance(resourceManager, (HistogramCriteria) criteria);
        } else if (criteria instanceof MultiSelectCriteria) {
            criteriaComponent = newInstance(resourceManager, (MultiSelectCriteria) criteria);
        } else {
            throw new IllegalArgumentException("Invalid model: " + criteria);
        }
        return criteriaComponent;
    }

    private CriteriaComponent newInstance(ResourceManager resourceManager, RangeCriteria criteria) {
        return new RangeCriteriaComponent(resourceManager, criteria);
    }

    private CriteriaComponent newInstance(ResourceManager resourceManager, ExtendedHistogramCriteria criteria) {
        return new ExtendedHistogramCriteriaComponent(resourceManager, criteria);
    }

    private CriteriaComponent newInstance(ResourceManager resourceManager, HistogramCriteria criteria) {
        return new HistogramCriteriaComponent(resourceManager, criteria);
    }

    private CriteriaComponent newInstance(ResourceManager resourceManager, MultiSelectCriteria criteria) {
        return new MultiSelectCriteriaComponent(resourceManager, criteria);
    }
}