package raf.graffito.dsw.controller;

import raf.graffito.dsw.controller.saving.*;
import raf.graffito.dsw.controller.slideElementActions.*;
import raf.graffito.dsw.controller.slideElementActions.LoadImageToLibraryAction;
import raf.graffito.dsw.gui.swing.ImageThumbnailPanel;
import raf.graffito.dsw.gui.swing.RightPanel;
import lombok.Getter;
import raf.graffito.dsw.gui.swing.SlideView;

@Getter
public class ActionManager {
    private final ExitAction exitAction;
    private final AboutUsAction aboutUsAction;
    private final AddNodeAction addNodeAction;
    private final RemoveNodeAction removeNodeAction;
    private final RenameNodeAction renameNodeAction;
    private OpenProjectAction openProjectAction;

    // Undo/Redo akcije
    private final UndoAction undoAction;
    private final RedoAction redoAction;

    // Akcije za serijalizaciju
    private final SaveProjectAction saveProjectAction;
    private final SaveAsProjectAction saveAsProjectAction;
    private final OpenProjectFromFileAction openProjectFromFileAction;

    // Akcije za šablone
    private final SaveAsTemplateAction saveAsTemplateAction;
    private final LoadTemplateAction loadTemplateAction;

    // Akcije za biblioteku slika
    private final LoadImageToLibraryAction loadImageToLibraryAction;

    // Akcije za upravljanje slajdovima
    private AddLogoSlideElementAction addLogoSlideElementAction;
    private AddTextSlideElementAction addTextSlideElementAction;
    private DeleteSlideElementAction deleteSlideElementAction;
    private MoveSlideElementAction moveSlideElementAction;
    private ResizeSlideElementAction resizeSlideElementAction;
    private RotateLeftAction rotateLeftAction;
    private RotateRightAction rotateRightAction;
    private SelectSlideElementAction selectSlideElementAction;
    private ZoomSlideElementAction zoomSlideElementAction;
    private CopySlideElementAction copySlideElementAction;
    private PasteSlideElementAction pasteSlideElementAction;

    public ActionManager() {
        this.exitAction = new ExitAction();
        this.aboutUsAction = new AboutUsAction();
        this.addNodeAction = new AddNodeAction();
        this.removeNodeAction = new RemoveNodeAction();
        this.renameNodeAction = new RenameNodeAction();
        this.undoAction = new UndoAction();
        this.redoAction = new RedoAction();
        this.saveProjectAction = new SaveProjectAction();
        this.saveAsProjectAction = new SaveAsProjectAction();
        this.openProjectFromFileAction = new OpenProjectFromFileAction();
        this.saveAsTemplateAction = new SaveAsTemplateAction();
        this.loadTemplateAction = new LoadTemplateAction();
        this.loadImageToLibraryAction = new LoadImageToLibraryAction();
    }

    public void initRightPanelActions(RightPanel rp) {
        this.openProjectAction = new OpenProjectAction(rp);
    }

    public void initImageLibraryActions(ImageThumbnailPanel thumbnailPanel) {
        this.loadImageToLibraryAction.setThumbnailPanel(thumbnailPanel);
    }

    public void initSlideElementActions(SlideView slideView) {
        addLogoSlideElementAction = new AddLogoSlideElementAction(slideView);
        addTextSlideElementAction = new AddTextSlideElementAction(slideView);
        deleteSlideElementAction = new DeleteSlideElementAction(slideView);
        moveSlideElementAction = new MoveSlideElementAction(slideView);
        resizeSlideElementAction = new ResizeSlideElementAction(slideView);
        rotateLeftAction = new RotateLeftAction(slideView);
        rotateRightAction = new RotateRightAction(slideView);
        selectSlideElementAction = new SelectSlideElementAction(slideView);
        zoomSlideElementAction = new ZoomSlideElementAction(slideView);
        copySlideElementAction = new CopySlideElementAction(slideView);
        pasteSlideElementAction = new PasteSlideElementAction(slideView);
    }
}