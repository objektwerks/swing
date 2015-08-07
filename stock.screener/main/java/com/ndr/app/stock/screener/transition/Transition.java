package com.ndr.app.stock.screener.transition;

import java.io.Serializable;

import javax.swing.JPanel;

public interface Transition extends Serializable {
    public String getKey();
    public JPanel transitionTo(TransitionListener transitionListener);
}