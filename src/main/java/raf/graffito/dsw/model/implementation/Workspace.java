package raf.graffito.dsw.model.implementation;

import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.graff.GraffNodeComposite;

public class Workspace extends GraffNodeComposite {
    public Workspace() {
        super(null,"Workspace");
    }

    @Override
    public void addChild(GraffNode child){
        if (child == null) {
            throw new IllegalArgumentException("Child node cannot be null");
        }
        super.getChildren().add(child);
    }

    @Override
    public void removeChild(GraffNode child) {

        super.getChildren().remove(child);
    }
}
