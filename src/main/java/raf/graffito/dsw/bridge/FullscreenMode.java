package raf.graffito.dsw.bridge;

import javax.swing.*;

// Fullscreen režim - prozor zauzima ceo ekran.
public class FullscreenMode extends WindowMode {

    public FullscreenMode(IWindowScaler scaler, int originalWidth, int originalHeight) {
        super(scaler, originalWidth, originalHeight);
    }

    @Override
    public void apply(JFrame frame) {
        scaler.applyFullscreen(frame);
    }

    @Override
    public String getModeName() {
        return "Fullscreen";
    }

    @Override
    public double getSlideScaleFactor() {
        // Fullscreen ima više prostora, slajd može biti veći
        return 1.5;
    }
}