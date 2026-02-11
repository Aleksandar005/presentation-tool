package raf.graffito.dsw.controller.listeners;

import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.state.StateManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class SlideMouseMotionListener implements MouseMotionListener {

    private final StateManager stateManager;
    private final SlideView slideView;

    public SlideMouseMotionListener(StateManager stateManager, SlideView slideView) {
        this.stateManager = stateManager;
        this.slideView = slideView;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point slidePoint = slideView.screenToSlideCoordinates(e.getPoint());
        Slide currentSlide = slideView.getCurrentSlide();

        if (currentSlide != null) {
            boolean needsRepaint = stateManager.getCurrentState().handleMouseDragged(slidePoint, currentSlide);

            if (needsRepaint) {
                slideView.repaint();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) { }
}