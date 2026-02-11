package raf.graffito.dsw.state;

import raf.graffito.dsw.model.implementation.slide.Slide;
import java.awt.Point;

// State interfejs za različite načine rada aplikacije.
public interface State {

    boolean handleMousePressed(Point slidePoint, Slide currentSlide);

    boolean handleMouseDragged(Point slidePoint, Slide currentSlide);

    boolean handleMouseReleased(Point slidePoint, Slide currentSlide);

    boolean handleMouseClicked(Point slidePoint, Slide currentSlide);

    default void onStateActivated() { }

    default void onStateDeactivated() { }
}