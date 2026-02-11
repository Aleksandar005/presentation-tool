package raf.graffito.dsw.gui.swing;

import lombok.Getter;
import lombok.Setter;
import raf.graffito.dsw.controller.SelectSlideFromListAction;
import raf.graffito.dsw.controller.SelectStandaloneSlideAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.observer.Subscriber;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SlideListPanel prikazuje listu slajdova u dva dela:
 * - Gornji deo: slajdovi selektovane prezentacije
 * - Donji deo: samostalni slajdovi projekta (direktna deca projekta)
 */
public class SlideListPanel extends JPanel implements Subscriber {

    @Getter @Setter
    private Presentation currentPresentation;

    @Getter @Setter
    private Project currentProject;

    private JPanel presentationSlidesContainer;
    private JPanel standaloneSlidesContainer;
    private JLabel presentationHeaderLabel;
    private JLabel standaloneHeaderLabel;
    private RightPanel rightPanel;

    @Getter @Setter
    private Slide selectedSlide;

    @Getter @Setter
    private Slide selectedStandaloneSlide;

    public SlideListPanel(RightPanel rightPanel) {
        super(new BorderLayout());
        this.rightPanel = rightPanel;

        setPreferredSize(new Dimension(180, 0));
        setBackground(new Color(245, 245, 245));

        // Glavni panel sa oba dela
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Gornji deo - slajdovi prezentacije
        JPanel presentationSection = createPresentationSection();

        // Donji deo - samostalni slajdovi
        JPanel standaloneSection = createStandaloneSection();

        mainPanel.add(presentationSection);
        mainPanel.add(standaloneSection);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

        ApplicationFramework.getInstance().getMessageGenerator().addSubscriber(this);
    }

    private JPanel createPresentationSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(new Color(245, 245, 245));

        // Header za slajdove prezentacije
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(new EmptyBorder(8, 10, 8, 10));

        presentationHeaderLabel = new JLabel("Slajdovi prezentacije");
        presentationHeaderLabel.setForeground(Color.WHITE);
        presentationHeaderLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        headerPanel.add(presentationHeaderLabel, BorderLayout.WEST);

        // Container za slajdove prezentacije
        presentationSlidesContainer = new JPanel();
        presentationSlidesContainer.setLayout(new BoxLayout(presentationSlidesContainer, BoxLayout.Y_AXIS));
        presentationSlidesContainer.setBackground(new Color(245, 245, 245));
        presentationSlidesContainer.setBorder(new EmptyBorder(5, 5, 5, 5));

        section.add(headerPanel, BorderLayout.NORTH);
        section.add(presentationSlidesContainer, BorderLayout.CENTER);

        return section;
    }

    private JPanel createStandaloneSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(new Color(245, 245, 245));

        // Header za samostalne slajdove
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 140, 0));
        headerPanel.setBorder(new EmptyBorder(8, 10, 8, 10));

        standaloneHeaderLabel = new JLabel("Samostalni slajdovi");
        standaloneHeaderLabel.setForeground(Color.WHITE);
        standaloneHeaderLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        headerPanel.add(standaloneHeaderLabel, BorderLayout.WEST);

        // Container za samostalne slajdove
        standaloneSlidesContainer = new JPanel();
        standaloneSlidesContainer.setLayout(new BoxLayout(standaloneSlidesContainer, BoxLayout.Y_AXIS));
        standaloneSlidesContainer.setBackground(new Color(245, 245, 245));
        standaloneSlidesContainer.setBorder(new EmptyBorder(5, 5, 5, 5));

        section.add(headerPanel, BorderLayout.NORTH);
        section.add(standaloneSlidesContainer, BorderLayout.CENTER);

        return section;
    }

    public void setPresentation(Presentation presentation) {
        this.currentPresentation = presentation;
        this.selectedSlide = null;
        refreshPresentationSlides();
    }

    public void setProject(Project project) {
        this.currentProject = project;
        this.selectedStandaloneSlide = null;
        refreshStandaloneSlides();
    }

    public void refreshSlides() {
        refreshPresentationSlides();
        refreshStandaloneSlides();
    }

    private void refreshPresentationSlides() {
        presentationSlidesContainer.removeAll();

        if (currentPresentation == null) {
            addNoSlidesMessage(presentationSlidesContainer, "Nema prezentacije");
        } else {
            List<Slide> slides = getSlidesFromPresentation(currentPresentation);

            if (slides.isEmpty()) {
                addNoSlidesMessage(presentationSlidesContainer, "Nema slajdova");
            } else {
                int index = 1;
                for (Slide slide : slides) {
                    JButton slideButton = createPresentationSlideButton(slide, index++);
                    presentationSlidesContainer.add(slideButton);
                    presentationSlidesContainer.add(Box.createVerticalStrut(5));
                }
            }
        }

        presentationSlidesContainer.revalidate();
        presentationSlidesContainer.repaint();
    }

    private void refreshStandaloneSlides() {
        standaloneSlidesContainer.removeAll();

        if (currentProject == null) {
            addNoSlidesMessage(standaloneSlidesContainer, "Nema projekta");
        } else {
            List<Slide> standaloneSlides = getStandaloneSlidesFromProject(currentProject);

            if (standaloneSlides.isEmpty()) {
                addNoSlidesMessage(standaloneSlidesContainer, "Nema samostalnih slajdova");
            } else {
                int index = 1;
                for (Slide slide : standaloneSlides) {
                    JButton slideButton = createStandaloneSlideButton(slide, index++);
                    standaloneSlidesContainer.add(slideButton);
                    standaloneSlidesContainer.add(Box.createVerticalStrut(5));
                }
            }
        }

        standaloneSlidesContainer.revalidate();
        standaloneSlidesContainer.repaint();
    }

    private JButton createPresentationSlideButton(Slide slide, int index) {
        JButton button = new JButton(index + ". " + slide.getTitle());
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        button.setPreferredSize(new Dimension(160, 35));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        boolean isSelected = slide == selectedSlide;
        if (isSelected) {
            button.setBackground(new Color(0, 120, 255));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.DARK_GRAY);
        }

        button.addActionListener(new SelectSlideFromListAction(slide, rightPanel));

        return button;
    }

    private JButton createStandaloneSlideButton(Slide slide, int index) {
        JButton button = new JButton(index + ". " + slide.getTitle());
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        button.setPreferredSize(new Dimension(160, 35));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        boolean isSelected = slide == selectedStandaloneSlide;
        if (isSelected) {
            button.setBackground(new Color(255, 140, 0));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.DARK_GRAY);
        }

        button.addActionListener(new SelectStandaloneSlideAction(slide, rightPanel));

        return button;
    }

    private void addNoSlidesMessage(JPanel container, String message) {
        JLabel noSlidesLabel = new JLabel(message);
        noSlidesLabel.setForeground(Color.GRAY);
        noSlidesLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        noSlidesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        noSlidesLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        container.add(noSlidesLabel);
    }

    private List<Slide> getSlidesFromPresentation(Presentation presentation) {
        List<Slide> slides = new ArrayList<>();
        for (GraffNode child : presentation.getChildren()) {
            if (child instanceof Slide) {
                slides.add((Slide) child);
            }
        }
        return slides;
    }

    private List<Slide> getStandaloneSlidesFromProject(Project project) {
        List<Slide> slides = new ArrayList<>();
        for (GraffNode child : project.getChildren()) {
            if (child instanceof Slide) {
                slides.add((Slide) child);
            }
        }
        return slides;
    }

    @Override
    public void update(Message message) {
        Object src = message.getSource();

        // Slajd dodat/uklonjen iz trenutne prezentacije
        if (src instanceof Slide slide && slide.getParent() == currentPresentation) {
            SwingUtilities.invokeLater(this::refreshPresentationSlides);
        }

        // Slajd dodat/uklonjen iz projekta (samostalni slajd)
        if (src instanceof Slide slide && slide.getParent() == currentProject) {
            SwingUtilities.invokeLater(this::refreshStandaloneSlides);
        }

        // Slajd prezentacije obrisan
        if (src instanceof Slide slide && slide == selectedSlide && slide.getParent() == null) {
            selectedSlide = null;
            SwingUtilities.invokeLater(this::refreshPresentationSlides);
        }

        // Samostalni slajd obrisan
        if (src instanceof Slide slide && slide == selectedStandaloneSlide && slide.getParent() == null) {
            selectedStandaloneSlide = null;
            SwingUtilities.invokeLater(this::refreshStandaloneSlides);
        }

        // Prezentacija promenjena
        if (src == currentPresentation) {
            SwingUtilities.invokeLater(this::refreshPresentationSlides);
        }

        // Projekat promenjen
        if (src == currentProject) {
            SwingUtilities.invokeLater(this::refreshStandaloneSlides);
        }
    }

    // Čisti selekciju slajdova prezentacije (kada se selektuje samostalni slajd).
    public void clearPresentationSelection() {
        this.selectedSlide = null;
        refreshPresentationSlides();
    }

    // Čisti selekciju samostalnih slajdova (kada se selektuje slajd prezentacije).
    public void clearStandaloneSelection() {
        this.selectedStandaloneSlide = null;
        refreshStandaloneSlides();
    }

    public void dispose() {
        ApplicationFramework.getInstance().getMessageGenerator().removeSubscriber(this);
    }
}