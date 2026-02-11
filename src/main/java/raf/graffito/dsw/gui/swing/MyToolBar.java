package raf.graffito.dsw.gui.swing;

import raf.graffito.dsw.controller.AboutUsAction;
import raf.graffito.dsw.controller.ExitAction;
import raf.graffito.dsw.controller.AddNodeAction;
import raf.graffito.dsw.controller.RemoveNodeAction;
import raf.graffito.dsw.controller.RenameNodeAction;
import raf.graffito.dsw.controller.saving.*;
import raf.graffito.dsw.mediator.SlideMediator;
import raf.graffito.dsw.mediator.StrategyRadioColleague;
import raf.graffito.dsw.mediator.WindowModeRadioColleague;

import javax.swing.*;
import java.awt.*;

public class MyToolBar extends JToolBar {

    // MEDIATOR: Za radio buttone
    private SlideMediator mediator;
    private StrategyRadioColleague strategyColleague;
    private WindowModeRadioColleague windowModeColleague;

    public MyToolBar() {
        super(HORIZONTAL);
        setFloatable(false);

        ExitAction exit = MainFrame.getInstance().getActionManager().getExitAction();
        AboutUsAction aboutUs = MainFrame.getInstance().getActionManager().getAboutUsAction();
        AddNodeAction addNode = MainFrame.getInstance().getActionManager().getAddNodeAction();
        RemoveNodeAction removeNode = MainFrame.getInstance().getActionManager().getRemoveNodeAction();
        RenameNodeAction renameNode = MainFrame.getInstance().getActionManager().getRenameNodeAction();
        SaveProjectAction saveProjectAction = MainFrame.getInstance().getActionManager().getSaveProjectAction();
        SaveAsProjectAction saveAsProjectAction = MainFrame.getInstance().getActionManager().getSaveAsProjectAction();
        SaveAsTemplateAction saveAsTemplateAction = MainFrame.getInstance().getActionManager().getSaveAsTemplateAction();
        OpenProjectFromFileAction  openProjectFromFileAction = MainFrame.getInstance().getActionManager().getOpenProjectFromFileAction();
        LoadTemplateAction loadTemplateAction = MainFrame.getInstance().getActionManager().getLoadTemplateAction();

        add(exit);
        add(aboutUs);
        add(addNode);
        add(removeNode);
        add(renameNode);
        add(saveProjectAction);
        add(saveAsProjectAction);
        add(saveAsTemplateAction);
        add(openProjectFromFileAction);
        add(loadTemplateAction);

        addSeparator(new Dimension(30, 0));

        // Inicijalizacija Mediatora
        mediator = new SlideMediator();
        strategyColleague = new StrategyRadioColleague(mediator);
        windowModeColleague = new WindowModeRadioColleague(mediator);
        mediator.setStrategyColleague(strategyColleague);
        mediator.setWindowModeColleague(windowModeColleague);

        // Dodaj radio buttone za strategiju
        createStrategySection();

        addSeparator(new Dimension(20, 0));

        // Dodaj radio buttone za režim prozora
        createWindowModeSection();
    }

    private void createStrategySection() {
        JLabel label = new JLabel("Provera prostora: ");
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        add(label);

        add(strategyColleague.getRbSimpleArea());
        add(strategyColleague.getRbPixelMatrix());
    }

    private void createWindowModeSection() {
        JLabel label = new JLabel("Režim prozora: ");
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        add(label);

        add(windowModeColleague.getRbNormal());
        add(windowModeColleague.getRbFullscreen());
        add(windowModeColleague.getRbSmall());
    }
}