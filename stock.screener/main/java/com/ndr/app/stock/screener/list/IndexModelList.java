package com.ndr.app.stock.screener.list;

import com.ndr.app.stock.screener.resource.ResourceManager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class IndexModelList<E> extends EntityList<E> {
    private static final long serialVersionUID = 6412166734269416027L;
    
	@Autowired private ResourceManager resourceManager;

    public IndexModelList() {
        super();
    }

    @PostConstruct
    protected void build() {
        setEntityIcon(resourceManager.getImageIcon("index.model.icon"));
    }
}