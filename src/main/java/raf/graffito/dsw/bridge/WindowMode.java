package raf.graffito.dsw.bridge;

import javax.swing.*;

// Apstrakcija za Bridge pattern.
public abstract class WindowMode {

    // Referenca na Implementatora (Most)
    protected IWindowScaler scaler;

    // Originalne dimenzije prozora (Normal režim)
    protected int originalWidth;
    protected int originalHeight;

    public WindowMode(IWindowScaler scaler, int originalWidth, int originalHeight) {
        this.scaler = scaler;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
    }

    // Primenjuje režim na prozor.
    public abstract void apply(JFrame frame);

    // Vraća naziv režima.
    public abstract String getModeName();

    // Vraća faktor skaliranja za SlideView.
    public abstract double getSlideScaleFactor();
}