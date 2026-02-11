package raf.graffito.dsw.tree.view;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class TemplateChooserDialog extends JDialog{
    public enum Choice {
        EMPTY_PROJECT,
        LOAD_TEMPLATE,
        CANCEL
    }

    private Choice userChoice = Choice.CANCEL;

    public TemplateChooserDialog(Frame parent) {
        super(parent, "Create Project", true);

        setLayout(new BorderLayout(10, 10));

        // Poruka
        JLabel messageLabel = new JLabel(
                "<html><center>How would you like to create your project?</center></html>",
                SwingConstants.CENTER
        );
        messageLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        // Panel sa dugmadima
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton emptyButton = new JButton("Empty Project");
        JButton templateButton = new JButton("Load Template");
        JButton cancelButton = new JButton("Cancel");

        emptyButton.addActionListener(e -> {
            userChoice = Choice.EMPTY_PROJECT;
            setVisible(false);
        });

        templateButton.addActionListener(e -> {
            userChoice = Choice.LOAD_TEMPLATE;
            setVisible(false);
        });

        cancelButton.addActionListener(e -> {
            userChoice = Choice.CANCEL;
            setVisible(false);
        });

        buttonPanel.add(emptyButton);
        buttonPanel.add(templateButton);
        buttonPanel.add(cancelButton);

        add(messageLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(350, 120));
        setLocationRelativeTo(parent);
    }

    public static Choice showDialog(Frame parent) {
        TemplateChooserDialog dialog = new TemplateChooserDialog(parent);
        dialog.setVisible(true);
        return dialog.getUserChoice();
    }
}
