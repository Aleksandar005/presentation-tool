package raf.graffito.dsw.controller.slideElementActions;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;

import java.awt.event.ActionEvent;

public class SelectSlideElementAction extends AbstractGraffAction {
    private final SlideView slideView;

    public SelectSlideElementAction(SlideView slideView) {
        super("Select element", "Selecting slide element");
        this.slideView = slideView;
    }

    public void actionPerformed(ActionEvent e) {
        slideView.getStateManager().setSelectState();
        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                "ENTERED IN 'SELECT' STATE", MessageType.INFO, slideView
        ));
    }
}
