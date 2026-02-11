package raf.graffito.dsw.controller.slideElementActions;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.state.concrete.ZoomState;

import java.awt.event.ActionEvent;


public class ZoomSlideElementAction extends AbstractGraffAction {
    private final SlideView slideView;

    public ZoomSlideElementAction(SlideView slideView) {
        super("Zoom", "Zoom in/out with mouse wheel");
        this.slideView = slideView;
    }

    public void actionPerformed(ActionEvent e) {
        // Aktiviraj ZoomState
        if(slideView.getStateManager().getCurrentState() instanceof ZoomState) {
            slideView.getStateManager().setNullState();
            ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                    "LEFT THE 'ZOOM' STATE - Use mouse wheel", MessageType.INFO, slideView
            ));
        } else {
            slideView.getStateManager().setZoomState();
            ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                    "ENTERED IN 'ZOOM' STATE - Use mouse wheel", MessageType.INFO, slideView
            ));
        }


    }
}