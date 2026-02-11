package raf.graffito.dsw.model.implementation.slide;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.graff.GraffNodeComposite;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Project;

public class Slide extends GraffNodeComposite {

    public Slide(GraffNode parent, String title) {
        super(parent, title);
    }

    @Override
    public void addChild(GraffNode child) {
        if (child == null) {
            throw new IllegalArgumentException("Child cannot be null");
        }

        if (!(child instanceof SlideElement)) {
            throw new IllegalArgumentException("Slide can only contain SlideElement");
        }

        // Dodaj u model
        super.getChildren().add(child);

        // Notifikuj preko MessageGenerator-a!
        notifyChange("ELEMENT_ADDED");
    }

    @Override
    public void removeChild(GraffNode child) {
        if (super.getChildren().contains(child)) {
            super.getChildren().remove(child);

            // Notifikuj preko MessageGenerator-a!
            notifyChange("ELEMENT_REMOVED");
        }
    }

    // Notifikacija kada se element promeni (move, resize, rotate).
    public void notifyElementChanged() {
        notifyChange("ELEMENT_CHANGED");
    }

    // Šalje poruku preko MessageGenerator-a.
    private void notifyChange(String action) {
        Message message = new Message(action, MessageType.INFO, this);
        ApplicationFramework.getInstance().getMessageGenerator().notify(message);
        GraffNode parent = this.getParent();

        if(parent instanceof Project){
            ((Project) parent).setChanged(true);
        } else {
            Project parentOfParent = (Project) parent.getParent();
            parentOfParent.setChanged(true);
        }
    }
}