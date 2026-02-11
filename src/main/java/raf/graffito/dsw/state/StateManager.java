package raf.graffito.dsw.state;

import lombok.Getter;
import raf.graffito.dsw.model.implementation.slide.SlideElement;
import raf.graffito.dsw.state.concrete.*;

import java.util.ArrayList;
import java.util.List;

/**
 * StateManager upravlja trenutnim stanjem.
 */
@Getter
public class StateManager {

    private State currentState;

    private final SelectState selectState;
    private final AddImageState addImageState;
    private final AddTextState addTextState;
    private final AddLogoState addLogoState;
    private final MoveState moveState;
    private final DeleteState deleteState;
    private final ResizeState resizeState;
    private final ZoomState zoomState;
    private final PasteState pasteState;  // DODATO: za copy/paste funkcionalnost

    public StateManager() {
        selectState = new SelectState();
        addImageState = new AddImageState();
        addTextState = new AddTextState();
        addLogoState = new AddLogoState();
        moveState = new MoveState();
        deleteState = new DeleteState();
        resizeState = new ResizeState();
        zoomState = new ZoomState();
        pasteState = new PasteState();  // DODATO

        currentState = null;
    }

    private void setState(State newState) {
        if (currentState != null) {
            currentState.onStateDeactivated();
        }
        currentState = newState;
        currentState.onStateActivated();
    }

    public boolean isCurrentStateSelectState() {
        return currentState instanceof SelectState;
    }

    public void setSelectState() { setState(selectState); }
    public void setAddImageState() { setState(addImageState); }
    public void setAddTextState() { setState(addTextState); }
    public void setAddLogoState() { setState(addLogoState); }
    public void setMoveState() {
        List<SlideElement> elements = new ArrayList<>(selectState.getSelectedElements());
        moveState.setElementsToMove(elements);
        setState(moveState);
    }
    public void setDeleteState() {
        List<SlideElement> elements = new ArrayList<>(selectState.getSelectedElements());
        deleteState.setSelectedElements(elements);
        setState(deleteState);
        selectState.clearSelection();
    }
    public void setResizeState() {
        List<SlideElement> elements = new ArrayList<>(selectState.getSelectedElements());
        resizeState.setElementsToResize(elements);
        setState(resizeState);
    }
    public void setZoomState() { setState(zoomState); }
    public void setPasteState() { setState(pasteState); }
    public void setNullState() { currentState = null; }
}