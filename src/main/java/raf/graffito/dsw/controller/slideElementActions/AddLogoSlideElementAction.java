package raf.graffito.dsw.controller.slideElementActions;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.gui.swing.SlideView;

import java.awt.event.ActionEvent;

public class AddLogoSlideElementAction extends AbstractGraffAction {

    private final SlideView slideView;

    public AddLogoSlideElementAction(SlideView slideView) {
        super("Add logo", "Add logo slide element");
        this.slideView = slideView;
    }

    public void actionPerformed(ActionEvent e) {
        slideView.getStateManager().setAddLogoState();
    }
}
