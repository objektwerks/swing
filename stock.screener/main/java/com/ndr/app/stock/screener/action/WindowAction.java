package com.ndr.app.stock.screener.action;

import com.ndr.app.stock.screener.frame.Frame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class WindowAction extends WindowAdapter {
    @Autowired private Frame frame;

	@Override
	public void windowOpened(WindowEvent event) {
        frame.onOpen();
    }

    @Override
    public void windowClosing(WindowEvent event) {
        frame.onClose();
    }
}