package raf.graffito.dsw.decorator;

import raf.graffito.dsw.model.implementation.Project;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class ColorPalette {
    private List<ColorDecorator> decorators = new ArrayList<>();

    public ColorDecorator get(Project p) {
        if (p == null) return null;
        for (ColorDecorator cd : decorators) {
            if (cd.getDecorated() == p) return cd;
        }
        return null;
    }

    public ColorDecorator findByColor(Color c) {
        if (c == null) return null;
        int rgb = c.getRGB();
        for (ColorDecorator cd : decorators) {
            Color have = cd.getColor();
            if (have != null && have.getRGB() == rgb) return cd;
        }
        return null;
    }

    public boolean hasColor(Color c) {
        return findByColor(c) != null;
    }

    public void set(Project p, Color c) {
        ColorDecorator cd = get(p);
        if (cd == null) {
            decorators.add(new ColorDecorator(p, c));
        } else {
            cd.setColor(c);
        }
    }

    public void remove(Project p) {
        ColorDecorator cd = get(p);
        if (cd != null) decorators.remove(cd);
    }

}
