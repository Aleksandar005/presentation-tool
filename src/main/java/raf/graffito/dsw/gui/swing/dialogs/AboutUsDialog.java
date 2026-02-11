package raf.graffito.dsw.gui.swing.dialogs;

import raf.graffito.dsw.gui.swing.MainFrame;

import javax.swing.*;
import java.awt.*;

public class AboutUsDialog extends JDialog {
    public AboutUsDialog(){
        super(MainFrame.getInstance(), "About Us", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        this.setIconImage(new ImageIcon(this.getClass().getResource("/images/about-us.png")).getImage());

        JPanel membersPanel = new JPanel(new GridLayout(1, 2, 20, 0)); // 1 red, 2 kolone, razmak 20px
        membersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // CLAN 1

        JPanel member1 = new JPanel();
        member1.setLayout(new BoxLayout(member1, BoxLayout.Y_AXIS));

        ImageIcon img1 = new ImageIcon(getClass().getResource("/images/member1-aleksandar.jpg"));
        Image scaled1 = img1.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel pic1 = new JLabel(new ImageIcon(scaled1));
        JLabel name1 = new JLabel("Aleksandar Todorovic", SwingConstants.CENTER);
        JLabel index1 = new JLabel("RN 2/2024", SwingConstants.CENTER);

        pic1.setAlignmentX(Component.CENTER_ALIGNMENT);
        name1.setAlignmentX(Component.CENTER_ALIGNMENT);
        index1.setAlignmentX(Component.CENTER_ALIGNMENT);

        member1.add(Box.createVerticalGlue());
        member1.add(pic1);
        member1.add(Box.createVerticalStrut(10));
        member1.add(name1);
        member1.add(index1);
        member1.add(Box.createVerticalGlue());

        // CLAN 2
        JPanel member2 = new JPanel();
        member2.setLayout(new BoxLayout(member2, BoxLayout.Y_AXIS));

        ImageIcon img2 = new ImageIcon(getClass().getResource("/images/member2-ksenija.jpg"));
        Image scaled2 = img2.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel pic2 = new JLabel(new ImageIcon(scaled2));
        JLabel name2 = new JLabel("Ksenija Matovic", SwingConstants.CENTER);
        JLabel index2 = new JLabel("RN 27/2024", SwingConstants.CENTER);

        pic2.setAlignmentX(Component.CENTER_ALIGNMENT);
        name2.setAlignmentX(Component.CENTER_ALIGNMENT);
        index2.setAlignmentX(Component.CENTER_ALIGNMENT);

        member2.add(Box.createVerticalGlue());
        member2.add(pic2);
        member2.add(Box.createVerticalStrut(10));
        member2.add(name2);
        member2.add(index2);
        member2.add(Box.createVerticalGlue());


        // Dodaj clanove u panel
        membersPanel.add(member1);
        membersPanel.add(member2);

        membersPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        add(membersPanel, BorderLayout.CENTER);

        pack();
        setMinimumSize(new Dimension(500, getHeight()));
        setLocationRelativeTo(MainFrame.getInstance());
    }
}
