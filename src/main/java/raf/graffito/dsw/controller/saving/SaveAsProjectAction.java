package raf.graffito.dsw.controller.saving;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.utility.FileChoosers;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.Project;

import java.awt.event.ActionEvent;
import java.io.File;

public class SaveAsProjectAction extends AbstractGraffAction {

    public SaveAsProjectAction() {
        super("Save As", "Save project to new location");
        putValue(SMALL_ICON, loadIcon("save-as.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Project project = getSelectedProject();

        if (project == null) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("NO PROJECT SELECTED", MessageType.WARNING, this));
            return;
        }

        File filePath = FileChoosers.chooseProjectToSave(project.getTitle());
        if (filePath == null) return;

        ApplicationFramework.getInstance().getSerializer().saveProject(project, filePath);
        project.setFilePath(filePath);
        project.setChanged(false);

        ApplicationFramework.getInstance().getMessageGenerator().notify(
                new Message("PROJECT SAVED AS: " + filePath.getAbsolutePath(), MessageType.INFO, this));
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