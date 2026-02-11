package raf.graffito.dsw.image;

import raf.graffito.dsw.model.implementation.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ImageLibrary čuva liste učitanih slika za svaki projekat.
 * Slično kao ColorPalette, mapira projekte na njihove biblioteke slika.
 */
public class ImageLibrary {
    private Map<Project, List<ImageProxy>> projectImages;

    public ImageLibrary() {
        this.projectImages = new HashMap<>();
    }

    /**
     * Vraća listu slika za dati projekat.
     * Ako projekat nema slika, vraća praznu listu.
     */
    public List<ImageProxy> getImages(Project project) {
        return projectImages.computeIfAbsent(project, k -> new ArrayList<>());
    }

    // Dodaje sliku u biblioteku projekta.
    public void addImage(Project project, ImageProxy image) {
        getImages(project).add(image);
    }

    // Dodaje više slika u biblioteku projekta.
    public void addImages(Project project, List<ImageProxy> images) {
        getImages(project).addAll(images);
    }

    // Uklanja sliku iz biblioteke projekta.
    public void removeImage(Project project, ImageProxy image) {
        getImages(project).remove(image);
    }

    // Briše sve slike za dati projekat.
    public void clearImages(Project project) {
        projectImages.remove(project);
    }

    // Proverava da li projekat ima učitanih slika.
    public boolean hasImages(Project project) {
        List<ImageProxy> images = projectImages.get(project);
        return images != null && !images.isEmpty();
    }

    // Vraća broj slika za dati projekat.
    public int getImageCount(Project project) {
        return getImages(project).size();
    }

    // Proverava da li je slika sa datom putanjom već učitana u projekat.
    public boolean containsImagePath(Project project, String path) {
        for (ImageProxy img : getImages(project)) {
            if (img.getFilePath().equals(path)) {
                return true;
            }
        }
        return false;
    }
}
