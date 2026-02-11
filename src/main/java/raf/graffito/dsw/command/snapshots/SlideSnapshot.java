package raf.graffito.dsw.command.snapshots;

import lombok.Getter;
import lombok.Setter;
import raf.graffito.dsw.model.implementation.slide.SlideElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SlideSnapshot {
    private List<ElementSnapshot> elementSnapshots = new ArrayList<>();
    @Setter @Getter
    private List<SlideElement> elementList = new ArrayList<>();

    public void addElementState(SlideElement element, Point location, Dimension dimension, double rotation) {
        elementSnapshots.add(new ElementSnapshot(element, location, dimension, rotation));
    }

    public void restoreElementStates(){
        for(ElementSnapshot snapshot : elementSnapshots){
            snapshot.restore();
        }
    }

}
