package raf.graffito.dsw.command;

import lombok.Setter;
import raf.graffito.dsw.command.snapshots.SlideSnapshot;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.model.implementation.slide.SlideElement;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CommandManager {
    // Mape koje čuvaju undo/redo stackove za svaki slajd
    private Map<Slide, Stack<SlideSnapshot>> undoStacks = new HashMap<>();
    private Map<Slide, Stack<SlideSnapshot>> redoStacks = new HashMap<>();

    @Setter
    private Slide currentSlide;
    private boolean isUndoingOrRedoing = false;


    public void saveStateBeforeChange() {
        if (currentSlide == null || isUndoingOrRedoing) return;

        SlideSnapshot snapshot = createSnapshot();
        getUndoStack().push(snapshot);
        getRedoStack().clear(); // Čisti redo samo za trenutni slajd
    }

    public void undo() {
        Stack<SlideSnapshot> undoStack = getUndoStack();

        if (undoStack.isEmpty() || currentSlide == null) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("NOTHING TO UNDO", MessageType.WARNING, this));
            return;
        }

        isUndoingOrRedoing = true;

        SlideSnapshot currentSnapshot = createSnapshot();
        getRedoStack().push(currentSnapshot);

        SlideSnapshot previousSnapshot = undoStack.pop();
        restoreSnapshot(previousSnapshot);

        isUndoingOrRedoing = false;
        currentSlide.notifyElementChanged();

        ApplicationFramework.getInstance().getMessageGenerator().notify(
                new Message("UNDO EXECUTED", MessageType.INFO, this));
    }

    public void redo() {
        Stack<SlideSnapshot> redoStack = getRedoStack();

        if (redoStack.isEmpty() || currentSlide == null) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("NOTHING TO REDO", MessageType.WARNING, this));
            return;
        }

        isUndoingOrRedoing = true;

        SlideSnapshot currentSnapshot = createSnapshot();
        getUndoStack().push(currentSnapshot);

        SlideSnapshot redoSnapshot = redoStack.pop();
        restoreSnapshot(redoSnapshot);

        isUndoingOrRedoing = false;
        currentSlide.notifyElementChanged();

        ApplicationFramework.getInstance().getMessageGenerator().notify(
                new Message("REDO EXECUTED", MessageType.INFO, this));
    }

    // Briše istoriju za određeni slajd (korisno kada se slajd obriše)
    public void clearHistoryForSlide(Slide slide) {
        undoStacks.remove(slide);
        redoStacks.remove(slide);
    }

    // Vraća undo stack za trenutni slajd (kreira novi ako ne postoji)
    private Stack<SlideSnapshot> getUndoStack() {
        if (currentSlide == null) return new Stack<>();
        return undoStacks.computeIfAbsent(currentSlide, k -> new Stack<>());
    }

    // Vraća redo stack za trenutni slajd (kreira novi ako ne postoji)
    private Stack<SlideSnapshot> getRedoStack() {
        if (currentSlide == null) return new Stack<>();
        return redoStacks.computeIfAbsent(currentSlide, k -> new Stack<>());
    }


    private SlideSnapshot createSnapshot() {
        SlideSnapshot snapshot = new SlideSnapshot();
        List<SlideElement> elements = getElementsFromSlide();

        for (SlideElement element : elements) {
            snapshot.addElementState(element,
                    new Point(element.getLocation()),
                    new Dimension(element.getDimension()),
                    element.getRotation());
        }
        snapshot.setElementList(new ArrayList<>(elements));
        return snapshot;
    }

    private void restoreSnapshot(SlideSnapshot snapshot) {
        List<SlideElement> currentElements = getElementsFromSlide();
        List<SlideElement> snapshotElements = snapshot.getElementList();

        for (SlideElement element : new ArrayList<>(currentElements)) {
            if (!snapshotElements.contains(element)) {
                currentSlide.removeChild(element);
            }
        }

        for (SlideElement element : snapshotElements) {
            if (!currentElements.contains(element)) {
                currentSlide.addChild(element);
            }
        }

        snapshot.restoreElementStates();
    }

    private List<SlideElement> getElementsFromSlide() {
        List<SlideElement> elements = new ArrayList<>();
        if (currentSlide != null) {
            for (GraffNode child : currentSlide.getChildren()) {
                if (child instanceof SlideElement) {
                    elements.add((SlideElement) child);
                }
            }
        }
        return elements;
    }
}
