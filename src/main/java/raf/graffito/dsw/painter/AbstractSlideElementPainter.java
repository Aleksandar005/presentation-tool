package raf.graffito.dsw.painter;

import lombok.Getter;
import raf.graffito.dsw.model.implementation.slide.SlideElement;

import java.awt.*;
import java.awt.geom.AffineTransform;

// Apstraktna bazna klasa za sve paintere.
public abstract class AbstractSlideElementPainter implements SlideElementPainter {
    @Getter
    public SlideElement element;
    @Getter
    public Shape shape;

    public AbstractSlideElementPainter(SlideElement element) {
        this.element = element;
        updateShape();
    }

    @Override
    public boolean elementAt(Point p) {
        return shape != null && shape.contains(p);
    }

    public void applyTransformations(Graphics2D g2d) {
        SlideElement elem = getElement();
        Point loc = elem.getLocation();
        Dimension dim = elem.getDimension();
        double rotation = elem.getRotation();

        // Ako ima rotaciju, primeni je
        if (rotation != 0.0) {
            // Rotiraj oko centra elementa
            int centerX = loc.x + dim.width / 2;
            int centerY = loc.y + dim.height / 2;
            g2d.rotate(Math.toRadians(rotation), centerX, centerY);
        }
    }

    public AffineTransform saveTransform(Graphics2D g2d) {
        return g2d.getTransform();
    }

    public void restoreTransform(Graphics2D g2d, AffineTransform original) {
        g2d.setTransform(original);
    }

    // Ažurira geometrijski oblik elementa na osnovu trenutne lokacije i dimenzija.
    public void updateShape() {
        Point loc = element.getLocation();
        Dimension dim = element.getDimension();
        this.shape = new Rectangle(loc.x, loc.y, dim.width, dim.height);
    }
}