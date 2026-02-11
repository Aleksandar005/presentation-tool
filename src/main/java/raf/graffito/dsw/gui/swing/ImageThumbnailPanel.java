package raf.graffito.dsw.gui.swing;

import lombok.Getter;
import lombok.Setter;
import raf.graffito.dsw.controller.slideElementActions.SelectImageFromLibraryAction;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.image.ImageLibrary;
import raf.graffito.dsw.image.ImageProxy;
import raf.graffito.dsw.model.implementation.Project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * ImageThumbnailPanel prikazuje horizontalnu listu učitanih slika za projekat.
 * Sadrži dugme "Učitaj Sliku" i scrollable listu thumbnail-a.
 */
public class ImageThumbnailPanel extends JPanel {

    private static final int PANEL_HEIGHT = 120;
    private static final int THUMBNAIL_SIZE = 80;
    private static final int THUMBNAIL_PADDING = 10;

    @Getter @Setter
    private Project currentProject;

    private JPanel thumbnailsContainer;
    private JScrollPane scrollPane;

    @Getter
    private SlideView slideView;

    @Getter @Setter
    private ImageProxy selectedImage;

    public ImageThumbnailPanel(SlideView slideView) {
        this.slideView = slideView;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, PANEL_HEIGHT));
        setBackground(new Color(60, 60, 60));
        setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(100, 100, 100)));

        // Registruj ovaj panel u ActionManager
        MainFrame.getInstance().getActionManager().initImageLibraryActions(this);

        createLeftPanel();
        createThumbnailsArea();
    }

    /**
     * Kreira levi panel sa labelom i dugmetom za učitavanje.
     */
    private void createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(70, 70, 70));
        leftPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        leftPanel.setPreferredSize(new Dimension(130, PANEL_HEIGHT));

        // Labela
        JLabel titleLabel = new JLabel("Biblioteka Slika");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Dugme za učitavanje - koristi akciju iz ActionManager-a
        JButton loadButton = new JButton(
                MainFrame.getInstance().getActionManager().getLoadImageToLibraryAction()
        );
        loadButton.setText("Učitaj Sliku");
        loadButton.setFont(new Font("SansSerif", Font.BOLD, 11));
        loadButton.setBackground(new Color(70, 130, 180));
        loadButton.setForeground(Color.WHITE);
        loadButton.setFocusPainted(false);
        loadButton.setBorderPainted(false);
        loadButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadButton.setMaximumSize(new Dimension(110, 35));

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(loadButton);
        leftPanel.add(Box.createVerticalGlue());

        add(leftPanel, BorderLayout.WEST);
    }

    /**
     * Kreira scrollable area za thumbnail-e.
     */
    private void createThumbnailsArea() {
        thumbnailsContainer = new JPanel();
        thumbnailsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, THUMBNAIL_PADDING, THUMBNAIL_PADDING));
        thumbnailsContainer.setBackground(new Color(50, 50, 50));

        scrollPane = new JScrollPane(thumbnailsContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(50, 50, 50));

        // Stilizovanje scrollbar-a
        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Osvežava prikaz thumbnail-a.
     */
    public void refreshThumbnails() {
        thumbnailsContainer.removeAll();

        if (currentProject == null) {
            addNoImagesMessage();
            thumbnailsContainer.revalidate();
            thumbnailsContainer.repaint();
            return;
        }

        ImageLibrary library = ApplicationFramework.getInstance().getImageLibrary();
        List<ImageProxy> images = library.getImages(currentProject);

        if (images.isEmpty()) {
            addNoImagesMessage();
        } else {
            for (ImageProxy imageProxy : images) {
                JButton thumbnailButton = createThumbnailButton(imageProxy);
                thumbnailsContainer.add(thumbnailButton);
            }
        }

        // Podesi preferred size za horizontalni scroll
        int width = images.size() * (THUMBNAIL_SIZE + THUMBNAIL_PADDING * 2 + 10) + THUMBNAIL_PADDING;
        thumbnailsContainer.setPreferredSize(new Dimension(width, PANEL_HEIGHT - 20));

        thumbnailsContainer.revalidate();
        thumbnailsContainer.repaint();
    }

    /**
     * Kreira dugme za jedan thumbnail.
     * SelectImageFromLibraryAction se kreira dinamički jer svaka slika ima svoju instancu.
     */
    private JButton createThumbnailButton(ImageProxy imageProxy) {
        // Kreiraj akciju za ovo dugme (dinamički, jer svaka slika ima svoju)
        SelectImageFromLibraryAction action = new SelectImageFromLibraryAction(
                imageProxy, this, slideView
        );

        JButton button = new JButton(action);
        button.setText(""); // Ukloni tekst, koristimo custom paint
        button.setPreferredSize(new Dimension(THUMBNAIL_SIZE + 10, THUMBNAIL_SIZE + 20));
        button.setMaximumSize(new Dimension(THUMBNAIL_SIZE + 10, THUMBNAIL_SIZE + 20));
        button.setBackground(new Color(50, 50, 50));
        button.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Custom paint za thumbnail
        button.setLayout(new BorderLayout(0, 2));

        // Panel za sliku
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Bela pozadina
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Crtaj thumbnail centriran
                if (imageProxy.getThumbnail() != null) {
                    int imgW = imageProxy.getThumbnail().getWidth();
                    int imgH = imageProxy.getThumbnail().getHeight();
                    int x = (getWidth() - imgW) / 2;
                    int y = (getHeight() - imgH) / 2;
                    g2d.drawImage(imageProxy.getThumbnail(), x, y, null);
                }

                // Okvir - plavi ako je selektovano
                boolean isSelected = imageProxy == selectedImage;
                g2d.setColor(isSelected ? new Color(0, 150, 255) : Color.GRAY);
                g2d.setStroke(new BasicStroke(isSelected ? 3 : 1));
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        imagePanel.setPreferredSize(new Dimension(THUMBNAIL_SIZE, THUMBNAIL_SIZE - 5));
        imagePanel.setOpaque(false);

        // Labela sa imenom
        String name = imageProxy.getFileName();
        if (name.length() > 12) {
            name = name.substring(0, 9) + "...";
        }
        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setForeground(Color.LIGHT_GRAY);
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 9));

        button.add(imagePanel, BorderLayout.CENTER);
        button.add(nameLabel, BorderLayout.SOUTH);

        // Tooltip sa punim imenom
        button.setToolTipText(imageProxy.getFileName());

        return button;
    }

    /**
     * Prikazuje poruku kada nema slika.
     */
    private void addNoImagesMessage() {
        JLabel noImagesLabel = new JLabel("Nema učitanih slika. Kliknite 'Učitaj Sliku' da dodate.");
        noImagesLabel.setForeground(Color.GRAY);
        noImagesLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        noImagesLabel.setBorder(new EmptyBorder(30, 20, 30, 20));
        thumbnailsContainer.add(noImagesLabel);
    }

    /**
     * Postavlja trenutni projekat i osvežava thumbnail-e.
     */
    public void setProject(Project project) {
        this.currentProject = project;
        this.selectedImage = null;
        refreshThumbnails();
    }
}