package raf.graffito.dsw.controller.saving;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.utility.FileChoosers;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.Project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class SaveProjectAction extends AbstractGraffAction {
    public SaveProjectAction() {
        super("Save", "Save project");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));
        putValue(SMALL_ICON, loadIcon("save.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Project project = getSelectedProject();

        if (project == null) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("NO PROJECT SELECTED", MessageType.ERROR, this));
            return;
        }

        if (!project.isChanged()) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("PROJECT NOT CHANGED - NO NEED TO SAVE", MessageType.WARNING, this));
            return;
        }

        File filePath = project.getFilePath();

        if (filePath == null) {
            filePath = FileChoosers.chooseProjectToSave(project.getTitle());
            if (filePath == null) return;
        }

        ApplicationFramework.getInstance().getSerializer().saveProject(project, filePath);
        project.setFilePath(filePath);
        project.setChanged(false);

        ApplicationFramework.getInstance().getMessageGenerator().notify(
                new Message("PROJECT SAVED: " + filePath.getAbsolutePath(), MessageType.INFO, this));
    }

    private Project getSelectedProject() {
        if (MainFrame.getInstance().getGraffTree() == null) return null;
        if (MainFrame.getInstance().getGraffTree().getSelectedNode() == null) return null;
        GraffNode selected = MainFrame.getInstance().getGraffTree().getSelectedNode().getGraffNode();
        if (selected instanceof Project) {
            return (Project) selected;
        }
        return null;
    }
}