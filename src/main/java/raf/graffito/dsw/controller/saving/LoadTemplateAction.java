package raf.graffito.dsw.controller.saving;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.utility.FileChoosers;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.model.implementation.Workspace;
import raf.graffito.dsw.serializer.TemplateManager;
import raf.graffito.dsw.tree.GraffTree;
import raf.graffito.dsw.tree.GraffTreeImplementation;
import raf.graffito.dsw.tree.model.GraffTreeItem;
import raf.graffito.dsw.tree.view.AuthorInputDialog;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.io.File;

// Akcija za učitavanje šablona u postojeći projekat.
public class LoadTemplateAction extends AbstractGraffAction {

    public LoadTemplateAction() {
        super("Load Template", "Load a template into the current project");
        putValue(SMALL_ICON, loadIcon("open-template.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GraffTree tree = MainFrame.getInstance().getGraffTree();
        GraffTreeItem selected = tree.getSelectedNode();
        Project project = getProjectFromSelection(selected);

        boolean isNewProject = false;

        // Ako nema selektovanog projekta, kreiraj novi
        if (project == null) {
            project = createNewProjectWithAuthor(tree);
            if (project == null) {
                return;
            }
            isNewProject = true;
        } else {
            // Upozorenje ako postojeći projekat već ima sadržaj
            if (!project.getChildren().isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(
                        MainFrame.getInstance(),
                        "Loading a template will replace the current project content.\n" +
                                "Do you want to continue?",
                        "Confirm Load Template",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }
        }

        File templateFile = FileChoosers.chooseTemplateToOpen();

        if (templateFile != null) {
            loadTemplateIntoProject(templateFile, project);
        } else if (isNewProject) {
            tree.removeSelected();
        }
    }

    private Project createNewProjectWithAuthor(GraffTree tree) {
        selectWorkspaceIfNeeded(tree);

        GraffTreeItem workspaceItem = tree.getSelectedNode();

        if (workspaceItem == null || !(workspaceItem.getGraffNode() instanceof Workspace)) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("CANNOT FIND WORKSPACE", MessageType.ERROR, this)
            );
            return null;
        }

        tree.addChild(workspaceItem);

        GraffTreeItem projectItem = tree.getSelectedNode();

        if (projectItem == null || !(projectItem.getGraffNode() instanceof Project)) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("FAILED TO CREATE PROJECT", MessageType.ERROR, this)
            );
            return null;
        }

        AuthorInputDialog dialog = new AuthorInputDialog(MainFrame.getInstance());
        dialog.setVisible(true);

        String author = dialog.getAuthor();

        if (author == null) {
            tree.removeSelected();
            return null;
        }

        if (author.trim().isEmpty()) {
            tree.removeSelected();
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("AUTHOR NAME CANNOT BE EMPTY", MessageType.ERROR, this));
            return null;
        }

        tree.setAuthor(author);
        return (Project) projectItem.getGraffNode();
    }

    private void selectWorkspaceIfNeeded(GraffTree tree) {
        GraffTreeItem selected = tree.getSelectedNode();

        if (selected != null && selected.getGraffNode() instanceof Workspace) {
            return;
        }

        if (selected != null) {
            GraffTreeItem workspaceItem = findWorkspaceItem(selected);
            if (workspaceItem != null) {
                selectNode(tree, workspaceItem);
                return;
            }
        }

        if (tree instanceof GraffTreeImplementation) {
            GraffTreeImplementation treeImpl = (GraffTreeImplementation) tree;
            GraffTreeItem root = (GraffTreeItem) treeImpl.getTreeModel().getRoot();
            selectNode(tree, root);
        }
    }

    private void selectNode(GraffTree tree, GraffTreeItem item) {
        if (tree instanceof GraffTreeImplementation) {
            GraffTreeImplementation treeImpl = (GraffTreeImplementation) tree;
            TreePath path = new TreePath(item.getPath());
            treeImpl.getTreeView().setSelectionPath(path);
        }
    }

    private GraffTreeItem findWorkspaceItem(GraffTreeItem item) {
        if (item == null) return null;

        GraffNode node = item.getGraffNode();

        if (node instanceof Workspace) {
            return item;
        }

        if (item.getParent() instanceof GraffTreeItem) {
            return findWorkspaceItem((GraffTreeItem) item.getParent());
        }

        return null;
    }

    private boolean loadTemplateIntoProject(File templateFile, Project project) {
        TemplateManager templateManager = ApplicationFramework.getInstance().getTemplateManager();
        boolean success = templateManager.loadTemplateIntoProject(templateFile, project);

        if (success) {
            MainFrame.getInstance().getGraffTree().refreshTree();

            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("TEMPLATE LOADED: " + templateFile.getName(), MessageType.INFO, this)
            );
        }

        return success;
    }

    private Project getProjectFromSelection(GraffTreeItem selected) {
        if (selected == null) return null;

        GraffNode node = selected.getGraffNode();

        if (node instanceof Project) {
            return (Project) node;
        }

        return null;
    }
}