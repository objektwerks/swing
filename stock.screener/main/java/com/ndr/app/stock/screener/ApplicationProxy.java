package com.ndr.app.stock.screener;

import com.ndr.model.stock.screener.Application;
import com.ndr.model.stock.screener.Criteria;
import com.ndr.model.stock.screener.StockUniverse;
import com.ndr.model.stock.screener.User;

import java.util.List;
import java.util.Map;

public enum ApplicationProxy {
    instance;

    private Application application;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public User getUser() {
        return application.getUser();
    }
    
    public List<Criteria> getPrototypeCriteria() {
        return application.getPrototypeCriteria();
    }

    public Map<Integer, String> getSubIndustries() {
        return application.getSubIndustries();
    }

    public StockUniverse newStockUniverseInstance() {
        return application.getPrototypeStockUniverse().newPrototypeInstance();
    }
}