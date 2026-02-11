package raf.graffito.dsw.controller.saving;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.utility.FileChoosers;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.implementation.Project;

import java.awt.event.ActionEvent;
import java.io.File;

public class OpenProjectFromFileAction extends AbstractGraffAction {

    public OpenProjectFromFileAction() {
        super("Open Project", "Open project from file");
        putValue(SMALL_ICON, loadIcon("open-project.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = FileChoosers.chooseProjectToOpen();
        if (file == null) return;

        Project loadedProject = ApplicationFramework.getInstance().getSerializer().loadProject(file);

        if (loadedProject == null) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("ERROR LOADING PROJECT", MessageType.ERROR, this));
            return;
        }

        if (projectNameExists(loadedProject.getTitle())) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("PROJECT WITH NAME '" + loadedProject.getTitle() + "' ALREADY EXISTS", MessageType.ERROR, this));
            return;
        }

        loadedProject.setFilePath(file);
        loadedProject.setChanged(false);

        ApplicationFramework.getInstance().getGraffRepository().getRoot().addChild(loadedProject);
        MainFrame.getInstance().getGraffTree().refreshTree();

        ApplicationFramework.getInstance().getMessageGenerator().notify(
                new Message("PROJECT LOADED: " + file.getAbsolutePath(), MessageType.INFO, this));
    }

    private boolean projectNameExists(String projectName) {
        return ApplicationFramework.getInstance()
                .getGraffRepository()
                .getRoot()
                .findByName(projectName) != null;
    }
}