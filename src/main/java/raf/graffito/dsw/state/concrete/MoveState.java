package raf.graffito.dsw.state.concrete;

import lombok.Setter;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.model.implementation.slide.SlideElement;
import raf.graffito.dsw.state.State;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MoveState implements State {

    @Setter
    private List<SlideElement> elementsToMove = new ArrayList<>();
    private Point startDragPoint;
    private boolean dragging = false;
    private boolean stateSaved = false;

    @Override
    public boolean handleMousePressed(Point slidePoint, Slide currentSlide) {
        dragging = true;
        startDragPoint = new Point(slidePoint);
        stateSaved = false;
        return true; // Repaint
    }

    @Override
    public boolean handleMouseDragged(Point slidePoint, Slide currentSlide) {
        if (!dragging || elementsToMove.isEmpty()) return false;

        // Pomeri SVE selektovane elemente
        int dx = slidePoint.x - startDragPoint.x;
        int dy = slidePoint.y - startDragPoint.y;

        if (dx == 0 && dy == 0) return false;

        // Sačuvaj stanje samo jednom na početku drag-a
        if (!stateSaved) {
            ApplicationFramework.getInstance().getCommandManager().saveStateBeforeChange();
            stateSaved = true;
        }

        // Prvo proveri da li moze da se pomera
        boolean canMove = true;
        for (SlideElement element : elementsToMove) {
            Point loc = element.getLocation();
            Dimension dim = element.getDimension();

            int newX = loc.x + dx;
            int newY = loc.y + dy;

            if (newX < 0 || newY < 0 ||
                    newX + dim.width > 800 ||
                    newY + dim.height > 600) {
                canMove = false;
                break;
            }
        }

        // Ako moze, pomeri sve
        if (canMove) {
            for (SlideElement element : elementsToMove) {
                Point loc = element.getLocation();
                element.setLocation(new Point(loc.x + dx, loc.y + dy));
            }
            startDragPoint = new Point(slidePoint);
            return true;
        }

        startDragPoint = new Point(slidePoint);
        currentSlide.notifyElementChanged();

        return true; // Repaint
    }


    @Override
    public boolean handleMouseReleased(Point slidePoint, Slide currentSlide) {
        dragging = false;
        stateSaved = false;

        if (!elementsToMove.isEmpty()) {
            currentSlide.notifyElementChanged();
        }

        return false;
    }

    @Override
    public boolean handleMouseClicked(Point slidePoint, Slide currentSlide) {
        return false;
    }

    @Override
    public void onStateDeactivated() {
        dragging = false;
        stateSaved = false;
        elementsToMove.clear();
    }

    private void moveElementsWithBounds(int dx, int dy) {
        for (SlideElement element : elementsToMove) {
            Point currentLoc = element.getLocation();
            Dimension dim = element.getDimension();

            // Računaj novu poziciju
            int newX = currentLoc.x + dx;
            int newY = currentLoc.y + dy;

            // Ograniči na granice slajda
            newX = Math.max(0, Math.min(newX, SlideView.SLIDE_WIDTH - dim.width));
            newY = Math.max(0, Math.min(newY, SlideView.SLIDE_HEIGHT - dim.height));

            // Pomeri element na ograničenu poziciju
            element.setLocation(new Point(newX, newY));
        }
    }
}