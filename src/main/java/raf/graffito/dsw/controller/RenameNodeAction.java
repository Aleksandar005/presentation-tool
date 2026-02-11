package raf.graffito.dsw.controller;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.tree.GraffTree;
import raf.graffito.dsw.tree.model.GraffTreeItem;
import raf.graffito.dsw.tree.view.NameInputDialog;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.Workspace;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RenameNodeAction extends AbstractGraffAction {
    public RenameNodeAction() {
        super("Rename", "Rename selected file");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl R"));
        putValue(SMALL_ICON, loadIcon("rename-action.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GraffTree tree = MainFrame.getInstance().getGraffTree();
        if(tree == null) return;

        GraffTreeItem selected = tree.getSelectedNode();
        if(selected == null) {
            tree.renameSelected(null);
            return;
        }

        GraffNode node = selected.getGraffNode();
        String current = node.getTitle();

        if (node instanceof Workspace) {
            ApplicationFramework.getInstance()
                    .getMessageGenerator()
                    .notify(new Message("WORKSPACE CANNOT BE RENAMED", MessageType.WARNING, node));
            return;
        }

        String name = NameInputDialog.prompt(MainFrame.getInstance(), "Rename", current);

        tree.renameSelected(name);
    }
}
