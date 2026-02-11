package raf.graffito.dsw.controller.slideElementActions;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.model.implementation.slide.SlideElement;
import raf.graffito.dsw.state.concrete.SelectState;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

// Akcija za rotaciju selektovanih elemenata za 90 stepeni udesno (clockwise).
public class RotateRightAction extends AbstractGraffAction {
    private final SlideView slideView;

    public RotateRightAction(SlideView slideView) {
        super("Rotate Right", "Rotate selected elements 90° right");
        this.slideView = slideView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!slideView.getStateManager().isCurrentStateSelectState()) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                    "SELECT STATE MUST BE ACTIVE TO ROTATE", MessageType.WARNING, slideView
            ));
            return;
        }

        SelectState selectState = (SelectState) slideView.getStateManager().getCurrentState();
        List<SlideElement> selectedElements = selectState.getSelectedElements();

        if (selectedElements.isEmpty()) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                    "NO ELEMENTS SELECTED", MessageType.WARNING, slideView
            ));
            return;
        }

        // Proveri da li će svi elementi ostati unutar slajda nakon rotacije
        for (SlideElement element : selectedElements) {
            if (!canRotate(element, 90)) {
                ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                        "ROTATION WOULD MOVE ELEMENT OUTSIDE SLIDE BOUNDS", MessageType.WARNING, slideView
                ));
                return;
            }
        }

        ApplicationFramework.getInstance().getCommandManager().saveStateBeforeChange();

        for (SlideElement element : selectedElements) {
            element.rotate(90);
        }

        Slide currentSlide = slideView.getCurrentSlide();
        if (currentSlide != null) {
            currentSlide.notifyElementChanged();
        }

        slideView.repaint();
    }

    // Proverava da li element može biti rotiran a da ostane unutar slajda.
    private boolean canRotate(SlideElement element, double angleDegrees) {
        Point loc = element.getLocation();
        Dimension dim = element.getDimension();
        double currentRotation = element.getRotation();
        double newRotation = currentRotation + angleDegrees;

        // Izračunaj centar elementa
        double centerX = loc.x + dim.width / 2.0;
        double centerY = loc.y + dim.height / 2.0;

        // Izračunaj sve četiri temena rotiranog pravougaonika
        double[][] corners = {
                {loc.x, loc.y},
                {loc.x + dim.width, loc.y},
                {loc.x + dim.width, loc.y + dim.height},
                {loc.x, loc.y + dim.height}
        };

        double radians = Math.toRadians(newRotation);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        for (double[] corner : corners) {
            // Translacija do centra
            double dx = corner[0] - centerX;
            double dy = corner[1] - centerY;

            // Rotacija
            double rotatedX = centerX + dx * cos - dy * sin;
            double rotatedY = centerY + dx * sin + dy * cos;

            // Provera granica
            if (rotatedX < 0 || rotatedX > SlideView.SLIDE_WIDTH ||
                    rotatedY < 0 || rotatedY > SlideView.SLIDE_HEIGHT) {
                return false;
            }
        }

        return true;
    }
}