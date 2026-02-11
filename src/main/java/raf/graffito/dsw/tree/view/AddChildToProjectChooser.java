package raf.graffito.dsw.tree.view;

import raf.graffito.dsw.model.repository.NodeType;

import javax.swing.*;
import java.awt.*;

public class AddChildToProjectChooser {
    private AddChildToProjectChooser() {}

    public static NodeType chooseForProject(Window parent) {
        Object[] options = {"Presentation", "Slide", "Cancel"};
        int i = JOptionPane.showOptionDialog(
                parent,
                "Add under Project:",
                "Create",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
        );
        if (i == 0) return NodeType.PRESENTATION;
        if (i == 1) return NodeType.SLIDE;
        return null;
    }
}
