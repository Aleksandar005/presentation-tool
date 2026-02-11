package raf.graffito.dsw.tree.view;

import raf.graffito.dsw.tree.handlers.SlideReorderTransferHandler;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

public class GraffTreeView extends JTree {

    public GraffTreeView(DefaultTreeModel defaultTreeModel) {
        setModel(defaultTreeModel);  // Setujemo model
        setDragEnabled(true);
        setDropMode(DropMode.INSERT);
        setTransferHandler(new SlideReorderTransferHandler((DefaultTreeModel) getModel()));
        GraffTreeCellRenderer ruTreeCellRenderer = new GraffTreeCellRenderer(); // Kreiramo renderer
        setCellRenderer(ruTreeCellRenderer); // Postavljamo renderer
        setEditable(false);
        setToggleClickCount(3);
    }
}
