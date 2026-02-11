package raf.graffito.dsw.state.concrete;

import lombok.Setter;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.model.implementation.slide.SlideElement;
import raf.graffito.dsw.state.State;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteState implements State {
    @Setter
    private List<SlideElement> selectedElements = new ArrayList<>();

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
        return false;
    }

    @Override
    public void onStateActivated() {
        Slide slide = (Slide) selectedElements.getFirst().getParent();

        ApplicationFramework.getInstance().getCommandManager().saveStateBeforeChange();

        for (SlideElement elem : selectedElements) {
            slide.removeChild(elem);
        }

        selectedElements.clear();
    }
}