package raf.graffito.dsw.model.repository.factory;

import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.Workspace;

public class WorkspaceFactory implements NodeFactory{

    @Override
    public GraffNode create(GraffNode parent, String title) {
        return new Workspace();
    }
}
