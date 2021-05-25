package cz.cvut.fel.pjv;

import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Implementation of sprite animations via transition of view on sprite list
 */
public class SpriteAnimation extends Transition {
    private final ImageView imageView;
    private final int width;
    private final int height;
    private int offsetY;
    private final ArrayList<Integer> columns;
    public boolean isLastIteration;

    /**
     * Constructor
     * @param imageView sprite list image view
     * @param duration duration of animation
     * @param offsetY defines Y position in sprite list to play animation
     * @param width sprite width
     * @param height sprite height
     * @param columns array with values of frame quantity of each row
     */
    public SpriteAnimation(ImageView imageView, Duration duration, int offsetY, int width, int height, ArrayList<Integer> columns) {
        this.imageView = imageView;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.columns = columns;
        setCycleDuration(duration);
        isLastIteration = false;
    }

    /**
     * Changes sprite list playing row
     * @param offsetY
     */
    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    /**
     * X offset processing with known Y offset
     * Animation goes on the row defined by Y offset and
     * ImageView of the current frame moves depending on the time and frames quantity
     * @param v
     */
    @Override
    protected void interpolate(double v) {

        /**
         * Feature to stop animation when it gets last frame
         */
        if(isLastIteration && v == 1.) {
            stop();
        }

        final int currentColumns = columns.get((int) offsetY/height); // getting quantity of frames on the row
        final int index = Math.min((int) (v * currentColumns), currentColumns - 1); // calculating frame number in the row
        final int x = index * width;
        final int y = offsetY;

        imageView.setViewport(new Rectangle2D(x, y, width, height));
    }
}
