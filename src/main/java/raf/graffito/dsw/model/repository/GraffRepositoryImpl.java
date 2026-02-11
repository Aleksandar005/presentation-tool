package raf.graffito.dsw.model.repository;

import raf.graffito.dsw.model.implementation.Workspace;
import raf.graffito.dsw.model.repository.factory.*;

public class GraffRepositoryImpl implements GraffRepository {
    private Workspace root;

    public GraffRepositoryImpl() {
        root = new Workspace();
    }

    @Override
    public Workspace getRoot() {
        return root;
    }

    @Override
    public NodeFactory getNodeFactory(NodeType type) {
        return switch (type) {
            case WORKSPACE -> new WorkspaceFactory();
            case PROJECT -> new ProjectFactory();
            case PRESENTATION -> new PresentationFactory();
            case SLIDE -> new SlideFactory();
            default -> null;
        };

    }
}
