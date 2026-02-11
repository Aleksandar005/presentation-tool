package raf.graffito.dsw.state.concrete;

import lombok.Setter;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.model.implementation.slide.SlideElement;
import raf.graffito.dsw.state.State;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResizeState implements State {

    @Setter
    private List<SlideElement> elementsToResize = new ArrayList<>();
    private Point startDragPoint;
    private Map<SlideElement, Dimension> originalDimensions = new HashMap<>();
    private boolean resizing = false;
    private boolean stateSaved = false;

    @Override
    public boolean handleMousePressed(Point slidePoint, Slide currentSlide) {
        if (elementsToResize.isEmpty()) return false;

        // Proveri da li je kliknuto na neki od selektovanih
        resizing = true;
        startDragPoint = new Point(slidePoint);
        stateSaved = false;

        // Sačuvaj originalne dimenzije
        originalDimensions.clear();
        for (SlideElement element : elementsToResize) {
            originalDimensions.put(element, new Dimension(element.getDimension()));
        }

        return true;
    }

    @Override
    public boolean handleMouseDragged(Point slidePoint, Slide currentSlide) {
        if (!resizing || elementsToResize.isEmpty()) return false;

        if (!stateSaved) {
            ApplicationFramework.getInstance().getCommandManager().saveStateBeforeChange();
            stateSaved = true;
        }

        // Za scale factor
        int dx = slidePoint.x - startDragPoint.x;
        int dy = slidePoint.y - startDragPoint.y;

        // Prosecno prevlacenje
        double avgDrag = (dx + dy) / 2.0;
        double scaleFactor = 1.0 + (avgDrag / 100.0);

        // Ogranici scale (0.2x - 5x)
        scaleFactor = Math.max(0.2, Math.min(5.0, scaleFactor));

        // Primerni scale
        for (SlideElement element : elementsToResize) {
            Dimension original = originalDimensions.get(element);
            if (original != null) {
                int newWidth = (int)(original.width * scaleFactor);
                int newHeight = (int)(original.height * scaleFactor);

                // Minimum dimenzije
                newWidth = Math.max(20, newWidth);
                newHeight = Math.max(20, newHeight);

                // MAKSIMALNE DIMENZIJE - NE SME IZAĆI IZVAN SLAYDA
                Point loc = element.getLocation();

                // Ne sme da bude širi od slajda - X pozicija
                if (loc.x + newWidth > 800) { // SLIDE_WIDTH
                    newWidth = 800 - loc.x;
                }

                // Ne sme da bude viši od slajda - Y pozicija
                if (loc.y + newHeight > 600) { // SLIDE_HEIGHT
                    newHeight = 600 - loc.y;
                }

                element.setDimension(new Dimension(newWidth, newHeight));
            }
        }

        return true; // Repaint
    }

    @Override
    public boolean handleMouseReleased(Point slidePoint, Slide currentSlide) {
        resizing = false;
        originalDimensions.clear();
        stateSaved = false;

        if (!elementsToResize.isEmpty()) {
            currentSlide.notifyElementChanged();
        }

        return false;
    }

    @Override
    public boolean handleMouseClicked(Point slidePoint, Slide currentSlide) {
        return false;
    }

    private SlideElement findElementAt(Point p, Slide slide) {
        List<SlideElement> elements = new ArrayList<>();
        for (var child : slide.getChildren()) {
            if (child instanceof SlideElement) elements.add((SlideElement) child);
        }
        for (int i = elements.size() - 1; i >= 0; i--) {
            Point loc = elements.get(i).getLocation();
            Dimension dim = elements.get(i).getDimension();
            if (p.x >= loc.x && p.x <= loc.x + dim.width && p.y >= loc.y && p.y <= loc.y + dim.height) {
                return elements.get(i);
            }
        }
        return null;
    }

    @Override
    public void onStateDeactivated() {
        resizing = false;
        originalDimensions.clear();
        stateSaved = false;
    }
}