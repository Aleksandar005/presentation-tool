package raf.graffito.dsw.mediator;

import lombok.Getter;
import raf.graffito.dsw.controller.listeners.mediator_listeners.WindowModeChangeListener;

import javax.swing.*;

/**
 * Colleague koji predstavlja grupu radio button-a za izbor režima prozora.
 * Komunicira isključivo preko Mediatora - ne zna za WindowModeManager direktno.
 */
@Getter
public class WindowModeRadioColleague extends SlideColleague {

    private final ButtonGroup buttonGroup;
    private final JRadioButton rbNormal;
    private final JRadioButton rbFullscreen;
    private final JRadioButton rbSmall;

    public WindowModeRadioColleague(ISlideMediator mediator) {
        super(mediator);

        buttonGroup = new ButtonGroup();

        rbNormal = new JRadioButton("Normal");
        rbNormal.setToolTipText("Normalna veličina prozora (80% ekrana)");
        rbNormal.addActionListener(new WindowModeChangeListener(mediator, this, "MODE_NORMAL"));

        rbFullscreen = new JRadioButton("Fullscreen");
        rbFullscreen.setToolTipText("Prozor zauzima ceo ekran");
        rbFullscreen.addActionListener(new WindowModeChangeListener(mediator, this, "MODE_FULLSCREEN"));

        rbSmall = new JRadioButton("Small");
        rbSmall.setToolTipText("Veličina 2x manja od normalne");
        rbSmall.addActionListener(new WindowModeChangeListener(mediator, this, "MODE_SMALL"));

        buttonGroup.add(rbNormal);
        buttonGroup.add(rbFullscreen);
        buttonGroup.add(rbSmall);

        // Podrazumevano - Normal režim
        rbNormal.setSelected(true);
    }
}