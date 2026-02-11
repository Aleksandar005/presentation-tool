package raf.graffito.dsw.gui.swing;

import lombok.Getter;
import raf.graffito.dsw.controller.listeners.SlideMouseListener;
import raf.graffito.dsw.controller.listeners.SlideMouseMotionListener;
import raf.graffito.dsw.controller.listeners.SlideMouseWheelListener;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.model.implementation.slide.*;
import raf.graffito.dsw.observer.Subscriber;
import raf.graffito.dsw.painter.*;
import raf.graffito.dsw.state.StateManager;
import raf.graffito.dsw.state.concrete.SelectState;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

/**
 * SlideView implementira Subscriber i automatski se repaint-uje
 * kada Slide promeni (doda/ukloni element).
 */
public class SlideView extends JPanel implements Subscriber {

    public static final int SLIDE_WIDTH = 800;
    public static final int SLIDE_HEIGHT = 600;

    @Getter
    private double zoomLevel = 1.0;
    @Getter
    private double displayScale = 1.0;

    @Getter
    private List<SlideElementPainter> painters;

    @Getter
    private StateManager stateManager;

    @Getter
    private Slide currentSlide;

    public SlideView() {
        setBackground(new Color(200, 200, 200));

        painters = new ArrayList<>();
        stateManager = new StateManager();

        addMouseListener(new SlideMouseListener(stateManager, this));
        addMouseMotionListener(new SlideMouseMotionListener(stateManager, this));
        addMouseWheelListener(new SlideMouseWheelListener(this));

        ApplicationFramework.getInstance().getMessageGenerator().addSubscriber(this);
    }

    @Override
    public void update(Message message) {
        if (message.getSource() == currentSlide) {
            String content = message.getContent();
            if (content.equals("ELEMENT_ADDED") ||
                    content.equals("ELEMENT_REMOVED") ||
                    content.equals("ELEMENT_CHANGED")) {
                loadPaintersFromSlide();
                repaint();
            }
        }
    }

    public void setCurrentSlide(Slide slide) {
        this.currentSlide = slide;

        // Postavi trenutni slajd u CommandManager
        ApplicationFramework.getInstance().getCommandManager().setCurrentSlide(slide);

        loadPaintersFromSlide();
        repaint();
    }

    public void loadPaintersFromSlide() {
        painters.clear();
        if (currentSlide == null) return;

        for (var child : currentSlide.getChildren()) {
            if (child instanceof SlideElement) {
                SlideElementPainter painter = createPainterFor((SlideElement) child);
                if (painter != null) {
                    painters.add(painter);
                }
            }
        }
    }

    private SlideElementPainter createPainterFor(SlideElement element) {
        if (element instanceof ImageSlideElement) {
            return new ImageSlideElementPainter((ImageSlideElement) element);
        } else if (element instanceof TextSlideElement) {
            return new TextSlideElementPainter((TextSlideElement) element);
        } else if (element instanceof LogoSlideElement) {
            return new LogoSlideElementPainter((LogoSlideElement) element);
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Siva pozadina za ceo panel
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if (currentSlide == null) {
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("Arial", Font.PLAIN, 18));

            String message = "No slide selected";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(message)) / 2;
            int y = getHeight() / 2;

            g2d.drawString(message, x, y);
            return;
        }

        calculateDisplayScale();

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int scaledWidth = (int)(SLIDE_WIDTH * displayScale * zoomLevel);
        int scaledHeight = (int)(SLIDE_HEIGHT * displayScale * zoomLevel);
        int offsetX = (panelWidth - scaledWidth) / 2;
        int offsetY = (panelHeight - scaledHeight) / 2;

        // Senka ispod slajda
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillRect(offsetX + 5, offsetY + 5, scaledWidth, scaledHeight);

        g2d.translate(offsetX, offsetY);

        AffineTransform oldTransform = g2d.getTransform();
        g2d.scale(displayScale * zoomLevel, displayScale * zoomLevel);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Bela pozadina slajda
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, SLIDE_WIDTH, SLIDE_HEIGHT);

        drawGrid(g2d);

        // Okvir slajda
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(0, 0, SLIDE_WIDTH, SLIDE_HEIGHT);

        for (SlideElementPainter painter : painters) {
            painter.paint(g2d);
        }

        drawSelectionBorders(g2d);

        g2d.setTransform(oldTransform);
    }

    private void calculateDisplayScale() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        if (panelWidth == 0 || panelHeight == 0) {
            displayScale = 1.0;
            return;
        }

        double scaleX = (double) panelWidth / SLIDE_WIDTH;
        double scaleY = (double) panelHeight / SLIDE_HEIGHT;
        displayScale = Math.min(scaleX, scaleY) * 0.95;
    }

    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(new Color(240, 240, 240));
        g2d.setStroke(new BasicStroke(1));
        int gridSize = 50;

        for (int x = 0; x <= SLIDE_WIDTH; x += gridSize) {
            g2d.drawLine(x, 0, x, SLIDE_HEIGHT);
        }
        for (int y = 0; y <= SLIDE_HEIGHT; y += gridSize) {
            g2d.drawLine(0, y, SLIDE_WIDTH, y);
        }
    }

    public Point screenToSlideCoordinates(Point screenPoint) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int scaledWidth = (int)(SLIDE_WIDTH * displayScale * zoomLevel);
        int scaledHeight = (int)(SLIDE_HEIGHT * displayScale * zoomLevel);
        int offsetX = (panelWidth - scaledWidth) / 2;
        int offsetY = (panelHeight - scaledHeight) / 2;

        double scale = displayScale * zoomLevel;
        int slideX = (int)((screenPoint.x - offsetX) / scale);
        int slideY = (int)((screenPoint.y - offsetY) / scale);

        return new Point(slideX, slideY);
    }

    private void drawSelectionBorders(Graphics2D g2d) {
        if (stateManager.getCurrentState() instanceof SelectState) {
            SelectState selectState = (SelectState) stateManager.getCurrentState();

            List<SlideElement> selected = selectState.getSelectedElements();

            for (var element : selected) {
                drawRotatedSelectionBorder(g2d, element);
            }

            if (selectState.isMultiSelecting()) {
                Rectangle dragRect = selectState.createSelectionRectangle();
                if (dragRect != null) {
                    g2d.setColor(new Color(0, 120, 255, 50));
                    g2d.fillRect(dragRect.x, dragRect.y, dragRect.width, dragRect.height);

                    g2d.setColor(new Color(0, 120, 255));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRect(dragRect.x, dragRect.y, dragRect.width, dragRect.height);
                }
            }
        }
    }

    /**
     * Crta rotirani selekcioni okvir oko elementa koristeci AffineTransform.
     */
    private void drawRotatedSelectionBorder(Graphics2D g2d, SlideElement element) {
        Point loc = element.getLocation();
        Dimension dim = element.getDimension();
        double rotation = element.getRotation();

        int padding = 4;
        int x = loc.x - padding;
        int y = loc.y - padding;
        int width = dim.width + 2 * padding;
        int height = dim.height + 2 * padding;

        // Centar elementa za rotaciju
        double centerX = loc.x + dim.width / 2.0;
        double centerY = loc.y + dim.height / 2.0;

        // Sacuvaj trenutnu transformaciju
        AffineTransform oldTransform = g2d.getTransform();

        // Primeni rotaciju oko centra elementa
        g2d.rotate(Math.toRadians(rotation), centerX, centerY);

        // Crtaj okvir
        g2d.setColor(new Color(0, 120, 255));
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                0, new float[]{10, 5}, 0));
        g2d.drawRect(x, y, width, height);

        // Vrati originalnu transformaciju
        g2d.setTransform(oldTransform);
    }

    public void setZoomLevel(double zoom) {
        this.zoomLevel = Math.max(0.5, Math.min(3.0, zoom));
        repaint();
    }
}