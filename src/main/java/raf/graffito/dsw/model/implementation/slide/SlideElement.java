package raf.graffito.dsw.model.implementation.slide;

import raf.graffito.dsw.model.graff.GraffLeaf;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.prototype.Prototype;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;


@Getter
@Setter
public abstract class SlideElement extends GraffLeaf implements Prototype {

    protected Point location;
    protected Dimension dimension;
    protected double rotation; // Rotacija u stepenima (0-360)

    public SlideElement(GraffNode parent, String title, Point location, Dimension dimension) {
        super(parent, title);
        this.location = location;
        this.dimension = dimension;
        this.rotation = 0.0;
    }

    // Pomera element za zadati offset.
    public void move(int dx, int dy) {
        location.translate(dx, dy);
    }

    // Rotira element za zadati ugao (u stepenima).
    public void rotate(double angle) {
        this.rotation = (this.rotation + angle) % 360;
        if (this.rotation < 0) {
            this.rotation += 360;
        }
    }

    // Skalira element (promeni dimenzije).
    public void scale(double factor) {
        int newWidth = (int)(dimension.width * factor);
        int newHeight = (int)(dimension.height * factor);
        dimension = new Dimension(newWidth, newHeight);
    }


    @Override
    public abstract Prototype clone();
}