package raf.graffito.dsw.state.concrete;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.model.implementation.slide.TextSlideElement;
import raf.graffito.dsw.state.State;
import raf.graffito.dsw.strategy.SpaceChecker;
import raf.graffito.dsw.util.ElementNameGenerator;

import javax.swing.*;
import java.awt.*;

public class AddTextState implements State {

    private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 24);
    private static final Color DEFAULT_COLOR = Color.BLACK;

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
        // PROVERA PROSTORA - koristi Strategy pattern
        SpaceChecker spaceChecker = ApplicationFramework.getInstance().getSpaceChecker();
        if (!spaceChecker.hasEnoughSpace(currentSlide)) {
            ApplicationFramework.getInstance().getMessageGenerator()
                    .notify(new Message(spaceChecker.getErrorMessage(currentSlide), MessageType.ERROR, currentSlide));
            return false;
        }

        String text = showTextInputDialog();

        if (text != null && !text.trim().isEmpty()) {
            ApplicationFramework.getInstance().getCommandManager().saveStateBeforeChange();
            TextSlideElement element = new TextSlideElement(
                    currentSlide,
                    ElementNameGenerator.nextTextName(currentSlide),
                    text.trim(),
                    slidePoint,
                    DEFAULT_FONT,
                    DEFAULT_COLOR
            );

            currentSlide.addChild(element);
        }
        return false;
    }

    private String showTextInputDialog() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel("Unesite tekst:");
        JTextArea textArea = new JTextArea(3, 20);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);

        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Dodavanje teksta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            return textArea.getText();
        }
        return null;
    }

    @Override
    public void onStateActivated() {
        ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                "[AddTextState] Activated", MessageType.INFO, this
        ));
    }
}