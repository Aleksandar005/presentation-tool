package raf.graffito.dsw.controller.listeners.mediator_listeners;

import raf.graffito.dsw.mediator.ISlideMediator;
import raf.graffito.dsw.mediator.SlideColleague;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Listener za promenu strategije provere prostora.
public class StrategyChangeListener implements ActionListener {

    private final ISlideMediator mediator;
    private final SlideColleague colleague;
    private final String event;

    public StrategyChangeListener(ISlideMediator mediator, SlideColleague colleague, String event) {
        this.mediator = mediator;
        this.colleague = colleague;
        this.event = event;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (mediator != null) {
            mediator.notify(colleague, event);
        }
    }
}