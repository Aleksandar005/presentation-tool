package raf.graffito.dsw.mediator;

import lombok.Getter;
import raf.graffito.dsw.controller.listeners.mediator_listeners.StrategyChangeListener;

import javax.swing.*;

/**
 * Colleague koji predstavlja grupu radio button-a za izbor strategije provere prostora.
 * Komunicira isključivo preko Mediatora - ne zna za SpaceChecker direktno.
 */
@Getter
public class StrategyRadioColleague extends SlideColleague {

    private final ButtonGroup buttonGroup;
    private final JRadioButton rbSimpleArea;
    private final JRadioButton rbPixelMatrix;

    public StrategyRadioColleague(ISlideMediator mediator) {
        super(mediator);

        buttonGroup = new ButtonGroup();

        rbSimpleArea = new JRadioButton("Sabiranje površina");
        rbSimpleArea.setToolTipText("Sabira površine svih elemenata bez obzira na preklapanje");
        rbSimpleArea.addActionListener(new StrategyChangeListener(mediator, this, "STRATEGY_SIMPLE_AREA"));

        rbPixelMatrix = new JRadioButton("Binarna matrica");
        rbPixelMatrix.setToolTipText("Koristi matricu piksela - rešava problem preklapanja");
        rbPixelMatrix.addActionListener(new StrategyChangeListener(mediator, this, "STRATEGY_PIXEL_MATRIX"));

        buttonGroup.add(rbSimpleArea);
        buttonGroup.add(rbPixelMatrix);

        // Podrazumevano - sabiranje površina
        rbSimpleArea.setSelected(true);
    }
}