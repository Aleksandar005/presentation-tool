package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.RightPanel;
import raf.graffito.dsw.model.implementation.slide.Slide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Akcija za selektovanje samostalnog slajda iz SlideListPanel-a.
public class SelectStandaloneSlideAction implements ActionListener {
    
    private final Slide slide;
    private final RightPanel rightPanel;

    public SelectStandaloneSlideAction(Slide slide, RightPanel rightPanel) {
        this.slide = slide;
        this.rightPanel = rightPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        rightPanel.showStandaloneSlide(slide);
    }
}