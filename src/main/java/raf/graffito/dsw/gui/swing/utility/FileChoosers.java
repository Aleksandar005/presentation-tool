package raf.graffito.dsw.gui.swing.utility;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.gui.swing.MainFrame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * Centralizovani file chooser-i za aplikaciju.
 */
public class FileChoosers {

    private FileChoosers() {
        // Utility klasa - bez instanciranja
    }

    /**
     * Otvara dijalog za izbor šablona za učitavanje.
     * Dozvoljava samo fajlove iz templates foldera.
     */
    public static File chooseTemplateToOpen() {
        File templatesDir = ApplicationFramework.getInstance()
                .getTemplateManager()
                .getTemplatesDirectory();

        JFileChooser fileChooser = new JFileChooser(templatesDir);
        fileChooser.setDialogTitle("Select Template");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Template Files", "json"));

        int result = fileChooser.showOpenDialog(MainFrame.getInstance());

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Provera da je fajl iz templates foldera
            if (!isInTemplatesFolder(selectedFile)) {
                ApplicationFramework.getInstance().getMessageGenerator().notify(
                        new Message("CANNOT LOAD TEMPLATE FROM OUTSIDE TEMPLATE FOLDER", MessageType.ERROR, null)
                );
                return null;
            }

            return selectedFile;
        }
        return null;
    }

    /**
     * Otvara dijalog za otvaranje projekta.
     */
    public static File chooseProjectToOpen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Project");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));

        int result = fileChooser.showOpenDialog(MainFrame.getInstance());

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Otvara dijalog za čuvanje projekta.
     */
    public static File chooseProjectToSave(String projectTitle) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Project");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));

        if (projectTitle != null && !projectTitle.isEmpty()) {
            String suggestedName = projectTitle.replaceAll("\\s+", "_") + ".json";
            fileChooser.setSelectedFile(new File(suggestedName));
        }

        int result = fileChooser.showSaveDialog(MainFrame.getInstance());

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".json")) {
                file = new File(file.getAbsolutePath() + ".json");
            }
            return file;
        }
        return null;
    }

    /**
     * Otvara dijalog za čuvanje šablona.
     * Dozvoljava samo čuvanje u templates folderu.
     */
    public static File chooseTemplateToSave(String projectTitle) {
        File templatesDir = ApplicationFramework.getInstance()
                .getTemplateManager()
                .getTemplatesDirectory();

        JFileChooser fileChooser = new JFileChooser(templatesDir);
        fileChooser.setDialogTitle("Save Project as Template");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));

        if (projectTitle != null && !projectTitle.isEmpty()) {
            String suggestedName = projectTitle.replaceAll("\\s+", "_") + "_template.json";
            fileChooser.setSelectedFile(new File(templatesDir, suggestedName));
        }

        int result = fileChooser.showSaveDialog(MainFrame.getInstance());

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".json")) {
                file = new File(file.getAbsolutePath() + ".json");
            }
            return file;
        }
        return null;
    }

    // Proverava da li je fajl unutar templates foldera.
    public static boolean isInTemplatesFolder(File file) {
        try {
            File templatesDir = ApplicationFramework.getInstance()
                    .getTemplateManager()
                    .getTemplatesDirectory();
            String filePath = file.getCanonicalPath();
            String templatesDirPath = templatesDir.getCanonicalPath();
            return filePath.startsWith(templatesDirPath);
        } catch (Exception e) {
            return false;
        }
    }
}