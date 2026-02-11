package raf.graffito.dsw.bridge;

import javax.swing.*;

// Small režim
public class SmallMode extends WindowMode {

    public SmallMode(IWindowScaler scaler, int originalWidth, int originalHeight) {
        super(scaler, originalWidth, originalHeight);
    }

    @Override
    public void apply(JFrame frame) {
        scaler.applyScale(frame, 0.5);
    }

    @Override
    public String getModeName() {
        return "Small";
    }

    @Override
    public double getSlideScaleFactor() {
        return 0.5;
    }
}