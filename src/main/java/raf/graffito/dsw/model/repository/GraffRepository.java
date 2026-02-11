package raf.graffito.dsw.model.repository;

import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.graff.GraffNodeComposite;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Workspace;
import raf.graffito.dsw.model.repository.factory.NodeFactory;

public interface GraffRepository {
    Workspace getRoot();
    NodeFactory getNodeFactory(NodeType type);

}
