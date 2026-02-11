package raf.graffito.dsw.controller;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExitAction extends AbstractGraffAction {
    public ExitAction() {
        super("Exit", "Exit application");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));
        putValue(SMALL_ICON, loadIcon("exit.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
