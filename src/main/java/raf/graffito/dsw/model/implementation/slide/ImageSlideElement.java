package raf.graffito.dsw.model.implementation.slide;

import lombok.Setter;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.prototype.Prototype;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

// ImageSlideElement predstavlja sliku na slajdu.
@Getter
@Setter
public class ImageSlideElement extends SlideElement {

    private BufferedImage image;
    private String path;

    public ImageSlideElement(GraffNode parent, String title, BufferedImage image, Point location) {
        super(parent, title, location, new Dimension(image.getWidth(), image.getHeight()));
        this.image = image;
    }

    // Privatni konstruktor za kloniranje.
    private ImageSlideElement(GraffNode parent, String title, BufferedImage image,
                              Point location, Dimension dimension, double rotation, String path) {
        super(parent, title, new Point(location), new Dimension(dimension));
        this.image = image;
        this.rotation = rotation;
        this.path = path;
    }

    // Prototype pattern - kreira duboku kopiju elementa.
    @Override
    public Prototype clone() {
        // Kopiramo originalnu lokaciju - PasteState će primeniti offset
        Point newLocation = new Point(location);

        // DEEP COPY slike
        BufferedImage copiedImage = deepCopyImage(this.image);

        ImageSlideElement copy = new ImageSlideElement(
                getParent(),
                getTitle() + " (Copy)",
                copiedImage,
                newLocation,
                new Dimension(dimension),
                this.rotation,
                this.path
        );

        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                "Kloniran ImageSlideElement: " + getTitle() + " -> " + copy.getTitle(), MessageType.INFO, this
        ));
        return copy;
    }

    // Kreira potpuno novu instancu slike sa kopiranim podacima - deep copy.
    private BufferedImage deepCopyImage(BufferedImage source) {
        ColorModel cm = source.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = source.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.dimension = new Dimension(image.getWidth(), image.getHeight());
    }
}