package raf.graffito.dsw.state.concrete;

import raf.graffito.dsw.clipboard.ClipboardManager;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.model.implementation.slide.SlideElement;
import raf.graffito.dsw.state.State;
import raf.graffito.dsw.strategy.SpaceChecker;

import java.awt.*;
import java.util.List;

// PasteState - kada je aktivan, klik na slajd paste-uje kopirane elemente.
public class PasteState implements State {

    @Override
    public boolean handleMousePressed(Point slidePoint, Slide currentSlide) {
        return false;
    }

    @Override
    public boolean handleMouseDragged(Point slidePoint, Slide currentSlide) {
        return false;
    }

    @Override
    public boolean handleMouseReleased(Point slidePoint, Slide currentSlide) {
        return false;
    }

    @Override
    public boolean handleMouseClicked(Point slidePoint, Slide currentSlide) {
        if (currentSlide == null) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("Nije selektovan nijedan slajd!", MessageType.WARNING, this)
            );
            return false;
        }

        ClipboardManager clipboard = ApplicationFramework.getInstance().getClipboardManager();

        if (!clipboard.hasContent()) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("Clipboard je prazan! Prvo kopirajte elemente.", MessageType.WARNING, this)
            );
            return false;
        }

        // provera prostora - koristi Strategy pattern
        if (!ApplicationFramework.getInstance().getSpaceChecker().hasEnoughSpace(currentSlide)) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message(ApplicationFramework.getInstance().getSpaceChecker().getErrorMessage(currentSlide), MessageType.ERROR, currentSlide)
            );
            return false;
        }

        // Dobavi klonove za paste
        List<SlideElement> elementsToPaste = clipboard.getClonedElementsForPaste();

        // Izračunaj bounding box svih elemenata (za centriranje grupe)
        Rectangle boundingBox = calculateBoundingBox(elementsToPaste);

        // Offset za pomeranje - centar bounding box-a treba da bude na poziciji klika
        int offsetX = slidePoint.x - (boundingBox.x + boundingBox.width / 2);
        int offsetY = slidePoint.y - (boundingBox.y + boundingBox.height / 2);

        // Proveri da li svi elementi staju u slajd nakon pomeranja
        for (SlideElement element : elementsToPaste) {
            Point newLocation = new Point(
                    element.getLocation().x + offsetX,
                    element.getLocation().y + offsetY
            );

            if (!isWithinBounds(newLocation, element.getDimension())) {
                ApplicationFramework.getInstance().getMessageGenerator().notify(
                        new Message("Elementi ne mogu biti paste-ovani van granica slajda!",
                                MessageType.ERROR, this)
                );
                return false;
            }
        }

        // Sačuvaj stanje pre promene (za Undo)
        ApplicationFramework.getInstance().getCommandManager().saveStateBeforeChange();

        // Postavi elemente na novu poziciju i dodaj ih na slajd
        for (SlideElement element : elementsToPaste) {
            Point newLocation = new Point(
                    element.getLocation().x + offsetX,
                    element.getLocation().y + offsetY
            );
            element.setLocation(newLocation);
            element.setParent(currentSlide);
            currentSlide.addChild(element);
        }

        ApplicationFramework.getInstance().getMessageGenerator().notify(
                new Message("Paste-ovano " + elementsToPaste.size() + " elemenata",
                        MessageType.INFO, currentSlide)
        );

        return true; // Repaint
    }

    // Izračunava bounding box koji obuhvata sve elemente.
    private Rectangle calculateBoundingBox(List<SlideElement> elements) {
        if (elements.isEmpty()) {
            return new Rectangle(0, 0, 0, 0);
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (SlideElement element : elements) {
            Point loc = element.getLocation();
            Dimension dim = element.getDimension();

            minX = Math.min(minX, loc.x);
            minY = Math.min(minY, loc.y);
            maxX = Math.max(maxX, loc.x + dim.width);
            maxY = Math.max(maxY, loc.y + dim.height);
        }

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    // Proverava da li element staje u granice slajda.
    private boolean isWithinBounds(Point location, Dimension dimension) {
        if (location.x < 0 || location.y < 0) {
            return false;
        }

        return location.x + dimension.width <= SlideView.SLIDE_WIDTH &&
                location.y + dimension.height <= SlideView.SLIDE_HEIGHT;
    }

    @Override
    public void onStateActivated() {
    }

    @Override
    public void onStateDeactivated() {
    }
}