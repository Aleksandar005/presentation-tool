package raf.graffito.dsw.state.concrete;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.implementation.slide.LogoSlideElement;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.state.State;
import raf.graffito.dsw.strategy.SpaceChecker;
import raf.graffito.dsw.util.ElementNameGenerator;

import javax.swing.*;
import java.awt.*;

public class AddLogoState implements State {

    private static final int DEFAULT_SIZE = 80;
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);

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
        // PROVERA PROSTORA - koristi Strategy pattern
        SpaceChecker spaceChecker = ApplicationFramework.getInstance().getSpaceChecker();
        if (!spaceChecker.hasEnoughSpace(currentSlide)) {
            ApplicationFramework.getInstance().getMessageGenerator()
                    .notify(new Message(spaceChecker.getErrorMessage(currentSlide), MessageType.ERROR, currentSlide));
            return false;
        }

        // CENTRIRANJE - slidePoint je centar loga
        Point centeredLocation = new Point(
                slidePoint.x - DEFAULT_SIZE / 2,
                slidePoint.y - DEFAULT_SIZE / 2
        );

        Dimension dimension = new Dimension(DEFAULT_SIZE, DEFAULT_SIZE);

        // PROVERA GRANICA - da li je logo unutar slajda?
        if (!isWithinBounds(centeredLocation, dimension)) {
            ApplicationFramework.getInstance().getMessageGenerator()
                    .notify(new Message("LOGO WOULD BE OUT OF SLIDE BOUNDS", MessageType.WARNING, currentSlide));
            return false;
        }

        ApplicationFramework.getInstance().getCommandManager().saveStateBeforeChange();

        LogoSlideElement element = new LogoSlideElement(
                currentSlide,
                ElementNameGenerator.nextLogoName(currentSlide),
                centeredLocation,  // Centrirano!
                dimension,
                PRIMARY_COLOR
        );

        currentSlide.addChild(element);
        return false;
    }
    private boolean isWithinBounds(Point location, Dimension dimension) {
        // Proveri da li je gornji levi ugao unutar
        if (location.x < 0 || location.y < 0) {
            return false;
        }

        // Proveri da li je donji desni ugao unutar
        if (location.x + dimension.width > SlideView.SLIDE_WIDTH ||
                location.y + dimension.height > SlideView.SLIDE_HEIGHT) {
            return false;
        }

        return true;
    }

    @Override
    public void onStateActivated() {
        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                "[AddLogoState] Activated",  MessageType.INFO, this
        ));
    }
}