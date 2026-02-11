package raf.graffito.dsw.controller.slideElementActions;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.state.concrete.SelectState;

import java.awt.event.ActionEvent;

public class DeleteSlideElementAction extends AbstractGraffAction {
    private final SlideView slideView;

    public DeleteSlideElementAction(SlideView slideView) {
        super("Delete element", "Deleting slide element");
        this.slideView = slideView;
    }

    public void actionPerformed(ActionEvent e) {
        if(!(slideView.getStateManager().getCurrentState() instanceof SelectState) || (
                ((SelectState) slideView.getStateManager().getCurrentState()).getSelectedElements().isEmpty()
                )){
            ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                    "NOTHING IS SELECTED TO BE DELETED", MessageType.WARNING, slideView
            ));
        } else {
            slideView.getStateManager().setDeleteState();
            ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                    "ENTERED IN 'DELETE' STATE", MessageType.INFO, slideView
            ));

            // Vrati se u Select state odma nakon toga
            slideView.getStateManager().setSelectState();
            ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                    "RETURNED To 'SELECT' STATE", MessageType.INFO, slideView
            ));
        }
    }
}
