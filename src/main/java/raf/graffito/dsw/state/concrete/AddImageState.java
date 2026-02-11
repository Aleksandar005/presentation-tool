package raf.graffito.dsw.state.concrete;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.implementation.slide.ImageSlideElement;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.state.State;
import raf.graffito.dsw.strategy.SpaceChecker;
import raf.graffito.dsw.util.ElementNameGenerator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.Element;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


public class AddImageState implements State {

    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 300;

    // ✅ Putanja se postavlja SPOLJA!
    private String imagePath;
    private BufferedImage cachedImage;

    public void setImagePath(String imagePath) {
        // Ako se putanja promenila, resetuj cache
        if (!imagePath.equals(this.imagePath)) {
            this.cachedImage = null;
        }
        this.imagePath = imagePath;
    }

    @Override
    public boolean handleMousePressed(Point slidePoint, Slide currentSlide) {
        return false;
    }

    @Override
    public boolean handleMouseDragged(Point slidePoint, Slide currentSlide) {
        return false;
    }

    @Override
    public boolean handleMouseReleased(Point slidePoint, Slide currentSlide) {
        return false;
    }

    @Override
    public boolean handleMouseClicked(Point slidePoint, Slide currentSlide) {
        if (currentSlide == null) return false;

        if (imagePath == null || imagePath.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Putanja slike nije postavljena!",
                    "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // PROVERA PROSTORA - koristi Strategy pattern
        SpaceChecker spaceChecker = ApplicationFramework.getInstance().getSpaceChecker();
        if (!spaceChecker.hasEnoughSpace(currentSlide)) {
            ApplicationFramework.getInstance().getMessageGenerator()
                    .notify(new Message(spaceChecker.getErrorMessage(currentSlide), MessageType.ERROR, currentSlide));
            return false;
        }


        // Učitaj sliku ako još nije
        if (cachedImage == null) {
            cachedImage = loadImage(imagePath);
            if (cachedImage == null) return false;
        }

        // Skaliranje
        BufferedImage scaledImage = scaleImageIfNeeded(cachedImage);

        Point centeredLocation = new Point(
                slidePoint.x - scaledImage.getWidth() / 2,
                slidePoint.y - scaledImage.getHeight() / 2
        );

        Dimension dimension = new Dimension(scaledImage.getWidth(), scaledImage.getHeight());
        if (!isWithinBounds(centeredLocation, dimension)) {
            ApplicationFramework.getInstance().getMessageGenerator()
                    .notify(new Message("IMAGE WOULD BE OUT OF SLIDE BOUNDS", MessageType.WARNING, currentSlide));
            return false;
        }

        ApplicationFramework.getInstance().getCommandManager().saveStateBeforeChange();

        // Kreiraj element na poziciji klika
        ImageSlideElement element = new ImageSlideElement(
                currentSlide,
                ElementNameGenerator.nextImageName(currentSlide),
                scaledImage,
                centeredLocation
        );
        element.setPath(imagePath);


        // Dodaj na slajd
        currentSlide.addChild(element);

        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message("[AddImageState] PICTURE ADDED FROM: " + imagePath,
                MessageType.INFO, currentSlide));
        return false;
    }

    private BufferedImage loadImage(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }
            return ImageIO.read(file);
        } catch (Exception ex) {
            ApplicationFramework.getInstance().getMessageGenerator()
                    .notify(new Message("ERROR IN LOADING IMAGE", MessageType.WARNING, null));
            return null;
        }
    }

    private BufferedImage scaleImageIfNeeded(BufferedImage original) {
        int w = original.getWidth();
        int h = original.getHeight();

        if (w <= MAX_WIDTH && h <= MAX_HEIGHT) return original;

        double scale = Math.min((double)MAX_WIDTH/w, (double)MAX_HEIGHT/h);
        int newW = (int)(w * scale);
        int newH = (int)(h * scale);

        BufferedImage scaled = new BufferedImage(newW, newH, original.getType());
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, newW, newH, null);
        g.dispose();

        return scaled;
    }

    private boolean isWithinBounds(Point location, Dimension dimension) {
        if (location.x < 0 || location.y < 0) {
            return false;
        }

        if (location.x + dimension.width > SlideView.SLIDE_WIDTH ||
                location.y + dimension.height > SlideView.SLIDE_HEIGHT) {
            return false;
        }

        return true;
    }

    @Override
    public void onStateActivated() {
        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                "[AddImageState] Activated - image: " + imagePath, MessageType.INFO, this
        ));
    }

    @Override
    public void onStateDeactivated() {

    }
}