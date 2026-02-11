package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.gui.swing.RightPanel;
import raf.graffito.dsw.tree.GraffTree;
import raf.graffito.dsw.tree.model.GraffTreeItem;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.Project;

import java.awt.event.ActionEvent;

public class OpenProjectAction extends AbstractGraffAction {
    private RightPanel rightPanel;

    public OpenProjectAction(RightPanel rightPanel) {
        super("Open Project", "Open project presentations in tabs");
        this.rightPanel = rightPanel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        GraffTree tree = MainFrame.getInstance().getGraffTree();
        if (tree == null) return;

        GraffTreeItem selected = tree.getSelectedNode();
        if (selected == null) return;

        GraffNode node = selected.getGraffNode();
        if (node instanceof Project p) {
            rightPanel.showProject(p);
        }
    }
}
