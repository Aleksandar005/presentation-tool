package raf.graffito.dsw.painter;

import java.awt.*;

// Interface koji definiše operacije za crtanje elemenata na slajdu.
public interface SlideElementPainter {
    // Crta element
    void paint(Graphics2D g);

    // Proverava da li se element nalazi na datoj tački.
    boolean elementAt(Point p);
}