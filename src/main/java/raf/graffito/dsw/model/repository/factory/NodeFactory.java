package raf.graffito.dsw.model.repository.factory;

import raf.graffito.dsw.model.graff.GraffNode;

public interface NodeFactory {
    GraffNode create(GraffNode parent, String title);
}
