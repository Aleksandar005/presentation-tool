package raf.graffito.dsw.image;

import lombok.Getter;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * ImageProxy implementira Proxy pattern za lazy loading slika.
 * Thumbnail se kreira odmah (mala verzija), dok se puna slika
 * učitava tek kada je potrebna (lazy loading).
 */
@Getter
public class ImageProxy {
    private static final int THUMBNAIL_SIZE = 80;

    private final String filePath;
    private final String fileName;
    private BufferedImage thumbnail;      // Učitava se odmah (mala verzija)
    private BufferedImage fullImage;      // Učitava se lazy (tek kad treba)
    private boolean fullImageLoaded;

    public ImageProxy(String filePath) {
        this.filePath = filePath;
        this.fileName = new File(filePath).getName();
        this.fullImageLoaded = false;

        // Odmah kreiraj thumbnail (brzo jer je mala slika)
        createThumbnail();
    }

    /**
     * Kreira thumbnail verziju slike.
     * Ovo se radi odmah pri kreiranju proxy-ja.
     */
    private void createThumbnail() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                ApplicationFramework.getInstance().getMessageGenerator().notify(
                        new Message("IMAGE NOT FOUND", MessageType.ERROR, this)
                );
                thumbnail = createPlaceholderThumbnail();
                return;
            }

            BufferedImage original = ImageIO.read(file);
            if (original == null) {
                thumbnail = createPlaceholderThumbnail();
                return;
            }

            // Skaliranje za thumbnail
            int originalWidth = original.getWidth();
            int originalHeight = original.getHeight();

            double scale = Math.min(
                    (double) THUMBNAIL_SIZE / originalWidth,
                    (double) THUMBNAIL_SIZE / originalHeight
            );

            int newWidth = (int) (originalWidth * scale);
            int newHeight = (int) (originalHeight * scale);

            thumbnail = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = thumbnail.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.drawImage(original, 0, 0, newWidth, newHeight, null);
            g2d.dispose();

            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("[ImageProxy] Thumbnail created for: " + fileName, MessageType.INFO, this)
            );

        } catch (Exception e) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("ERROR LOADING THE THUMBNAIL", MessageType.ERROR, this)
            );
        }
    }

    // Kreira placeholder thumbnail ako učitavanje ne uspe.
    private BufferedImage createPlaceholderThumbnail() {
        BufferedImage placeholder = new BufferedImage(THUMBNAIL_SIZE, THUMBNAIL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRect(0, 0, THUMBNAIL_SIZE - 1, THUMBNAIL_SIZE - 1);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g2d.drawString("Error", 25, 45);
        g2d.dispose();
        return placeholder;
    }

    /**
     * Vraća punu sliku - LAZY LOADING.
     * Slika se učitava tek pri prvom pozivu ove metode.
     */
    public BufferedImage getFullImage() {
        if (!fullImageLoaded) {
            loadFullImage();
        }
        return fullImage;
    }

    /**
     * Učitava punu sliku sa diska.
     * Ovo je "skupa" operacija koja se odlaže dok nije potrebna.
     */
    private void loadFullImage() {
        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                "[ImageProxy] LAZY LOADING - Loading full image: " + fileName, MessageType.INFO, this
        ));

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                ApplicationFramework.getInstance().getMessageGenerator().notify(
                        new Message("IMAGE FILE NOT FOUND: " + fileName, MessageType.ERROR, this)
                );
                fullImage = null;
                fullImageLoaded = true;
                return;
            }

            long startTime = System.currentTimeMillis();
            fullImage = ImageIO.read(file);
            long endTime = System.currentTimeMillis();

            fullImageLoaded = true;


        } catch (Exception e) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("ERROR LOADING IMAGE: " + e.getMessage(), MessageType.ERROR, this)
            );
            fullImage = null;
            fullImageLoaded = true;
        }
    }

    // Resetuje lazy loading - forsira ponovno učitavanje pri sledećem pristupu.
    public void resetFullImage() {
        fullImage = null;
        fullImageLoaded = false;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
