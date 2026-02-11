package raf.graffito.dsw.command.snapshots;

import raf.graffito.dsw.model.implementation.slide.SlideElement;

import java.awt.*;

public class ElementSnapshot {
    SlideElement element;
    Point location;
    Dimension dimension;
    double rotation;

    ElementSnapshot(SlideElement element, Point location, Dimension dimension, double rotation) {
        this.element = element;
        this.location = location;
        this.dimension = dimension;
        this.rotation = rotation;
    }

    void restore(){
        element.setLocation(location);
        element.setDimension(dimension);
        double currentRotation = rotation;
        element.setRotation(rotation - currentRotation);
    }
}
