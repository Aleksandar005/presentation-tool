package raf.graffito.dsw.tree.view;

import javax.swing.*;
import java.awt.*;

public class NameInputDialog {
    private NameInputDialog() {}

    public static String prompt(Window parent, String title, String initial){
        String s = (String) JOptionPane.showInputDialog(
                parent,
                "New name:",
                title != null ? title : "Rename",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                initial
        );

        return (s==null)?initial:s;
    }
}
