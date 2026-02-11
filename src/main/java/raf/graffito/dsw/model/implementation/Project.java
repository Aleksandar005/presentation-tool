package raf.graffito.dsw.model.implementation;

import lombok.Setter;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.graff.GraffNodeComposite;
import lombok.Getter;
import raf.graffito.dsw.model.implementation.slide.Slide;

import java.io.File;

@Getter
@Setter
public class Project extends GraffNodeComposite {
    private int number;
    private String author;
    private boolean changed;
    private File filePath;

    public Project(GraffNode parent, String title) {
        super(parent,title);
        changed = false;
    }

    @Override
    public void addChild(GraffNode child){
        if (child == null) {
            throw new IllegalArgumentException("Child node cannot be null");
        }

        super.getChildren().add(child);
        updateNumber();
        changed = true;
    }

    @Override
    public void removeChild(GraffNode child) {
        super.getChildren().remove(child);
        updateNumber();
        changed = true;
    }

    private void updateNumber(){
        number = 0;
        for(GraffNode child : super.getChildren()){
            if(child instanceof Slide)
                number++;
            else
                number += ((Presentation) child).getSlideNumber();
        }
    }
}
