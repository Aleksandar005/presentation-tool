package raf.graffito.dsw.decorator;

import raf.graffito.dsw.model.graff.GraffNode;
import lombok.*;


@Getter
@Setter
public abstract class GraffNodeDecorator extends GraffNode {
    public GraffNode decorated;

    public GraffNodeDecorator(GraffNode decorated) {
        super(decorated.getParent(), decorated.getTitle());
        this.decorated = decorated;
    }

    @Override
    public GraffNode findByName(String name) {
        return null;
    }

    @Override
    public GraffNode getParent() {
        return decorated.getParent();
    }

    @Override
    public String getTitle() {
        return decorated.getTitle();
    }

    @Override public boolean equals(Object o) {
        return (this == o) || (o instanceof GraffNodeDecorator d && d.decorated.equals(this.decorated));
    }
}
