package raf.graffito.dsw.util;

import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.slide.ImageSlideElement;
import raf.graffito.dsw.model.implementation.slide.LogoSlideElement;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.model.implementation.slide.TextSlideElement;

// Generise konzistentna imena za SlideElement a ne neka random
public class ElementNameGenerator {
    public static String nextImageName(Slide slide){
        return nextIndexedName(slide, "Image ", ImageSlideElement.class);
    }

    public static String nextLogoName(Slide slide){
        return nextIndexedName(slide, "Logo ", LogoSlideElement.class);
    }

    public static String nextTextName(Slide slide){
        return nextIndexedName(slide, "Text ", TextSlideElement.class);
    }

    private static String nextIndexedName(Slide slide, String base, Class<?> elementType){
        String name = base;
        int index = 1;

        while(nameExists(slide, name, elementType)){
            name = base + index;
            index++;
        }

        return name;
    }

    private static boolean nameExists(Slide slide, String name, Class<?> elementType){
        for(GraffNode child : slide.getChildren()){
            if(elementType.isInstance(child) && child.getTitle().equals(name)){
                return true;
            }
        }

        return false;
    }
}
