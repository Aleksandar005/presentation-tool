package raf.graffito.dsw.model.implementation;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.graff.GraffNodeComposite;
import lombok.*;

@Getter
@Setter
public class Presentation extends GraffNodeComposite {
    private String description;
    private int slideNumber;

    public Presentation(GraffNode parent, String title) {
        super(parent, title);
        description = "";
    }

    public Presentation(GraffNode parent, String title, String description){
        super(parent,title);
        this.description = description;
    }

    @Override
    public void addChild(GraffNode child){
        if (child == null) {
            throw new IllegalArgumentException("Child node cannot be null");
        }

        super.getChildren().add(child);
        slideNumber++;
        notifyChange("CHILD_ADDED");
    }

    @Override
    public void removeChild(GraffNode child){
        if (getChildren().contains(child)) {
            super.getChildren().remove(child);
            slideNumber--;
        }
        notifyChange("CHILD_REMOVED");
    }

    private void notifyChange(String action) {
        Message message = new Message(action, MessageType.INFO, this);
        ApplicationFramework.getInstance().getMessageGenerator().notify(message);
        ((Project)(this.getParent())).setChanged(true);
    }
}
