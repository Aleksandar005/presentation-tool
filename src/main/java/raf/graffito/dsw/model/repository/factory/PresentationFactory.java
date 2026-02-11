package raf.graffito.dsw.model.repository.factory;


import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.Presentation;

public class PresentationFactory implements NodeFactory {
    @Override
    public GraffNode create(GraffNode parent, String title) {
        return new Presentation(parent, title);
    }
}
