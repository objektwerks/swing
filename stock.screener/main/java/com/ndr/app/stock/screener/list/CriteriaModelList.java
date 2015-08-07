package com.ndr.app.stock.screener.list;

import com.ndr.app.stock.screener.resource.ResourceManager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class CriteriaModelList<E> extends EntityList<E> {
    private static final long serialVersionUID = 1593240552694774145L;
    
	@Autowired private ResourceManager resourceManager;

    public CriteriaModelList() {
        super();
    }

    @PostConstruct
    protected void build() {
        setEntityIcon(resourceManager.getImageIcon("criteria.model.icon"));
    }
}