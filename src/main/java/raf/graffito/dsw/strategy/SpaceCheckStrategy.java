package raf.graffito.dsw.strategy;

import raf.graffito.dsw.model.implementation.slide.Slide;

/**
 * Strategy interfejs za proveru dostupnog prostora na slajdu.
 * Omogućava dinamičku promenu načina izračunavanja zauzetosti.
 */
public interface SpaceCheckStrategy {

    /**
     * Izračunava procenat zauzetog prostora na slajdu.
     */
    double calculateOccupiedSpace(Slide slide);

    /**
     * Vraća naziv strategije (za prikaz u UI).
     */
    String getStrategyName();
}