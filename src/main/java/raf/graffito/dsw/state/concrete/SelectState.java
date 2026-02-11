package raf.graffito.dsw.state.concrete;

import lombok.Getter;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.model.implementation.slide.SlideElement;
import raf.graffito.dsw.state.State;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SelectState implements State {

    @Getter
    private List<SlideElement> selectedElements = new ArrayList<>();
    private Point selectionStart, selectionEnd;
    @Getter
    private boolean isMultiSelecting = false;

    @Override
    public boolean handleMousePressed(Point slidePoint, Slide currentSlide) {
        SlideElement clickedElement = findElementAt(slidePoint, currentSlide);

        if (clickedElement != null) {
            selectedElements.clear();
            selectedElements.add(clickedElement);
            isMultiSelecting = false;
        } else {
            selectionStart = new Point(slidePoint);
            selectionEnd = new Point(slidePoint);
            isMultiSelecting = true;
            selectedElements.clear();
        }
        return true; // treba repaint
    }

    @Override
    public boolean handleMouseDragged(Point slidePoint, Slide currentSlide) {
        if (!isMultiSelecting) return false;

        selectionEnd = new Point(slidePoint);
        selectedElements.clear();
        Rectangle rect = createSelectionRectangle();

        for (GraffNode child : currentSlide.getChildren()) {
            if (child instanceof SlideElement && intersects((SlideElement)child, rect)) {
                selectedElements.add((SlideElement) child);
            }
        }
        return true;  // treba repaint
    }

    @Override
    public boolean handleMouseReleased(Point slidePoint, Slide currentSlide) {
        isMultiSelecting = false;
        return true; // treba repaint
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
            if (contains(elements.get(i), p)) return elements.get(i);
        }
        return null;
    }

    private boolean contains(SlideElement elem, Point p) {
        Point loc = elem.getLocation();
        Dimension dim = elem.getDimension();
        return p.x >= loc.x && p.x <= loc.x + dim.width && p.y >= loc.y && p.y <= loc.y + dim.height;
    }

    public Rectangle createSelectionRectangle() {
        if (selectionStart == null || selectionEnd == null) return null;

        int x = Math.min(selectionStart.x, selectionEnd.x);
        int y = Math.min(selectionStart.y, selectionEnd.y);
        return new Rectangle(x, y, Math.abs(selectionEnd.x - selectionStart.x),
                Math.abs(selectionEnd.y - selectionStart.y));
    }

    private boolean intersects(SlideElement elem, Rectangle rect) {
        Point loc = elem.getLocation();
        Dimension dim = elem.getDimension();
        return new Rectangle(loc.x, loc.y, dim.width, dim.height).intersects(rect);
    }

    @Override
    public void onStateDeactivated() {
        isMultiSelecting = false;
    }

    public void clearSelection() {
        selectedElements.clear();
        isMultiSelecting = false;
    }
}