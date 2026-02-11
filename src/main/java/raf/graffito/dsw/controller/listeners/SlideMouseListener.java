package raf.graffito.dsw.controller.listeners;

import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.state.StateManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Delegira mouse eventi na trenutni State.
public class SlideMouseListener implements MouseListener {

    private final StateManager stateManager;
    private final SlideView slideView;

    public SlideMouseListener(StateManager stateManager, SlideView slideView) {
        this.stateManager = stateManager;
        this.slideView = slideView;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point slidePoint = slideView.screenToSlideCoordinates(e.getPoint());
        Slide currentSlide = slideView.getCurrentSlide();

        if (currentSlide != null) {
            boolean needsRepaint = stateManager.getCurrentState().handleMousePressed(slidePoint, currentSlide);

            if (needsRepaint) {
                slideView.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point slidePoint = slideView.screenToSlideCoordinates(e.getPoint());
        Slide currentSlide = slideView.getCurrentSlide();

        if (currentSlide != null) {
            boolean needsRepaint = stateManager.getCurrentState().handleMouseReleased(slidePoint, currentSlide);

            if (needsRepaint) {
                slideView.repaint();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point slidePoint = slideView.screenToSlideCoordinates(e.getPoint());
        Slide currentSlide = slideView.getCurrentSlide();

        if (currentSlide != null) {
            boolean needsRepaint = stateManager.getCurrentState().handleMouseClicked(slidePoint, currentSlide);

            if (needsRepaint) {
                slideView.repaint();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}