package raf.graffito.dsw.mediator;

/**
 * Mediator interfejs za komunikaciju između UI komponenti i logike aplikacije.
 * Eliminiše direktne veze između UI komponenti i StateManager-a.
 */
public interface ISlideMediator {

    /**
     * Obaveštava mediator o događaju od strane colleague-a.
     * @param sender Colleague koji šalje događaj
     * @param event Tip događaja (npr. "MODE_SELECT", "MODE_ADD_TEXT", "STRATEGY_CHANGED")
     */
    void notify(SlideColleague sender, String event);
}