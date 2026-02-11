package raf.graffito.dsw.gui.swing;

import raf.graffito.dsw.controller.ActionManager;
import raf.graffito.dsw.controller.events.TreeDoubleClickAdapter;
import raf.graffito.dsw.tree.GraffTree;
import raf.graffito.dsw.tree.GraffTreeImplementation;
import raf.graffito.dsw.tree.view.GraffTreeView;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.observer.Subscriber;
import javax.swing.*;
import java.awt.*;
import lombok.*;

@Getter
public class MainFrame extends JFrame implements Subscriber {

    // Buduća polja za sve komponente view-a na glavnom prozoru

    private static MainFrame instance;
    private ActionManager actionManager;
    private GraffTree graffTree;

    private MainFrame() {
        instance = this;
        initialize();
    }

    public static MainFrame getInstance(){
        if(instance == null)
            instance = new MainFrame();

        return instance;
    }

    private void initialize() {
        this.actionManager = new ActionManager();

        graffTree = new GraffTreeImplementation();
        GraffTreeView graffTreeView = graffTree.generateTree();

        JScrollPane leftScroll = new JScrollPane(graffTreeView);
        leftScroll.setMinimumSize(new Dimension(100, 100));

        RightPanel rightPanel = new RightPanel();
        actionManager.initRightPanelActions(rightPanel);
        graffTreeView.addMouseListener(new TreeDoubleClickAdapter(actionManager.getOpenProjectAction()));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll, rightPanel);
        splitPane.setResizeWeight(0.25); // ~25% širine za tree
        add(splitPane, BorderLayout.CENTER);

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize((int)(screenWidth * 0.8), (int)(screenHeight * 0.8));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Graffito");

        MyMenuBar menu = new MyMenuBar();
        setJMenuBar(menu);

        MyToolBar toolBar = new MyToolBar();
        JScrollPane toolBarScroll = new JScrollPane(toolBar,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        toolBarScroll.setBorder(null);
        toolBarScroll.setPreferredSize(new Dimension(0, toolBar.getPreferredSize().height + 20));
        add(toolBarScroll, BorderLayout.NORTH);
    }

    @Override
    public void update(Message message) {
        switch(message.getType()){
            case ERROR, WARNING -> SwingUtilities.invokeLater(() -> {
                int type = (message.getType() == MessageType.ERROR) ? JOptionPane.ERROR_MESSAGE : JOptionPane.WARNING_MESSAGE;
                JOptionPane.showMessageDialog(
                        this,
                        message.getContent(),
                        message.getType().toString(),
                        type
                );
            });
            default -> {
                // Obavestenja se ne prikazuju
            }
        }
    }
}