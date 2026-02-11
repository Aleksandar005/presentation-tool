package raf.graffito.dsw.strategy;

import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.model.implementation.slide.SlideElement;

import java.awt.*;

/**
 * Način 2: Slajd posmatrati kao binarnu matricu piksela (0=slobodan, 1=zauzet piksel),
 * čime se rešava problem preklapanja iz 1. načina.
 */
public class PixelMatrixStrategy implements SpaceCheckStrategy {

    @Override
    public double calculateOccupiedSpace(Slide slide) {
        if (slide == null) return 0.0;

        int width = SlideView.SLIDE_WIDTH;
        int height = SlideView.SLIDE_HEIGHT;

        // Binarna matrica: false = slobodno, true = zauzeto
        boolean[][] pixelMatrix = new boolean[width][height];

        // Popuni matricu za svaki element
        for (GraffNode child : slide.getChildren()) {
            if (child instanceof SlideElement element) {
                markElementInMatrix(pixelMatrix, element, width, height);
            }
        }

        // Prebroj zauzete piksele
        int occupiedPixels = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (pixelMatrix[x][y]) {
                    occupiedPixels++;
                }
            }
        }

        int totalPixels = width * height;
        return (double) occupiedPixels / totalPixels;
    }

    /**
     * Označava piksele koje element zauzima u matrici.
     */
    private void markElementInMatrix(boolean[][] matrix, SlideElement element,
                                     int matrixWidth, int matrixHeight) {
        Point loc = element.getLocation();
        Dimension dim = element.getDimension();

        // Granice elementa (osiguraj da su unutar slajda)
        int startX = Math.max(0, loc.x);
        int startY = Math.max(0, loc.y);
        int endX = Math.min(matrixWidth - 1, loc.x + dim.width - 1);
        int endY = Math.min(matrixHeight - 1, loc.y + dim.height - 1);

        // Označi sve piksele koje element pokriva
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                matrix[x][y] = true;
            }
        }
    }

    @Override
    public String getStrategyName() {
        return "Binarna matrica";
    }
}