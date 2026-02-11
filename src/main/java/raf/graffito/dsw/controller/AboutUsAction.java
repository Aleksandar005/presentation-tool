package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.dialogs.AboutUsDialog;

import java.awt.event.ActionEvent;

public class AboutUsAction extends AbstractGraffAction{
    public AboutUsAction(){
        super("About Us", "Learn more about creators of this app");
        putValue(SMALL_ICON, loadIcon("about-us.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AboutUsDialog dialog = new AboutUsDialog();
        dialog.setVisible(true);
    }
}
