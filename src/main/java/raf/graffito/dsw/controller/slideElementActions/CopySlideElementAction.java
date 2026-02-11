package raf.graffito.dsw.controller.slideElementActions;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.implementation.slide.SlideElement;
import raf.graffito.dsw.state.concrete.SelectState;

import java.awt.event.ActionEvent;
import java.util.List;

// Akcija za kopiranje selektovanih elemenata u clipboard.
public class CopySlideElementAction extends AbstractGraffAction {

    private final SlideView slideView;

    public CopySlideElementAction(SlideView slideView) {
        super("Copy", "Copy selected elements");
        this.slideView = slideView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Proveri da li je SelectState aktivan
        if (!slideView.getStateManager().isCurrentStateSelectState()) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("You must be in Select mode to copy elements!",
                            MessageType.WARNING, slideView)
            );
            return;
        }

        SelectState selectState = (SelectState) slideView.getStateManager().getCurrentState();
        List<SlideElement> selectedElements = selectState.getSelectedElements();

        if (selectedElements.isEmpty()) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("No element is selected!", MessageType.WARNING, slideView)
            );
            return;
        }

        // Kopiraj elemente u clipboard
        ApplicationFramework.getInstance().getClipboardManager().copy(selectedElements);

        ApplicationFramework.getInstance().getMessageGenerator().notify(
                new Message("Copied " + selectedElements.size() + " elements",
                        MessageType.INFO, slideView)
        );
    }
}