package raf.graffito.dsw.controller;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public abstract class AbstractGraffAction extends AbstractAction{

    public AbstractGraffAction(String name, String description){
        super(name);
        putValue(SHORT_DESCRIPTION, description);
    }

    public Icon loadIcon(String fileName){
        URL url = getClass().getResource("/images/" + fileName);

        if (url == null) {
            System.err.println("Icon file not found: " + fileName);
            return null;
        }

        ImageIcon icon = new ImageIcon(url);
        Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
