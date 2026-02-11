package raf.graffito.dsw.bridge;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;

import javax.swing.*;
import java.awt.*;

// Konkretna implementacija IWindowScaler za Swing.
public class SwingWindowScaler implements IWindowScaler {

    @Override
    public void applyScale(JFrame frame, double scale) {
        // Dobavi veličinu ekrana
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();

        // Izračunaj normalnu veličinu (80% ekrana)
        int normalWidth = (int) (screenSize.width);
        int normalHeight = (int) (screenSize.height);

        // Primeni skaliranje
        int newWidth = (int) (normalWidth * scale);
        int newHeight = (int) (normalHeight * scale);

        // Vrati iz maximized stanja ako je potrebno
        frame.setExtendedState(JFrame.NORMAL);

        // Postavi novu veličinu
        frame.setSize(newWidth, newHeight);
        frame.setLocationRelativeTo(null); // Centriraj

        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                "[SwingWindowScaler] Primenjena skala: " + scale + " (" + newWidth + "x" + newHeight + ")", MessageType.INFO, MainFrame.getInstance()
        ));
    }

    @Override
    public void applyFullscreen(JFrame frame) {
        // Maximized window (sa taskbarom)
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                "[SwingWindowScaler] Fullscreen režim aktiviran", MessageType.INFO, MainFrame.getInstance()
        ));
    }

    @Override
    public void applyNormal(JFrame frame, int originalWidth, int originalHeight) {
        // Vrati iz maximized stanja
        frame.setExtendedState(JFrame.NORMAL);

        // Postavi originalnu veličinu
        frame.setSize(originalWidth, originalHeight);
        frame.setLocationRelativeTo(null); // Centriraj


        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                "[SwingWindowScaler] Normal režim: " + originalWidth + "x" + originalHeight, MessageType.INFO, MainFrame.getInstance()
        ));
    }
}