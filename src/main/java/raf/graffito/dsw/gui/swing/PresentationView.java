package raf.graffito.dsw.gui.swing;

import lombok.Getter;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.observer.Subscriber;

import javax.swing.*;
import java.awt.*;


@Getter
public class PresentationView extends JPanel implements Subscriber {
    private Presentation presentation;
    private SlideView slideView;
    private SlideToolbar toolbar;
    private RightPanel rightPanel;
    private ImageThumbnailPanel imageThumbnailPanel;

    public PresentationView(Presentation presentation, RightPanel rightPanel) {
        super(new BorderLayout());
        this.presentation = presentation;
        this.rightPanel = rightPanel;

        // SlideView - canvas za prikaz
        slideView = new SlideView();

        // Toolbar - alati sa desne strane
        toolbar = new SlideToolbar();
        toolbar.connectToSlideView(slideView);

        // ImageThumbnailPanel - horizontalni panel ispod SlideView
        imageThumbnailPanel = new ImageThumbnailPanel(slideView);

        // Postavi projekat za ImageThumbnailPanel
        GraffNode parent = presentation.getParent();
        if (parent instanceof Project) {
            imageThumbnailPanel.setProject((Project) parent);
        }

        // Scroll pane za SlideView
        JScrollPane scrollPane = new JScrollPane(slideView);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Panel koji sadrži SlideView i ImageThumbnailPanel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(imageThumbnailPanel, BorderLayout.SOUTH);

        // SlideToolbar u JScrollPane za vertikalno skrolovanje kada je prozor mali
        JScrollPane toolbarScroll = new JScrollPane(toolbar);
        toolbarScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        toolbarScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        toolbarScroll.setBorder(null);
        toolbarScroll.getVerticalScrollBar().setUnitIncrement(16);

        // Layout: centerPanel (SlideView + Thumbnails) i Toolbar (desno) u scrollu
        add(centerPanel, BorderLayout.CENTER);
        add(toolbarScroll, BorderLayout.EAST);

        // Registruj subscriber
        ApplicationFramework.getInstance().getMessageGenerator().addSubscriber(this);
        putClientProperty("presentation", presentation);
    }


    @Override
    public void update(Message message) {
        Object src = message.getSource();

        // Ako je Slide preimenovan ili obrisan
        if (src instanceof Slide) {
            Slide slide = (Slide) src;

            // Proveri da li je to trenutni Slide u SlideView-u
            if (slideView.getCurrentSlide() == slide) {
                // Ažuriraj header
                rightPanel.setSlide(slide);
            }

            // Ako je Slide obrisan (parent = null)
            if (slide.getParent() == null && slideView.getCurrentSlide() == slide) {
                slideView.setCurrentSlide(null);
                rightPanel.setSlide(null);
            }
        }

        // Ako se promeni projekat ili biblioteka slika
        if (src instanceof Project) {
            GraffNode parent = presentation.getParent();
            if (parent == src) {
                imageThumbnailPanel.refreshThumbnails();
            }
        }

        // Jednostavno - samo repaint ako je ova Presentation
        if (src == presentation) {
            SwingUtilities.invokeLater(() -> {
                revalidate();
                repaint();
            });
        }
    }

    public void dispose() {
        ApplicationFramework.getInstance().getMessageGenerator().removeSubscriber(this);
    }

    /**
     * Osvežava ImageThumbnailPanel (poziva se kada se promeni projekat).
     */
    public void refreshImageLibrary() {
        GraffNode parent = presentation.getParent();
        if (parent instanceof Project) {
            imageThumbnailPanel.setProject((Project) parent);
        }
    }
}