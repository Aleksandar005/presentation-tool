package raf.graffito.dsw.controller.slideElementActions;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.gui.swing.SlideView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AddTextSlideElementAction extends AbstractGraffAction {

    private final SlideView slideView;

    public AddTextSlideElementAction(SlideView slideView) {
        super("Text", "Adding text");
        this.slideView = slideView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (slideView.getCurrentSlide() == null) {
            JOptionPane.showMessageDialog(slideView,
                    "No slide is open!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Aktiviraj AddTextState
        slideView.getStateManager().setAddTextState();
    }
}
