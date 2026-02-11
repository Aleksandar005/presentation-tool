package raf.graffito.dsw.bridge;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

// Upravlja trenutnim režimom prozora.
@Getter
public class WindowModeManager {

    private final int originalWidth;
    private final int originalHeight;
    private final IWindowScaler scaler;

    @Setter
    private WindowMode currentMode;

    public WindowModeManager() {
        // Izračunaj originalne dimenzije (80% ekrana)
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        this.originalWidth = (int) (screenSize.width * 0.8);
        this.originalHeight = (int) (screenSize.height * 0.8);

        // Kreiraj Swing implementaciju skalera
        this.scaler = new SwingWindowScaler();

        // Podrazumevani režim je Normal
        this.currentMode = new NormalMode(scaler, originalWidth, originalHeight);
    }

    // Primenjuje trenutni režim na prozor.
    public void applyCurrentMode(JFrame frame) {
        if (currentMode != null) {
            currentMode.apply(frame);
        }
    }

    // Postavlja Normal režim.
    public void setNormalMode() {
        this.currentMode = new NormalMode(scaler, originalWidth, originalHeight);
    }

    // Postavlja Fullscreen režim.
    public void setFullscreenMode() {
        this.currentMode = new FullscreenMode(scaler, originalWidth, originalHeight);
    }

    // Postavlja Small režim.
    public void setSmallMode() {
        this.currentMode = new SmallMode(scaler, originalWidth, originalHeight);
    }

    // Vraća faktor skaliranja za SlideView.
    public double getSlideScaleFactor() {
        return currentMode != null ? currentMode.getSlideScaleFactor() : 1.0;
    }

    // Vraća naziv trenutnog režima.
    public String getCurrentModeName() {
        return currentMode != null ? currentMode.getModeName() : "Unknown";
    }
}