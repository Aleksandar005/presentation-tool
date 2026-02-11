package raf.graffito.dsw.mediator;

/**
 * Bazna klasa za UI komponente koje komuniciraju preko Mediatora.
 */
public abstract class SlideColleague {

    protected ISlideMediator mediator;

    public SlideColleague(ISlideMediator mediator) {
        this.mediator = mediator;
    }

    public void setMediator(ISlideMediator mediator) {
        this.mediator = mediator;
    }
}