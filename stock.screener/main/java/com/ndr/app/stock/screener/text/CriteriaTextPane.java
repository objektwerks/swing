package com.ndr.app.stock.screener.text;

import java.awt.Dimension;

import javax.annotation.PostConstruct;
import javax.swing.JTextPane;

import org.springframework.stereotype.Component;

@Component
public final class CriteriaTextPane extends JTextPane {
    private static final long serialVersionUID = 483330861819015822L;

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
        setPreferredSize(new Dimension(300, 75));        
    }
}