package com.ndr.app.stock.screener.text;

import java.awt.Dimension;

import javax.annotation.PostConstruct;
import javax.swing.JTextPane;

import org.springframework.stereotype.Component;


@Component
public final class BenchmarkPortfolioStatisticsTextPane extends JTextPane {
    private static final long serialVersionUID = -8271916085641040317L;

    @Override
    public void setText(String text) {
        if (text == null) text = "";
        super.setText(text);
        setSelectionStart(0);
        setSelectionEnd(1);
    }

	@PostConstruct
    protected void build() {
        setEditable(false);
        setPreferredSize(new Dimension(200, 75));        
    }
}