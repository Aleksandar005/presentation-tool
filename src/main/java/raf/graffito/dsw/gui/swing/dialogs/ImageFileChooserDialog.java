package raf.graffito.dsw.gui.swing.dialogs;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * Dijalog za izbor slika sa diska.
 * Podržava višestruki izbor slika.
 */
public class ImageFileChooserDialog {

    private static File lastDirectory = null;

    private ImageFileChooserDialog() {
        // Utility klasa - privatni konstruktor
    }

    // Otvara dijalog za izbor jedne ili više slika.
    public static File[] chooseImages(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Izaberite slike");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Image Files (*.jpg, *.jpeg, *.png, *.gif, *.bmp)",
                "jpg", "jpeg", "png", "gif", "bmp"
        ));

        // Pamti poslednji direktorijum
        if (lastDirectory != null && lastDirectory.exists()) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }

        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();

            // Zapamti direktorijum za sledeći put
            if (selectedFiles.length > 0) {
                lastDirectory = selectedFiles[0].getParentFile();
            }

            return selectedFiles;
        }

        return null;
    }

    // Otvara dijalog za izbor jedne slike.
    public static File chooseSingleImage(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Izaberite sliku");
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Image Files (*.jpg, *.jpeg, *.png, *.gif, *.bmp)",
                "jpg", "jpeg", "png", "gif", "bmp"
        ));

        if (lastDirectory != null && lastDirectory.exists()) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }

        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            lastDirectory = selectedFile.getParentFile();
            return selectedFile;
        }

        return null;
    }
}