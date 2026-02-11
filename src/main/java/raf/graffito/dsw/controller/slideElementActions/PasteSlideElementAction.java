package raf.graffito.dsw.controller.slideElementActions;

import raf.graffito.dsw.controller.AbstractGraffAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;

import java.awt.event.ActionEvent;

// Akcija za aktiviranje PasteState režima.
public class PasteSlideElementAction extends AbstractGraffAction {

    private final SlideView slideView;

    public PasteSlideElementAction(SlideView slideView) {
        super("Paste", "Paste copied elements");
        this.slideView = slideView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Proveri da li ima nešto u clipboard-u
        if (!ApplicationFramework.getInstance().getClipboardManager().hasContent()) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("Clipboard is empty! Copy elements first.\n",
                            MessageType.WARNING, slideView)
            );
            return;
        }

        // Aktiviraj PasteState
        slideView.getStateManager().setPasteState();

        ApplicationFramework.getInstance().getMessageGenerator().notify(
                new Message("Paste mode activated - click on the slide to paste elements",
                        MessageType.INFO, slideView)
        );
    }
}