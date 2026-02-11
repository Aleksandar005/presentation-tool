package raf.graffito.dsw.clipboard;

import lombok.Getter;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.implementation.slide.SlideElement;
import raf.graffito.dsw.prototype.Prototype;

import java.util.ArrayList;
import java.util.List;

// ClipboardManager čuva kopirane elemente (odmah se prave elementi, jer ako se originali obrisu potrebno je da kopije i dalje budu prisutne)
public class ClipboardManager {

    @Getter
    private List<SlideElement> copiedElements = new ArrayList<>();

    // Kopira elemente - odmah ih klonira i čuva klonove.
    public void copy(List<SlideElement> elements) {
        copiedElements.clear();

        for (SlideElement element : elements) {
            // Kloniraj odmah pri kopiranju
            SlideElement clone = (SlideElement) element.clone();
            copiedElements.add(clone);
        }

        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                "[ClipboardManager] Kopirano " + copiedElements.size() + " elemenata", MessageType.INFO, this
        ));
    }

    /**
     * Vraća nove klonove kopiranih elemenata za paste.
     * Svaki put se prave novi klonovi, tako da se može paste-ovati više puta.
     */
    public List<SlideElement> getClonedElementsForPaste() {
        List<SlideElement> clones = new ArrayList<>();

        for (SlideElement element : copiedElements) {
            SlideElement clone = (SlideElement) element.clone();
            clones.add(clone);
        }

        return clones;
    }

    // Proverava da li ima nešto u clipboard-u.
    public boolean hasContent() {
        return !copiedElements.isEmpty();
    }

    // Briše sadržaj clipboard-a.
    public void clear() {
        copiedElements.clear();
    }

    // Vraća broj kopiranih elemenata.
    public int getCount() {
        return copiedElements.size();
    }
}