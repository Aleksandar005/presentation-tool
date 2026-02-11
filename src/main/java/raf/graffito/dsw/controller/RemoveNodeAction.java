package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.tree.GraffTree;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RemoveNodeAction extends AbstractGraffAction {
    public RemoveNodeAction() {
        super("Remove", "Remove selected file");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
        putValue(SMALL_ICON, loadIcon("remove-action.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GraffTree tree = MainFrame.getInstance().getGraffTree();
        if(tree == null) return;

        tree.removeSelected();
    }
}
