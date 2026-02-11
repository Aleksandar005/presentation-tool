package raf.graffito.dsw.model.repository.factory;

import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.slide.Slide;

public class SlideFactory implements NodeFactory {

    @Override
    public GraffNode create(GraffNode parent, String title) {
        return new Slide(parent, title);
    }
}
