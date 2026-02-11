package raf.graffito.dsw.bridge;

import javax.swing.*;

// Definiše tehničke operacije za skaliranje prozora.
public interface IWindowScaler {

    // Primenjuje skaliranje na prozor.
    void applyScale(JFrame frame, double scale);

    // Postavlja prozor na fullscreen režim.
    void applyFullscreen(JFrame frame);

    // Vraća originalnu (normalnu) veličinu prozora.
    void applyNormal(JFrame frame, int originalWidth, int originalHeight);
}