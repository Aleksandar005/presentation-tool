package raf.graffito.dsw.strategy;

import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.model.implementation.slide.SlideElement;

import java.awt.*;

/**
 * Način 1: Sabiranje površine svih elemenata na slajdu,
 * bez obzira na njihovo potencijalno preklapanje.
 */
public class SimpleAreaStrategy implements SpaceCheckStrategy {

    @Override
    public double calculateOccupiedSpace(Slide slide) {
        if (slide == null) return 0.0;

        double totalElementArea = 0.0;

        for (GraffNode child : slide.getChildren()) {
            if (child instanceof SlideElement element) {
                Dimension dim = element.getDimension();
                totalElementArea += dim.width * dim.height;
            }
        }

        double slideArea = SlideView.SLIDE_WIDTH * SlideView.SLIDE_HEIGHT;
        return totalElementArea / slideArea;
    }

    @Override
    public String getStrategyName() {
        return "Sabiranje površina";
    }
}