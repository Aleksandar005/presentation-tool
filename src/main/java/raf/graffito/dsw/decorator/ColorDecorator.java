package raf.graffito.dsw.decorator;

import raf.graffito.dsw.model.graff.GraffNode;
import java.awt.*;
import lombok.*;

@Getter
@Setter
public class ColorDecorator extends GraffNodeDecorator{
    private Color color;

    public ColorDecorator(GraffNode decorated, Color color) {
        super(decorated);
        this.color = color;
    }

}
