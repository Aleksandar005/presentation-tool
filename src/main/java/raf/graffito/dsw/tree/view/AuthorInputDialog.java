package raf.graffito.dsw.tree.view;

import javax.swing.*;
import java.awt.*;

public class AuthorInputDialog extends JDialog {
    private JTextField authorField;
    private boolean confirmed = false;

    public AuthorInputDialog(Frame parent) {
        super(parent, "Enter Author", true); // modal dialog

        setLayout(new BorderLayout(10, 10));

        authorField = new JTextField(20);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("Author:"), BorderLayout.NORTH);
        centerPanel.add(authorField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    public String getAuthor() {
        return confirmed ? authorField.getText().trim() : null;
    }
}
