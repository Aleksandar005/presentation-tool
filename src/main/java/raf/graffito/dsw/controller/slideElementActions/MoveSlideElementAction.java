package raf.graffito.dsw.controller.slideElementActions;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.state.concrete.SelectState;

import java.awt.event.ActionEvent;

public class MoveSlideElementAction extends AbstractGraffAction {
    private final SlideView slideView;

    public MoveSlideElementAction(SlideView slideView) {
        super("Move element", "Moving slide element");
        this.slideView = slideView;
    }

    public void actionPerformed(ActionEvent e) {
        if (!(slideView.getStateManager().getCurrentState() instanceof SelectState)) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                    "NOTHING IS SELECTED TO MOVE", MessageType.WARNING, slideView
            ));
            return;
        }

        // Aktiviraj MoveState
        slideView.getStateManager().setMoveState();

        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                "ENTERED IN 'MOVE' STATE", MessageType.INFO, slideView
        ));
    }
}
