package raf.graffito.dsw.strategy;

import lombok.Getter;
import lombok.Setter;
import raf.graffito.dsw.model.implementation.slide.Slide;

/**
 * Context klasa za Strategy pattern.
 * Drži trenutnu strategiju za proveru prostora i izvršava je.
 */
@Getter
public class SpaceChecker {

    // Minimalni procenat slobodnog prostora (20% = 0.2)
    private static final double MIN_FREE_SPACE = 0.20;

    @Setter
    private SpaceCheckStrategy strategy;

    public SpaceChecker() {
        // Podrazumevana strategija - sabiranje površina
        this.strategy = new SimpleAreaStrategy();
    }

    public SpaceChecker(SpaceCheckStrategy initialStrategy) {
        this.strategy = initialStrategy;
    }

    /**
     * Proverava da li ima dovoljno prostora na slajdu.
     */
    public boolean hasEnoughSpace(Slide slide) {
        if (slide == null) return false;

        double occupiedSpace = strategy.calculateOccupiedSpace(slide);
        double freeSpace = 1.0 - occupiedSpace;

        return freeSpace >= MIN_FREE_SPACE;
    }

    /**
     * Vraća procenat zauzetog prostora.
     */
    public double getOccupiedPercentage(Slide slide) {
        if (slide == null) return 0.0;
        return strategy.calculateOccupiedSpace(slide) * 100;
    }

    /**
     * Vraća procenat slobodnog prostora.
     */
    public double getFreePercentage(Slide slide) {
        if (slide == null) return 100.0;
        return (1.0 - strategy.calculateOccupiedSpace(slide)) * 100;
    }

    /**
     * Vraća naziv trenutne strategije.
     */
    public String getCurrentStrategyName() {
        return strategy.getStrategyName();
    }

    /**
     * Generiše formatiranu error poruku za prikaz korisniku.
     */
    public String getErrorMessage(Slide slide) {
        double occupied = getOccupiedPercentage(slide);
        String strategyName = getCurrentStrategyName();

        StringBuilder sb = new StringBuilder();
        sb.append("NEMA DOVOLJNO PROSTORA NA SLAJDU!\n");
        sb.append("Strategija: ").append(strategyName).append("\n");
        sb.append(String.format("Zauzeto: %.1f%%", occupied));

        // Ako je sabiranje površina i procenat > 100%, dodaj objašnjenje
        if (strategy instanceof SimpleAreaStrategy && occupied > 100) {
            sb.append(" (više od 100% zbog preklapanja elemenata računato - računato Binarnom matricom" +
                    ")");
        }

        sb.append("\nPotrebno min. 20% slobodnog prostora.");

        return sb.toString();
    }
}