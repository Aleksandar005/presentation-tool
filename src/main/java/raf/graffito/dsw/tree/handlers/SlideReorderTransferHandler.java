package raf.graffito.dsw.tree.handlers;

import raf.graffito.dsw.tree.model.GraffTreeItem;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.graff.GraffNodeComposite;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.model.implementation.slide.Slide;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.List;

public class SlideReorderTransferHandler extends TransferHandler {

    private final DefaultTreeModel model;

    private GraffTreeItem dragged;
    private GraffTreeItem srcParent;

    public SlideReorderTransferHandler(DefaultTreeModel model) {
        this.model = model;
    }

    @Override public int getSourceActions(JComponent c) { return MOVE; }

    @Override
    protected Transferable createTransferable(JComponent component) {
        JTree tree = (JTree) component;
        TreePath selectedPath = tree.getSelectionPath();

        if (selectedPath == null) {
            return null;
        }

        GraffTreeItem selectedItem = (GraffTreeItem) selectedPath.getLastPathComponent();

        if (!(selectedItem.getGraffNode() instanceof Slide)) {
            return null;
        }

        dragged = selectedItem;
        srcParent = (GraffTreeItem) selectedItem.getParent();

        if (srcParent == null) {
            return null;
        }

        GraffNode parentNode = srcParent.getGraffNode();
        if (!(parentNode instanceof Project || parentNode instanceof Presentation)) {
            return null;
        }

        return new StringSelection("slide");
    }

    @Override
    public boolean canImport(TransferSupport support) {
        if (!support.isDrop() || dragged == null || srcParent == null) {
            return false;
        }

        JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
        GraffTreeItem targetItem = (GraffTreeItem) dropLocation.getPath().getLastPathComponent();

        GraffNode targetNode = targetItem.getGraffNode();
        boolean targetIsSlide = targetNode instanceof Slide;
        boolean targetIsProjectOrPresentation =
                targetNode instanceof Project || targetNode instanceof Presentation;

        GraffTreeItem destinationParent =
                targetIsSlide ? (GraffTreeItem) targetItem.getParent() : targetItem;

        boolean sameParent = destinationParent == srcParent;

        return (targetIsSlide || targetIsProjectOrPresentation) && sameParent;
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) return false;

        JTree tree = (JTree) support.getComponent();
        JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
        GraffTreeItem targetItem = (GraffTreeItem) dropLocation.getPath().getLastPathComponent();

        GraffTreeItem parentItem =
                (targetItem.getGraffNode() instanceof Slide)
                        ? (GraffTreeItem) targetItem.getParent()
                        : targetItem;

        int fromIndex = parentItem.getIndex(dragged);
        int toIndex   = dropLocation.getChildIndex();

        if (toIndex == -1) toIndex = parentItem.getChildCount();
        if (toIndex > fromIndex) toIndex--;
        if (fromIndex == toIndex) return false;

        GraffNodeComposite parentNode = (GraffNodeComposite) parentItem.getGraffNode();

        // 1) Domen: pomeri dete u listi
        List<GraffNode> children = parentNode.getChildren();
        GraffNode moved = children.remove(fromIndex);
        children.add(toIndex, moved);

        // 2) JTree model: pomeri i UI čvor
        model.removeNodeFromParent(dragged);
        model.insertNodeInto(dragged, parentItem, toIndex);

        // 3) Selektuj novu poziciju
        TreePath newPath = new TreePath(dragged.getPath());
        tree.setSelectionPath(newPath);
        tree.scrollPathToVisible(newPath);

        return true;
    }


    @Override
    public void exportDone(JComponent source, Transferable data, int action) {
        dragged = null;
        srcParent = null;
    }
}
