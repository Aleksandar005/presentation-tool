package raf.graffito.dsw.tree;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.observer.Subscriber;
import raf.graffito.dsw.tree.model.GraffTreeItem;

import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class TreeRefresher implements Subscriber {
    private DefaultTreeModel treeModel;

    public TreeRefresher(DefaultTreeModel treeModel) {
        this.treeModel = treeModel;

        ApplicationFramework.getInstance().getMessageGenerator().addSubscriber(this);
    }

    @Override
    public void update(Message message) {
        Object src = message.getSource();
        String content = message.getContent();

        if(src instanceof Slide && (content.equals("ELEMENT_ADDED") || content.equals("ELEMENT_REMOVED"))){
            Slide slide = (Slide) src;

            GraffTreeItem slideItem = findTreeItem((GraffTreeItem) treeModel.getRoot(), slide);

            if (slideItem != null) {
                // Rebuild children
                refreshSlideChildren(slideItem, slide);
            }
        }
    }

    private void refreshSlideChildren(GraffTreeItem slideItem, Slide slide) {
        // Obriši staru decu iz tree-a
        slideItem.removeAllChildren();

        // Dodaj novu decu iz modela
        for (GraffNode child : slide.getChildren()) {
            GraffTreeItem childItem = new GraffTreeItem(child);
            slideItem.add(childItem);
        }

        // Notifikuj tree da se promenila struktura
        treeModel.nodeStructureChanged(slideItem);
    }

    private GraffTreeItem findTreeItem(GraffTreeItem root, GraffNode targetNode){
        if(root.getGraffNode() == targetNode){
            return root;
        }

        for(int i = 0; i < root.getChildCount(); i++){
            TreeNode child = root.getChildAt(i);
            if(child instanceof GraffTreeItem){
                GraffTreeItem found = findTreeItem((GraffTreeItem) child, targetNode);
                if (found != null) {
                    return found;
                }
            }
        }

        return null;
    }
}
