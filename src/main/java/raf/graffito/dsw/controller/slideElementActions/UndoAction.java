package raf.graffito.dsw.controller.slideElementActions;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;

import java.awt.event.ActionEvent;

public class UndoAction extends AbstractGraffAction {
    public UndoAction() {
        super("Undo", "Undo last action");
    }

    @Override
    public void actionPerformed(ActionEvent e){
        ApplicationFramework.getInstance().getCommandManager().undo();
    }
}
