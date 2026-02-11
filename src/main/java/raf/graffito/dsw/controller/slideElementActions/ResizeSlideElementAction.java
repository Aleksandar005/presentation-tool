package raf.graffito.dsw.controller.slideElementActions;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.state.concrete.SelectState;

import java.awt.event.ActionEvent;


public class ResizeSlideElementAction extends AbstractGraffAction {
    private final SlideView slideView;

    public ResizeSlideElementAction(SlideView slideView) {
        super("Resize element", "Resizing slide elements");
        this.slideView = slideView;
    }

    public void actionPerformed(ActionEvent e) {
        if (!(slideView.getStateManager().getCurrentState() instanceof SelectState)) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                    "NOTHING IS SELECTED TO RESIZE", MessageType.WARNING, slideView
            ));
            return;
        }

        SelectState selectState = (SelectState) slideView.getStateManager().getCurrentState();

        if (selectState.getSelectedElements().isEmpty()) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                    "NO ELEMENTS SELECTED", MessageType.WARNING, slideView
            ));
            return;
        }

        slideView.getStateManager().setResizeState();

        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                "ENTERED IN 'RESIZE' STATE - Drag to resize", MessageType.INFO, slideView
        ));
    }
}