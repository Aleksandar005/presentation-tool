package raf.graffito.dsw.painter;

import raf.graffito.dsw.model.implementation.slide.SlideElement;
import raf.graffito.dsw.model.implementation.slide.TextSlideElement;

import java.awt.*;
import java.awt.geom.AffineTransform;

// Painter za crtanje teksta na slajdu.
public class TextSlideElementPainter extends AbstractSlideElementPainter {

    private TextSlideElement textElement;

    public TextSlideElementPainter(SlideElement element) {
        super(element);
        this.textElement = (TextSlideElement) element;
        initializeDimensions();
        updateShape();
    }

    // Inicijalizuje dimenzije teksta samo jednom pri kreiranju.
    private void initializeDimensions() {
        // Ako dimenzije još nisu postavljene (podrazumevana vrednost 100x30)
        Dimension currentDim = textElement.getDimension();
        if (currentDim.width == 100 && currentDim.height == 30) {
            // Izračunaj prirodne dimenzije teksta
            FontMetrics fm = new Canvas().getFontMetrics(textElement.getFont());
            int width = fm.stringWidth(textElement.getText());
            int height = fm.getHeight();
            textElement.setDimension(new Dimension(width, height));
        }
    }

    @Override
    public void paint(Graphics2D g) {
        Point loc = textElement.getLocation();
        Dimension dim = textElement.getDimension();

        // Sačuvaj originalni transform
        AffineTransform originalTransform = saveTransform(g);

        // Anti-aliasing za lepši tekst
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Primeni transformacije (rotaciju)
        applyTransformations(g);

        // Izračunaj prirodne dimenzije teksta za skaliranje
        FontMetrics fm = g.getFontMetrics(textElement.getFont());
        int naturalWidth = fm.stringWidth(textElement.getText());
        int naturalHeight = fm.getHeight();
        int ascent = fm.getAscent();

        // Izračunaj faktor skaliranja
        double scaleX = (double) dim.width / naturalWidth;
        double scaleY = (double) dim.height / naturalHeight;

        // Primeni skaliranje i crtaj tekst
        AffineTransform beforeScale = g.getTransform();
        g.translate(loc.x, loc.y);
        g.scale(scaleX, scaleY);

        g.setFont(textElement.getFont());
        g.setColor(textElement.getColor());
        g.drawString(textElement.getText(), 0, ascent);

        g.setTransform(beforeScale);

        // Vrati originalni transform
        restoreTransform(g, originalTransform);

        updateShape();
    }
}