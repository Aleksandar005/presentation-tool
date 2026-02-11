package raf.graffito.dsw.painter;

import raf.graffito.dsw.model.implementation.slide.LogoSlideElement;
import raf.graffito.dsw.model.implementation.slide.SlideElement;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Painter za crtanje loga na slajdu.
 * Logo se crta pomoću geometrijskih oblika (primitiva).
 */
public class LogoSlideElementPainter extends AbstractSlideElementPainter {

    private LogoSlideElement logoElement;

    public LogoSlideElementPainter(SlideElement element) {
        super(element);
        this.logoElement = (LogoSlideElement) element;
        updateShape();
    }

    @Override
    public void paint(Graphics2D g) {
        Point loc = logoElement.getLocation();
        Dimension dim = logoElement.getDimension();

        // Sačuvaj original state
        Graphics2D g2 = (Graphics2D) g.create();

        // Anti-aliasing za lepši prikaz
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Sačuvaj originalni transform
        AffineTransform originalTransform = saveTransform(g2);

        // Primeni transformacije (rotaciju)
        applyTransformations(g2);

        // Crtaj CIRCLE_STAR logo
        drawCircleStarLogo(g2, loc, dim);

        // Vrati originalni transform
        restoreTransform(g2, originalTransform);

        g2.dispose();
    }


    // Crta logo kao krug sa zvezdicom u sredini.
    private void drawCircleStarLogo(Graphics2D g, Point loc, Dimension dim) {
        int centerX = loc.x + dim.width / 2;
        int centerY = loc.y + dim.height / 2;
        int radius = Math.min(dim.width, dim.height) / 2;

        // Napravi gradijent od primarne ka sekundarnoj boji
        GradientPaint gradient = new GradientPaint(
                loc.x, loc.y, logoElement.getPrimaryColor(),
                loc.x + dim.width, loc.y + dim.height, logoElement.getSecondaryColor()
        );
        g.setPaint(gradient);

        // Nacrtaj krug
        g.fillOval(loc.x, loc.y, dim.width, dim.height);

        // Nacrtaj border
        g.setColor(logoElement.getPrimaryColor().darker());
        g.setStroke(new BasicStroke(3));
        g.drawOval(loc.x, loc.y, dim.width, dim.height);

        // Nacrtaj zvezdicu u sredini
        g.setColor(Color.WHITE);
        drawStar(g, centerX, centerY, (int)(radius * 0.6));
    }

    /**
     * Pomoćna metoda za crtanje petokrake zvezde.
     */
    private void drawStar(Graphics2D g, int centerX, int centerY, int radius) {
        int[] xPoints = new int[10];
        int[] yPoints = new int[10];

        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 2 + (2 * Math.PI * i) / 10;
            int r = (i % 2 == 0) ? radius : radius / 2;
            xPoints[i] = centerX + (int)(r * Math.cos(angle));
            yPoints[i] = centerY - (int)(r * Math.sin(angle));
        }

        g.fillPolygon(xPoints, yPoints, 10);
    }

}