package raf.graffito.dsw.state.concrete;

import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.state.State;
import java.awt.*;


public class ZoomState implements State {

    @Override
    public boolean handleMousePressed(Point slidePoint, Slide currentSlide) {
        return false;
    }

    @Override
    public boolean handleMouseDragged(Point slidePoint, Slide currentSlide) {
        return false;
    }

    @Override
    public boolean handleMouseReleased(Point slidePoint, Slide currentSlide) {
        return false;
    }

    @Override
    public boolean handleMouseClicked(Point slidePoint, Slide currentSlide) {
        return false;
    }
}