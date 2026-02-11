package raf.graffito.dsw.controller.events;

import raf.graffito.dsw.controller.AbstractGraffAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TreeDoubleClickAdapter extends MouseAdapter {
    private AbstractGraffAction doubleClickAction;

    public TreeDoubleClickAdapter(AbstractGraffAction doubleClickAction) {
        this.doubleClickAction = doubleClickAction;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getClickCount() == 2 && doubleClickAction != null) {
            doubleClickAction.actionPerformed(
                    new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, "doubleClick")
            );
        }
    }
}
