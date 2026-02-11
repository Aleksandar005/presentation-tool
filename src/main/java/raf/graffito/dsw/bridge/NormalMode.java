package raf.graffito.dsw.bridge;

import javax.swing.*;

// Normal režim
public class NormalMode extends WindowMode {

    public NormalMode(IWindowScaler scaler, int originalWidth, int originalHeight) {
        super(scaler, originalWidth, originalHeight);
    }

    @Override
    public void apply(JFrame frame) {
        scaler.applyNormal(frame, originalWidth, originalHeight);
    }

    @Override
    public String getModeName() {
        return "Normal";
    }

    @Override
    public double getSlideScaleFactor() {
        return 1.0;
    }
}