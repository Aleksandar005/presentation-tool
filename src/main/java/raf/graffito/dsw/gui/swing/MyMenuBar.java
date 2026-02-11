package raf.graffito.dsw.gui.swing;


import raf.graffito.dsw.controller.AboutUsAction;
import raf.graffito.dsw.controller.ExitAction;
import raf.graffito.dsw.controller.AddNodeAction;
import raf.graffito.dsw.controller.RemoveNodeAction;
import raf.graffito.dsw.controller.RenameNodeAction;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MyMenuBar extends JMenuBar {
    public MyMenuBar() {
        // File
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        ExitAction exit = MainFrame.getInstance().getActionManager().getExitAction();
        fileMenu.add(new JMenuItem(exit));
        AddNodeAction addNode = MainFrame.getInstance().getActionManager().getAddNodeAction();
        fileMenu.add(new JMenuItem(addNode));
        RemoveNodeAction removeNode = MainFrame.getInstance().getActionManager().getRemoveNodeAction();
        fileMenu.add(new JMenuItem(removeNode));
        add(fileMenu);

        fileMenu.addSeparator();

        // Save
        fileMenu.add(new JMenuItem(MainFrame.getInstance().getActionManager().getSaveProjectAction()));

        // Save As
        fileMenu.add(new JMenuItem(MainFrame.getInstance().getActionManager().getSaveAsProjectAction()));

        // Template opcije
        fileMenu.add(new JMenuItem(MainFrame.getInstance().getActionManager().getSaveAsTemplateAction()));

        // Open Project from file
        fileMenu.add(new JMenuItem(MainFrame.getInstance().getActionManager().getOpenProjectFromFileAction()));

        // Template opcije
        fileMenu.add(new JMenuItem(MainFrame.getInstance().getActionManager().getLoadTemplateAction()));
        fileMenu.addSeparator();

        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);

        editMenu.add(new JMenuItem(MainFrame.getInstance().getActionManager().getRenameNodeAction()));
        editMenu.addSeparator();

        // Undo/Redo
        editMenu.add(new JMenuItem(MainFrame.getInstance().getActionManager().getUndoAction()));
        editMenu.add(new JMenuItem(MainFrame.getInstance().getActionManager().getRedoAction()));

        // Help
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        AboutUsAction aboutUs = MainFrame.getInstance().getActionManager().getAboutUsAction();
        helpMenu.add(new JMenuItem(aboutUs));
        add(helpMenu);
    }
}
