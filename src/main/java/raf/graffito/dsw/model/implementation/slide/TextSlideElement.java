package raf.graffito.dsw.model.implementation.slide;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.prototype.Prototype;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

// TextSlideElement predstavlja tekstualni element na slajdu.
@Getter
@Setter
public class TextSlideElement extends SlideElement {

    private String text;
    private Font font;
    private Color color;

    public TextSlideElement(GraffNode parent, String title, String text, Point location, Font font, Color color) {
        super(parent, title, location, new Dimension(100, 30)); // Privremena dimenzija
        this.text = text;
        this.font = font;
        this.color = color;
    }

    // Privatni konstruktor za kloniranje.
    private TextSlideElement(GraffNode parent, String title, String text, Point location,
                             Dimension dimension, double rotation, Font font, Color color) {
        super(parent, title, new Point(location), new Dimension(dimension));
        this.text = text;
        this.font = font;
        this.color = color;
        this.rotation = rotation;
    }

    // Prototype pattern - kreira duboku kopiju elementa.
    @Override
    public Prototype clone() {
        // Kopiramo originalnu lokaciju - PasteState će primeniti offset
        Point newLocation = new Point(location);

        TextSlideElement copy = new TextSlideElement(
                getParent(),
                getTitle() + " (Copy)",
                this.text,
                newLocation,
                new Dimension(dimension),
                this.rotation,
                this.font,
                new Color(color.getRGB())
        );

        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                "Kloniran TextSlideElement: " + getTitle() + " -> " + copy.getTitle(), MessageType.INFO, this
        ));
        return copy;
    }
}