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

public class SaveAsTemplateAction extends AbstractGraffAction {
    public SaveAsTemplateAction() {
        super("Save as Template", "Save current project as a template");
        putValue(SMALL_ICON, loadIcon("save template.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Project project = getSelectedProject();

        if (project == null) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("NO PROJECT SELECTED", MessageType.WARNING, this)
            );
            return;
        }

        File selectedFile = FileChoosers.chooseTemplateToSave(project.getTitle());
        if (selectedFile == null) return;

        if (!FileChoosers.isInTemplatesFolder(selectedFile)) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("CANNOT SAVE TEMPLATE OUTSIDE TEMPLATE FOLDER", MessageType.ERROR, this)
            );
            return;
        }

        if (selectedFile.exists()) {
            int overwrite = JOptionPane.showConfirmDialog(
                    MainFrame.getInstance(),
                    "Template already exists. Do you want to overwrite it?",
                    "Confirm Overwrite",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (overwrite != JOptionPane.YES_OPTION) {
                return;
            }
        }

        ApplicationFramework.getInstance().getSerializer().saveProject(project, selectedFile);

        ApplicationFramework.getInstance().getMessageGenerator().notify(
                new Message("TEMPLATE SAVED: " + selectedFile.getName(), MessageType.INFO, this)
        );
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