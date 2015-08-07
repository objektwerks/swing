package com.ndr.app.stock.screener.transition;

import java.io.Serializable;

public interface TransitionListener extends Serializable {
    public void transitionTo(Transition transition);
}