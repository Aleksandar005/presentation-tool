package raf.graffito.dsw.gui.swing;

import lombok.Getter;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.observer.Subscriber;

import javax.swing.*;
import java.awt.*;

/**
 * StandaloneSlideView prikazuje samostalni slajd (slajd koji je direktno dete projekta).
 * Slična struktura kao PresentationView, ali za jedan slajd.
 */
@Getter
public class StandaloneSlideView extends JPanel implements Subscriber {

    private Slide slide;
    private Project project;
    private SlideView slideView;
    private SlideToolbar toolbar;
    private ImageThumbnailPanel imageThumbnailPanel;

    public StandaloneSlideView(Slide slide, Project project) {
        super(new BorderLayout());
        this.slide = slide;
        this.project = project;

        // SlideView - canvas za prikaz
        slideView = new SlideView();
        slideView.setCurrentSlide(slide);

        // Toolbar - alati sa desne strane
        toolbar = new SlideToolbar();
        toolbar.connectToSlideView(slideView);

        // ImageThumbnailPanel - horizontalni panel ispod SlideView
        imageThumbnailPanel = new ImageThumbnailPanel(slideView);
        imageThumbnailPanel.setProject(project);

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
    }

    @Override
    public void update(Message message) {
        Object src = message.getSource();

        // Ako je ovaj slajd promenjen
        if (src == slide) {
            SwingUtilities.invokeLater(() -> {
                revalidate();
                repaint();
            });
        }

        // Ako je slajd obrisan (parent = null) - obavesti preko MessageGenerator-a
        if (src instanceof Slide s && s == slide && s.getParent() == null) {
            SwingUtilities.invokeLater(() -> {
                slideView.setCurrentSlide(null);
                // Pošalji poruku da je standalone slajd obrisan - RightPanel će reagovati
                ApplicationFramework.getInstance().getMessageGenerator()
                        .notify(new Message("STANDALONE_SLIDE_DELETED", MessageType.INFO, this));
            });
        }

        // Ako se promeni projekat ili biblioteka slika
        if (src == project) {
            imageThumbnailPanel.refreshThumbnails();
        }
    }

    public void dispose() {
        ApplicationFramework.getInstance().getMessageGenerator().removeSubscriber(this);
    }

    /**
     * Osvežava ImageThumbnailPanel.
     */
    public void refreshImageLibrary() {
        imageThumbnailPanel.setProject(project);
    }
}