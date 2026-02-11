package raf.graffito.dsw.tree.model;

import raf.graffito.dsw.model.graff.GraffNode;
import javax.swing.tree.DefaultMutableTreeNode;
import lombok.*;

@Getter
@Setter
public class GraffTreeItem extends DefaultMutableTreeNode {
    private GraffNode graffNode;

    public GraffTreeItem(GraffNode graffNode) {
        this.graffNode = graffNode;
    }

    @Override
    public String toString() {
        return graffNode.getTitle();
    }

}
