package raf.graffito.dsw.mediator;

import raf.graffito.dsw.bridge.WindowModeManager;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.strategy.PixelMatrixStrategy;
import raf.graffito.dsw.strategy.SimpleAreaStrategy;
import raf.graffito.dsw.strategy.SpaceChecker;

/**
 * Konkretni Mediator koji koordinira komunikaciju između:
 * - Radio button-a za strategiju provere prostora (StrategyRadioColleague)
 * - Radio button-a za režim prozora (WindowModeRadioColleague)
 * - SpaceChecker-a
 * - WindowModeManager-a
 */
public class SlideMediator implements ISlideMediator {

    private StrategyRadioColleague strategyColleague;
    private WindowModeRadioColleague windowModeColleague;

    public void setStrategyColleague(StrategyRadioColleague colleague) {
        this.strategyColleague = colleague;
    }

    public void setWindowModeColleague(WindowModeRadioColleague colleague) {
        this.windowModeColleague = colleague;
    }

    @Override
    public void notify(SlideColleague sender, String event) {

        // Događaji za promenu strategije provere prostora
        switch (event) {
            case "STRATEGY_SIMPLE_AREA" -> {
                SpaceChecker spaceChecker = ApplicationFramework.getInstance().getSpaceChecker();
                spaceChecker.setStrategy(new SimpleAreaStrategy());
                ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                        "[SlideMediator] Strategy changed: Area summation", MessageType.INFO, this
                ));
            }
            case "STRATEGY_PIXEL_MATRIX" -> {
                SpaceChecker spaceChecker = ApplicationFramework.getInstance().getSpaceChecker();
                spaceChecker.setStrategy(new PixelMatrixStrategy());
                ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                        "[SlideMediator] Strategy changed: Binary matrix", MessageType.INFO, this
                ));
            }

            // Događaji za promenu režima prozora
            case "MODE_NORMAL" -> {
                WindowModeManager manager = ApplicationFramework.getInstance().getWindowModeManager();
                manager.setNormalMode();
                manager.applyCurrentMode(MainFrame.getInstance());
                ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                        "[SlideMediator] Window mode: Normal", MessageType.INFO, this
                ));
            }
            case "MODE_FULLSCREEN" -> {
                WindowModeManager manager = ApplicationFramework.getInstance().getWindowModeManager();
                manager.setFullscreenMode();
                manager.applyCurrentMode(MainFrame.getInstance());
                ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                        "[SlideMediator] Window mode: Fullscreen", MessageType.INFO, this
                ));
            }
            case "MODE_SMALL" -> {
                WindowModeManager manager = ApplicationFramework.getInstance().getWindowModeManager();
                manager.setSmallMode();
                manager.applyCurrentMode(MainFrame.getInstance());
                ApplicationFramework.getInstance().getMessageGenerator().notify(new Message(
                        "[SlideMediator] Window mode: Small", MessageType.INFO, this
                ));
            }
        }
    }
}