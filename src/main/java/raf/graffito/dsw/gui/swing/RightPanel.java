package raf.graffito.dsw.gui.swing;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.decorator.ColorDecorator;
import raf.graffito.dsw.decorator.ColorPalette;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.graff.GraffNodeComposite;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.observer.Subscriber;

import javax.swing.*;
import java.awt.*;
import lombok.*;

public class RightPanel extends JPanel implements Subscriber {
    private final JLabel lblPresentation = new JLabel("Presentation: -");
    private final JLabel lblProject = new JLabel("Project: -");
    private final JLabel lblAuthor = new JLabel("Author: -");
    private final JLabel lblSlide = new JLabel("Slide: -");

    @Getter
    private final JTabbedPane tabs = new JTabbedPane();
    @Getter @Setter
    private Project currentProject;
    @Getter @Setter
    private ProjectView projectView;

    @Getter
    private SlideListPanel slideListPanel;

    private JSplitPane splitPane;
    private JPanel centerPanel;

    // Panel koji sadrži tabove na vrhu i content ispod
    private JPanel tabsAndContentPanel;

    // Panel za sadržaj ispod tabova (PresentationView ili StandaloneSlideView)
    private JPanel contentBelowTabs;

    // Za prikaz samostalnog slajda
    @Getter
    private StandaloneSlideView standaloneSlideView;
    private boolean showingStandaloneSlide = false;

    // Flag da sprečimo rekurzivne pozive
    private boolean updatingTabs = false;

    public RightPanel() {
        super(new BorderLayout());

        JPanel header = new JPanel(new GridLayout(1, 4));
        header.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        header.add(lblPresentation);
        header.add(lblProject);
        header.add(lblAuthor);
        header.add(lblSlide);

        slideListPanel = new SlideListPanel(this);

        // Panel koji sadrži tabove na vrhu i content ispod
        tabsAndContentPanel = new JPanel(new BorderLayout());

        // Content panel ispod tabova
        contentBelowTabs = new JPanel(new BorderLayout());

        // Tabovi na vrhu, content ispod
        tabsAndContentPanel.add(tabs, BorderLayout.NORTH);
        tabsAndContentPanel.add(contentBelowTabs, BorderLayout.CENTER);

        // Početno stanje - samo tabsAndContentPanel, bez SlideListPanel
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(tabsAndContentPanel, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        tabs.addChangeListener(e -> {
            if (updatingTabs) return;

            int i = tabs.getSelectedIndex();
            if (i < 0) {
                // Nijedan tab nije selektovan
                if (!showingStandaloneSlide) {
                    setPresentation(null);
                    slideListPanel.setPresentation(null);
                    // Očisti content ispod tabova
                    contentBelowTabs.removeAll();
                    contentBelowTabs.revalidate();
                    contentBelowTabs.repaint();
                }
                return;
            }

            // Tab je selektovan - sakri standalone view ako postoji
            if (showingStandaloneSlide) {
                hideStandaloneSlideViewInternal();
            }

            // Dobavi PresentationView iz client property
            PresentationView pv = getPresentationViewAt(i);

            if (pv != null) {
                // Prikaži PresentationView u content panelu
                contentBelowTabs.removeAll();
                contentBelowTabs.add(pv, BorderLayout.CENTER);
                contentBelowTabs.revalidate();
                contentBelowTabs.repaint();

                Presentation pres = pv.getPresentation();
                setPresentation(pres);
                slideListPanel.setPresentation(pres);
                slideListPanel.clearStandaloneSelection();

                // Postavi selektovani slajd iz SlideView-a ove prezentacije
                Slide currentSlide = pv.getSlideView().getCurrentSlide();
                slideListPanel.setSelectedSlide(currentSlide);
                slideListPanel.refreshSlides();

                if (currentSlide != null) {
                    setSlide(currentSlide);
                } else {
                    setSlide(null);
                }
            }
        });

        // Registruj kao subscriber za poruke (npr. kada se obriše standalone slajd)
        ApplicationFramework.getInstance().getMessageGenerator().addSubscriber(this);
    }

    @Override
    public void update(Message message) {
        // Reaguj na brisanje standalone slajda
        if (message.getContent().equals("STANDALONE_SLIDE_DELETED") &&
                message.getSource() == standaloneSlideView) {
            SwingUtilities.invokeLater(this::hideStandaloneSlideView);
        }
    }

    private void showSlideListPanel() {
        centerPanel.removeAll();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, slideListPanel, tabsAndContentPanel);
        splitPane.setDividerLocation(180);
        splitPane.setResizeWeight(0);
        centerPanel.add(splitPane, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void hideSlideListPanel() {
        centerPanel.removeAll();
        centerPanel.add(tabsAndContentPanel, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    public void setProject(Project p) {
        if (p == null) {
            lblProject.setText("Project: -");
            return;
        }
        this.currentProject = p;
        lblProject.setText("Project: " + p.getTitle());
    }

    public void showProject(Project p) {
        ColorPalette pal = ApplicationFramework.getInstance().getColorPallete();

        ColorDecorator cd = pal.get(p);
        if (cd == null || cd.getColor() == null) {
            Color chosen = chooseFreeColor(pal);
            if (chosen == null) return;
            pal.set(p, chosen);
            cd = pal.get(p);
        }

        this.currentProject = p;
        setProject(p);
        setAuthor(p);

        // Prikaži SlideListPanel kad se otvori projekat
        showSlideListPanel();

        // Postavi projekat za SlideListPanel (za samostalne slajdove)
        slideListPanel.setProject(p);

        buildTabsFor(p);

        paintTabsWith(cd.getColor());

        if (projectView != null) projectView.dispose();
        projectView = new ProjectView(p, this, tabs, lblProject);
    }

    private Color chooseFreeColor(ColorPalette pal) {
        while (true) {
            Color chosen = JColorChooser.showDialog(
                    SwingUtilities.getWindowAncestor(this),
                    "Choose project color",
                    Color.decode("#89CFF0"));

            if (chosen == null) return null;

            if (!pal.hasColor(chosen)) {
                return chosen;
            }

            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("THAT COLOR ALREADY EXISTS", MessageType.WARNING, currentProject));
        }
    }

    private void paintTabsWith(Color c) {
        if (c == null) return;

        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.setBackgroundAt(i, c);
        }
        tabs.setOpaque(true);
    }

    public void buildTabsFor(Project p) {
        updatingTabs = true;

        for (int i = 0; i < tabs.getTabCount(); i++) {
            Component c = tabs.getComponentAt(i);
            if (c instanceof PresentationView pv) {
                pv.dispose();
            }
        }
        tabs.removeAll();

        if (p != null) {
            for (GraffNode child : ((GraffNodeComposite) p).getChildren()) {
                if (child instanceof Presentation pr) {
                    PresentationView editor = new PresentationView(pr, this);
                    // Dodaj tab sa praznim panelom kao placeholder
                    tabs.addTab(pr.getTitle(), new JPanel());
                    // Sačuvaj PresentationView kao client property
                    tabs.putClientProperty("pv_" + (tabs.getTabCount() - 1), editor);
                }
            }
        }

        updatingTabs = false;

        if (tabs.getTabCount() > 0) {
            tabs.setSelectedIndex(0);

            // Prikaži prvi PresentationView
            PresentationView pv = (PresentationView) tabs.getClientProperty("pv_0");
            if (pv != null) {
                contentBelowTabs.removeAll();
                contentBelowTabs.add(pv, BorderLayout.CENTER);
                contentBelowTabs.revalidate();
                contentBelowTabs.repaint();

                lblPresentation.setText("Presentation: " + pv.getPresentation().getTitle());
                slideListPanel.setPresentation(pv.getPresentation());

                Presentation presentation = pv.getPresentation();
                for (GraffNode child : presentation.getChildren()) {
                    if (child instanceof Slide firstSlide) {
                        pv.getSlideView().setCurrentSlide(firstSlide);
                        setSlide(firstSlide);
                        slideListPanel.setSelectedSlide(firstSlide);
                        slideListPanel.refreshSlides();
                        break;
                    }
                }
            }
        } else {
            lblPresentation.setText("Presentation: -");
            slideListPanel.setPresentation(null);
            contentBelowTabs.removeAll();
            contentBelowTabs.revalidate();
            contentBelowTabs.repaint();
        }

        if (p != null) {
            ColorDecorator cd = ApplicationFramework.getInstance()
                    .getColorPallete()
                    .get(p);
            if (cd != null && cd.getColor() != null) {
                paintTabsWith(cd.getColor());
            }
        }
    }

    /**
     * Vraća PresentationView za dati indeks taba.
     */
    public PresentationView getPresentationViewAt(int index) {
        return (PresentationView) tabs.getClientProperty("pv_" + index);
    }

    /**
     * Prikazuje samostalni slajd - odselektuje tabove i prikazuje StandaloneSlideView ispod.
     */
    public void showStandaloneSlide(Slide slide) {
        if (slide == null || currentProject == null) return;

        updatingTabs = true;

        // Odselektuj tabove (nijedan tab nije aktivan)
        tabs.setSelectedIndex(-1);

        updatingTabs = false;

        // Očisti selekciju slajdova prezentacije
        slideListPanel.clearPresentationSelection();
        slideListPanel.setPresentation(null);

        // Postavi selektovani samostalni slajd
        slideListPanel.setSelectedStandaloneSlide(slide);
        slideListPanel.refreshSlides();

        // Dispose starog standalone view-a ako postoji
        if (standaloneSlideView != null) {
            standaloneSlideView.dispose();
        }

        // Kreiraj novi StandaloneSlideView (bez reference na RightPanel)
        standaloneSlideView = new StandaloneSlideView(slide, currentProject);

        // Prikaži StandaloneSlideView u content panelu ispod tabova
        contentBelowTabs.removeAll();
        contentBelowTabs.add(standaloneSlideView, BorderLayout.CENTER);
        contentBelowTabs.revalidate();
        contentBelowTabs.repaint();

        showingStandaloneSlide = true;

        // Ažuriraj header
        lblPresentation.setText("Samostalni slajd");
        setSlide(slide);
    }

    /**
     * Sakriva StandaloneSlideView i vraća na prvi tab.
     */
    public void hideStandaloneSlideView() {
        hideStandaloneSlideViewInternal();

        // Selektuj prvi tab ako postoji
        if (tabs.getTabCount() > 0) {
            tabs.setSelectedIndex(0);
        }
    }

    private void hideStandaloneSlideViewInternal() {
        if (standaloneSlideView != null) {
            standaloneSlideView.dispose();
            standaloneSlideView = null;
        }

        showingStandaloneSlide = false;

        // Očisti selekciju samostalnih slajdova
        slideListPanel.clearStandaloneSelection();
    }

    public void setAuthor(Project p) {
        if (p == null || p.getAuthor() == null || p.getAuthor().isBlank()) {
            lblAuthor.setText("Author: -");
        } else {
            lblAuthor.setText("Author: " + p.getAuthor());
        }
    }

    public void setPresentation(Presentation pr) {
        lblPresentation.setText("Presentation: " + (pr != null ? pr.getTitle() : "-"));
    }

    public void setSlide(Slide slide) {
        lblSlide.setText("Slide: " + (slide != null ? slide.getTitle() : "-"));
    }
}