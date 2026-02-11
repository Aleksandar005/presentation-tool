package raf.graffito.dsw.controller.slideElementActions;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.ImageThumbnailPanel;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.image.ImageProxy;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.state.concrete.AddImageState;

import java.awt.event.ActionEvent;

/**
 * Akcija za selekciju slike iz biblioteke.
 * Aktivira AddImageState sa putanjom izabrane slike.
 */
public class SelectImageFromLibraryAction extends AbstractGraffAction {

    private final ImageProxy imageProxy;
    private final ImageThumbnailPanel thumbnailPanel;
    private final SlideView slideView;

    public SelectImageFromLibraryAction(ImageProxy imageProxy,
                                        ImageThumbnailPanel thumbnailPanel,
                                        SlideView slideView) {
        super("Select Image", "Izaberi sliku za dodavanje na slajd");
        this.imageProxy = imageProxy;
        this.thumbnailPanel = thumbnailPanel;
        this.slideView = slideView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (slideView.getCurrentSlide() == null) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("NO SLIDE SELECTED - Please select a slide first", MessageType.WARNING, this)
            );
            return;
        }

        // Postavi selektovanu sliku u panelu
        thumbnailPanel.setSelectedImage(imageProxy);

        // Aktiviraj AddImageState sa putanjom slike
        AddImageState state = slideView.getStateManager().getAddImageState();
        state.setImagePath(imageProxy.getFilePath());
        slideView.getStateManager().setAddImageState();

        // Osveži thumbnail panel da prikaže selekciju
        thumbnailPanel.refreshThumbnails();

        ApplicationFramework.getInstance().getMessageGenerator().notify(
                new Message("CLICK ON SLIDE TO ADD: " + imageProxy.getFileName(), MessageType.INFO, this)
        );
    }
}