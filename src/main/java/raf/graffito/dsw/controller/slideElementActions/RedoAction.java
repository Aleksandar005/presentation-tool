package raf.graffito.dsw.controller.slideElementActions;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;

import java.awt.event.ActionEvent;

public class RedoAction extends AbstractGraffAction {
    public RedoAction() {
        super("Redo", "Redo last undone action");
    }

    @Override
    public void actionPerformed(ActionEvent e){
        ApplicationFramework.getInstance().getCommandManager().redo();
    }
}
