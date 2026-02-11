package raf.graffito.dsw.model.graff;

import lombok.*;

@Getter
@Setter
public abstract class GraffNode {
    private GraffNode parent;
    private String title;

    public GraffNode(GraffNode parent,String title) {
        this.parent = parent;
        this.title = title;
    }

    public abstract GraffNode findByName(String name);
}
