package raf.graffito.dsw.model.repository.factory;

import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.Project;

public class ProjectFactory implements NodeFactory {

    @Override
    public GraffNode create(GraffNode parent, String title) {
        return new Project(parent, title);
    }
}
