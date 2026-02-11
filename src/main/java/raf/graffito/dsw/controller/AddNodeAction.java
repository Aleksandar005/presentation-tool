package raf.graffito.dsw.controller;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.serializer.TemplateManager;
import raf.graffito.dsw.tree.GraffTree;
import raf.graffito.dsw.tree.model.GraffTreeItem;
import raf.graffito.dsw.tree.view.AddChildToProjectChooser;
import raf.graffito.dsw.tree.view.TemplateChooserDialog;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.model.implementation.Workspace;
import raf.graffito.dsw.model.repository.NodeType;
import raf.graffito.dsw.tree.view.AuthorInputDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;

public class AddNodeAction extends AbstractGraffAction {
    public AddNodeAction() {
        super("Add", "Add new file");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl A"));
        putValue(SMALL_ICON, loadIcon("add-action.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GraffTree tree = MainFrame.getInstance().getGraffTree();
        if (tree == null) return;
        GraffTreeItem selected = tree.getSelectedNode();
        if(selected == null) {
            tree.addChild(selected);
            return;
        }

        GraffNode node = selected.getGraffNode();

        if(node instanceof Project){
            NodeType type = AddChildToProjectChooser.chooseForProject(MainFrame.getInstance());
            if(type != null) tree.addChild(selected, type);
        } else if (node instanceof Workspace){
            tree.addChild(selected);
            GraffTreeItem projectItem = (GraffTreeItem) selected.getChildAt(selected.getChildCount() - 1);

            AuthorInputDialog dialog = new AuthorInputDialog(MainFrame.getInstance());
            dialog.setVisible(true);

            String author = dialog.getAuthor();

            if (author == null) {
                tree.removeSelected();
                return;
            }

            if (author.trim().isEmpty()) {
                tree.removeSelected();
                ApplicationFramework.getInstance().getMessageGenerator().notify(
                        new Message("AUTHOR NAME CANNOT BE EMPTY", MessageType.ERROR, this));
                return;
            }

            tree.setAuthor(author);

            // Pitaj korisnika da li želi prazan projekat ili šablon
            TemplateChooserDialog.Choice choice = TemplateChooserDialog.showDialog(MainFrame.getInstance());

            switch (choice) {
                case LOAD_TEMPLATE:
                    // Učitaj šablon u novokreirani projekat
                    if (!loadTemplateIntoNewProject(projectItem)) {
                        // Ako učitavanje nije uspelo ili je otkazano, obriši projekat
                        tree.removeSelected();
                    }
                    break;

                case EMPTY_PROJECT:
                    // Standardna procedura - izaberi tip čvora za dodavanje
                    NodeType type = AddChildToProjectChooser.chooseForProject(MainFrame.getInstance());
                    if (type == null) tree.removeSelected();
                    else tree.addChild(projectItem, type);
                    break;

                case CANCEL:
                    // Obriši novokreirani projekat
                    tree.removeSelected();
                    break;
            }

        }
        else {
            tree.addChild(selected);
        }
    }

    // Učitava šablon u novokreirani projekat.
    private boolean loadTemplateIntoNewProject(GraffTreeItem projectItem) {
        TemplateManager templateManager = ApplicationFramework.getInstance().getTemplateManager();
        File templatesDir = templateManager.getTemplatesDirectory();

        JFileChooser fileChooser = new JFileChooser(templatesDir);
        fileChooser.setDialogTitle("Select Template");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Template Files", "json"));

        int result = fileChooser.showOpenDialog(MainFrame.getInstance());

        if (result == JFileChooser.APPROVE_OPTION) {
            File templateFile = fileChooser.getSelectedFile();
            Project project = (Project) projectItem.getGraffNode();

            boolean success = templateManager.loadTemplateIntoProject(templateFile, project);

            if (success) {
                // Osveži tree
                MainFrame.getInstance().getGraffTree().refreshTree();
                return true;
            }
        }

        // Cancel ili neuspešno učitavanje - vrati false da se projekat obriše
        return false;
    }
}