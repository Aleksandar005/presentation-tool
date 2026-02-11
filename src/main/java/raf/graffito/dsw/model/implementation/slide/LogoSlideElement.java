package raf.graffito.dsw.model.implementation.slide;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.prototype.Prototype;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

// LogoSlideElement predstavlja logo element na slajdu.
@Getter
@Setter
public class LogoSlideElement extends SlideElement {

    private Color primaryColor;
    private Color secondaryColor;

    public LogoSlideElement(GraffNode parent, String title, Point location,
                            Dimension dimension, Color primaryColor) {
        super(parent, title, location, dimension);
        this.primaryColor = primaryColor;
        this.secondaryColor = primaryColor.brighter();
    }

    // Privatni konstruktor za kloniranje.
    private LogoSlideElement(GraffNode parent, String title, Point location, Dimension dimension,
                             double rotation, Color primaryColor, Color secondaryColor) {
        super(parent, title, new Point(location), new Dimension(dimension));
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.rotation = rotation;
    }

    // Prototype pattern - kreira duboku kopiju elementa.
    @Override
    public Prototype clone() {
        // Kopiramo originalnu lokaciju - PasteState će primeniti offset
        Point newLocation = new Point(location);

        LogoSlideElement copy = new LogoSlideElement(
                getParent(),
                getTitle() + " (Copy)",
                newLocation,
                new Dimension(dimension),
                this.rotation,
                new Color(primaryColor.getRGB()),
                new Color(secondaryColor.getRGB())
        );

        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                "Kloniran LogoSlideElement: " + getTitle() + " -> " + copy.getTitle(), MessageType.INFO, this
        ));
        return copy;
    }

    public void setPrimaryColor(Color primaryColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = primaryColor.brighter();
    }
}