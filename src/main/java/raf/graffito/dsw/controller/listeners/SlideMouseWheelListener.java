package raf.graffito.dsw.controller.listeners;

import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.state.concrete.ZoomState;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

// Zoom sa wheel scroll-om.
public class SlideMouseWheelListener implements MouseWheelListener {

    private final SlideView slideView;
    private static final double ZOOM_STEP = 0.1;

    public SlideMouseWheelListener(SlideView slideView) {
        this.slideView = slideView;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (!(slideView.getStateManager().getCurrentState() instanceof ZoomState)) {
            return; // Ignoriši wheel ako nije Zoom state
        }
        int rotation = e.getWheelRotation();
        double currentZoom = slideView.getZoomLevel();
        double newZoom;

        if (rotation < 0) {
            newZoom = currentZoom + ZOOM_STEP; // Zoom in
        } else {
            newZoom = currentZoom - ZOOM_STEP; // Zoom out
        }

        slideView.setZoomLevel(newZoom);
    }
}