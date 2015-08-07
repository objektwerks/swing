package com.ndr.app.stock.screener.text;

import java.awt.Dimension;

import javax.annotation.PostConstruct;
import javax.swing.JTextPane;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public final class NoteTextPane extends JTextPane {
    private static final long serialVersionUID = 483366861818815822L;

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
        setPreferredSize(new Dimension(300, 100));
    }
}