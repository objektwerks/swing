package com.ndr.app.stock.screener.transition;

import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.transitions.ScreenTransition;
import org.jdesktop.animation.transitions.TransitionTarget;

public final class TransitionContainer extends JPanel implements TransitionListener, TransitionTarget {
    private static final long serialVersionUID = -7503838532870746683L;
    
	private ScreenTransition screenTransition;
    private Map<String, JPanel> panels;
    private String currentKey;

    public TransitionContainer(JPanel startPanel) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        screenTransition = buildTransition();
        panels = new HashMap<String, JPanel>();
        add(startPanel);
    }

    public TransitionContainer add(Transition transition) {
        JPanel panel = transition.transitionTo(this);
        panels.put(transition.getKey(), panel);
        return this;
    }

    public void transitionTo(Transition transition) {
        currentKey = transition.getKey();
        screenTransition.start();
    }

    @Override
    public void setupNextScreen() {
        removeAll();
        add(panels.get(currentKey));
    }

    private ScreenTransition buildTransition() {
        Animator animator = new Animator(1000);
        animator.setAcceleration(.5f);
        animator.setDeceleration(.5f);
        return new ScreenTransition(this, this, animator);
    }
}