package raf.graffito.dsw.painter;

import raf.graffito.dsw.model.implementation.slide.ImageSlideElement;
import raf.graffito.dsw.model.implementation.slide.SlideElement;

import java.awt.*;
import java.awt.geom.AffineTransform;

// Painter za crtanje slika na slajdu.
public class ImageSlideElementPainter extends AbstractSlideElementPainter {

    private ImageSlideElement imageElement;

    public ImageSlideElementPainter(SlideElement element) {
        super(element);
        this.imageElement = (ImageSlideElement) element;
        updateShape();
    }

    @Override
    public void paint(Graphics2D g) {
        Point loc = imageElement.getLocation();
        Dimension dim = imageElement.getDimension();

        // Sačuvaj originalni transform
        AffineTransform originalTransform = saveTransform(g);

        // Primeni transformacije (rotaciju)
        applyTransformations(g);

        // Crtanje slike
        g.drawImage(
                imageElement.getImage(),
                loc.x,
                loc.y,
                dim.width,
                dim.height,
                null
        );

        // Vrati originalni transform
        restoreTransform(g, originalTransform);
    }
}