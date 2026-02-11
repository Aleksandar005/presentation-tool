package raf.graffito.dsw.tree;

import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.tree.model.GraffTreeItem;
import raf.graffito.dsw.tree.view.GraffTreeView;
import raf.graffito.dsw.model.repository.NodeType;

public interface GraffTree {

    GraffTreeView generateTree();
    void addChild(GraffTreeItem parent, NodeType type);
    void addChild(GraffTreeItem parent);
    void removeSelected();
    void renameSelected(String newTitle);
    void setAuthor(String author);
    void refreshTree();
    GraffTreeItem getSelectedNode();
}
