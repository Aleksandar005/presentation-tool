package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.PresentationView;
import raf.graffito.dsw.gui.swing.RightPanel;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.slide.Slide;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Akcija za selektovanje slajda prezentacije iz SlideListPanel-a.
public class SelectSlideFromListAction implements ActionListener {

    private final Slide slide;
    private final RightPanel rightPanel;

    public SelectSlideFromListAction(Slide slide, RightPanel rightPanel) {
        this.slide = slide;
        this.rightPanel = rightPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTabbedPane tabs = rightPanel.getTabs();
        Presentation slidePresentation = (Presentation) slide.getParent();

        // Pronađi tab koji odgovara prezentaciji ovog slajda
        for (int i = 0; i < tabs.getTabCount(); i++) {
            PresentationView pv = rightPanel.getPresentationViewAt(i);
            if (pv != null && pv.getPresentation() == slidePresentation) {
                // Selektuj tab (ovo će automatski prikazati PresentationView)
                tabs.setSelectedIndex(i);

                // Postavi slajd u SlideView
                pv.getSlideView().setCurrentSlide(slide);

                // Ažuriraj SlideListPanel
                rightPanel.getSlideListPanel().setSelectedSlide(slide);
                rightPanel.getSlideListPanel().clearStandaloneSelection();
                rightPanel.getSlideListPanel().refreshSlides();

                // Ažuriraj header
                rightPanel.setSlide(slide);

                break;
            }
        }
    }
}