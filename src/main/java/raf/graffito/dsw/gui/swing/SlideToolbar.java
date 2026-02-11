package raf.graffito.dsw.gui.swing;

import lombok.Getter;
import raf.graffito.dsw.controller.slideElementActions.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

@Getter
public class SlideToolbar extends JPanel implements Scrollable {

    private JButton btnAddLogo;
    private JButton btnAddText;
    private JButton btnSelect;
    private JButton btnDelete;
    private JButton btnZoom;
    private JButton btnMove;
    private JButton btnResize;
    private JButton btnRotateLeft;
    private JButton btnRotateRight;
    private JButton btnUndo;
    private JButton btnRedo;

    // DODATO: Copy/Paste dugmići
    private JButton btnCopy;
    private JButton btnPaste;

    private SlideView slideView;

    public SlideToolbar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));
        setBackground(new Color(245, 245, 245));

        createUndoRedoSection();
        createCopyPasteSection();  // DODATO
        createAddElementsSection();
        createToolsSection();
    }

    public void connectToSlideView(SlideView slideView) {
        this.slideView = slideView;

        btnAddText.addActionListener(new AddTextSlideElementAction(slideView));
        btnDelete.addActionListener(new DeleteSlideElementAction(slideView));
        btnAddLogo.addActionListener(new AddLogoSlideElementAction(slideView));
        btnSelect.addActionListener(new SelectSlideElementAction(slideView));
        btnZoom.addActionListener(new ZoomSlideElementAction(slideView));
        btnMove.addActionListener(new MoveSlideElementAction(slideView));
        btnResize.addActionListener(new ResizeSlideElementAction(slideView));
        btnRotateLeft.addActionListener(new RotateLeftAction(slideView));
        btnRotateRight.addActionListener(new RotateRightAction(slideView));

        btnUndo.addActionListener(MainFrame.getInstance().getActionManager().getUndoAction());
        btnRedo.addActionListener(MainFrame.getInstance().getActionManager().getRedoAction());

        btnCopy.addActionListener(new CopySlideElementAction(slideView));
        btnPaste.addActionListener(new PasteSlideElementAction(slideView));

        slideView.getStateManager().setSelectState();
    }

    private void createUndoRedoSection() {
        JPanel section = createSection("History");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnUndo = createToolButton("Undo", new Color(255, 152, 0));
        btnRedo = createToolButton("Redo", new Color(76, 175, 80));

        buttonPanel.add(btnUndo);
        buttonPanel.add(btnRedo);

        section.add(Box.createVerticalStrut(5));
        section.add(buttonPanel);
        section.add(Box.createVerticalStrut(5));

        add(section);
        add(Box.createVerticalStrut(8));
    }

    // Sekcija za Copy/Paste
    private void createCopyPasteSection() {
        JPanel section = createSection("Copy/Paste");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnCopy = createToolButton("Copy", new Color(63, 81, 181));
        btnPaste = createToolButton("Paste", new Color(0, 150, 136));

        buttonPanel.add(btnCopy);
        buttonPanel.add(btnPaste);

        section.add(Box.createVerticalStrut(5));
        section.add(buttonPanel);
        section.add(Box.createVerticalStrut(5));

        add(section);
        add(Box.createVerticalStrut(8));
    }

    private void createAddElementsSection() {
        JPanel section = createSection("Add Element");

        btnAddLogo = createElementButton("Logo", new Color(255, 140, 0), "L");
        btnAddText = createElementButton("Text", new Color(50, 50, 150), "T");

        // Info labela za slike
        JLabel imageInfoLabel = new JLabel("<html><center><small><i>Images are added<br>from the library below</i></small></center></html>");
        imageInfoLabel.setForeground(new Color(120, 120, 120));
        imageInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageInfoLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        section.add(Box.createVerticalStrut(5));
        section.add(btnAddLogo);
        section.add(Box.createVerticalStrut(6));
        section.add(btnAddText);
        section.add(Box.createVerticalStrut(8));
        section.add(imageInfoLabel);

        add(section);
        add(Box.createVerticalStrut(8));
    }

    private void createToolsSection() {
        JPanel section = createSection("Tools");

        btnSelect = createToolButton("Select", new Color(105, 105, 105));
        btnMove = createToolButton("Move", new Color(100, 149, 237));
        btnResize = createToolButton("Resize", new Color(255, 140, 0));
        btnDelete = createToolButton("Delete", new Color(220, 20, 60));
        btnZoom = createToolButton("Zoom", new Color(70, 130, 180));
        btnRotateLeft = createToolButton("Rotate left", new Color(138, 43, 226));
        btnRotateRight = createToolButton("Rotate right", new Color(138, 43, 226));

        section.add(Box.createVerticalStrut(5));
        section.add(createCenteredButtonWrapper(btnSelect));
        section.add(Box.createVerticalStrut(5));
        section.add(createCenteredButtonWrapper(btnMove));
        section.add(Box.createVerticalStrut(5));
        section.add(createCenteredButtonWrapper(btnResize));
        section.add(Box.createVerticalStrut(5));
        section.add(createCenteredButtonWrapper(btnDelete));
        section.add(Box.createVerticalStrut(5));
        section.add(createCenteredButtonWrapper(btnZoom));
        section.add(Box.createVerticalStrut(8));

        // Separator za rotaciju
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        section.add(separator);
        section.add(Box.createVerticalStrut(8));

        // Rotacija u horizontalnom panelu
        JPanel rotatePanel = new JPanel(new GridLayout(1, 2, 4, 0));
        rotatePanel.setOpaque(false);
        rotatePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        rotatePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRotateLeft.setText("← 90");
        btnRotateRight.setText("90 →");

        rotatePanel.add(btnRotateLeft);
        rotatePanel.add(btnRotateRight);

        section.add(rotatePanel);
        section.add(Box.createVerticalStrut(5));

        add(section);
    }

    // Kreira sekciju sa okvirom i naslovom.
    private JPanel createSection(String title) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                title,
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 11),
                new Color(80, 80, 80)
        ));
        return section;
    }

    // Kreira wrapper panel za centriranje dugmeta.
    private JPanel createCenteredButtonWrapper(JButton button) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        wrapper.add(button);
        return wrapper;
    }

    // Kreira dugme za dodavanje elementa (Logo, Tekst).
    private JButton createElementButton(String text, Color color, String symbol) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout(3, 3));
        button.setPreferredSize(new Dimension(150, 60));
        button.setMaximumSize(new Dimension(150, 60));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);

        // Panel sa simbolom
        JPanel symbolPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Pozadina
                g2d.setColor(color.brighter());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

                // Simbol
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 22));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(symbol)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 3;
                g2d.drawString(symbol, x, y);
            }
        };
        symbolPanel.setPreferredSize(new Dimension(40, 40));

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));

        button.add(symbolPanel, BorderLayout.CENTER);
        button.add(label, BorderLayout.SOUTH);

        return button;
    }

    // Kreira standardno dugme za alat.
    private JButton createToolButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 11));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 30));
        button.setMaximumSize(new Dimension(140, 30));
        button.setMinimumSize(new Dimension(140, 30));

        // Hover efekat
        Color hoverColor = baseColor.brighter();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(baseColor);
            }
        });

        return button;
    }

    // ========== Scrollable interfejs implementacija ==========

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(180, 400);
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 20;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 60;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true; // Toolbar prati širinu viewporta
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false; // NE prati visinu - omogućava vertikalni scroll
    }
}