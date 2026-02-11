package raf.graffito.dsw.controller.slideElementActions;

import lombok.Setter;
import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.ImageThumbnailPanel;
import raf.graffito.dsw.gui.swing.dialogs.ImageFileChooserDialog;
import raf.graffito.dsw.image.ImageLibrary;
import raf.graffito.dsw.image.ImageProxy;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.implementation.Project;

import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Akcija za učitavanje slika sa diska u biblioteku projekta.
 * Otvara ImageFileChooserDialog i dodaje izabrane slike.
 */
public class LoadImageToLibraryAction extends AbstractGraffAction {

    @Setter
    private ImageThumbnailPanel thumbnailPanel;

    public LoadImageToLibraryAction() {
        super("Učitaj Sliku", "Učitaj slike sa diska u biblioteku");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (thumbnailPanel == null) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("IMAGE PANEL NOT INITIALIZED", MessageType.ERROR, this)
            );
            return;
        }

        Project currentProject = thumbnailPanel.getCurrentProject();

        if (currentProject == null) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("NO PROJECT SELECTED", MessageType.WARNING, this)
            );
            return;
        }

        // Otvori dijalog za izbor slika
        File[] selectedFiles = ImageFileChooserDialog.chooseImages(thumbnailPanel);

        if (selectedFiles == null || selectedFiles.length == 0) {
            return; // Korisnik je otkazao
        }

        // Dodaj slike u biblioteku
        ImageLibrary library = ApplicationFramework.getInstance().getImageLibrary();
        int loadedCount = 0;
        int duplicateCount = 0;

        for (File file : selectedFiles) {
            String path = file.getAbsolutePath();

            // Proveri da li je slika već učitana
            if (library.containsImagePath(currentProject, path)) {
                duplicateCount++;
                continue;
            }

            // Kreiraj proxy i dodaj u biblioteku
            ImageProxy proxy = new ImageProxy(path);
            library.addImage(currentProject, proxy);
            loadedCount++;
        }

        // Osveži prikaz
        thumbnailPanel.refreshThumbnails();

        // Obavesti korisnika
        if (loadedCount > 0) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message(loadedCount + " IMAGE(S) LOADED", MessageType.INFO, this)
            );
        }

        if (duplicateCount > 0) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message(duplicateCount + " IMAGE(S) ALREADY IN LIBRARY", MessageType.WARNING, this)
            );
        }
    }
}