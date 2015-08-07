package com.ndr.app.stock.screener.tab;

import com.ndr.app.stock.screener.panel.IndexModelPanel;

import java.awt.BorderLayout;

import javax.annotation.PostConstruct;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class IndexModelTab extends JPanel {
    private static final long serialVersionUID = -8193727038313883320L;
    
	@Autowired private IndexModelPanel indexModelPanel;

    @PostConstruct
    protected void build() {
        setLayout(new BorderLayout());
        add(indexModelPanel, BorderLayout.CENTER);
    }
}